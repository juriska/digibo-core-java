package com.digibo.core.controller;

import com.digibo.core.dto.request.LoginRequest;
import com.digibo.core.dto.response.AuthResponse;
import com.digibo.core.exception.UnauthorizedException;
import com.digibo.core.security.JwtTokenProvider;
import com.digibo.core.security.MockAuthenticationProvider;
import com.digibo.core.security.RsaKeyProvider;
import com.digibo.core.security.UserPrincipal;
import com.digibo.core.service.AuthPermissionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private static final String ACCESS_TOKEN_COOKIE = "access_token";
    private static final String REFRESH_TOKEN_COOKIE = "refresh_token";

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final AuthPermissionService authPermissionService;
    private final RsaKeyProvider rsaKeyProvider;

    @Value("${jwt.expiration:86400000}")
    private long jwtExpiration;

    @Value("${jwt.refresh-expiration:604800000}")
    private long refreshExpiration;

    @Value("${cookie.secure:false}")
    private boolean secureCookie;

    @Value("${cookie.same-site:Lax}")
    private String sameSite;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenProvider tokenProvider,
                          AuthPermissionService authPermissionService,
                          RsaKeyProvider rsaKeyProvider) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.authPermissionService = authPermissionService;
        this.rsaKeyProvider = rsaKeyProvider;
    }

    @GetMapping("/public-key")
    public ResponseEntity<Map<String, String>> getPublicKey() {
        return ResponseEntity.ok(Map.of("publicKey", rsaKeyProvider.getPublicKeyBase64()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request,
                                               HttpServletResponse response) {
        String password;
        try {
            password = rsaKeyProvider.decrypt(request.getPassword());
        } catch (IllegalArgumentException e) {
            throw new UnauthorizedException("Invalid username or password");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            password
                    )
            );

            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

            // Use permissions from authentication provider if already populated (mock users),
            // otherwise fetch from Oracle (real users)
            Set<String> permissions = userPrincipal.getPermissions().isEmpty()
                    ? authPermissionService.getUserPermissions(userPrincipal.getUsername())
                    : userPrincipal.getPermissions();
            logger.info("User {} logged in with {} permissions", userPrincipal.getUsername(), permissions.size());

            // Get roles from authentication
            List<String> roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            // Generate tokens
            String accessToken = tokenProvider.generateToken(
                    userPrincipal.getUsername(),
                    userPrincipal.getUserId(),
                    roles,
                    permissions
            );
            String refreshToken = tokenProvider.generateRefreshToken(userPrincipal.getUsername());

            // Set httpOnly cookies
            addTokenCookie(response, ACCESS_TOKEN_COOKIE, accessToken, (int) (jwtExpiration / 1000));
            addTokenCookie(response, REFRESH_TOKEN_COOKIE, refreshToken, (int) (refreshExpiration / 1000));

            return ResponseEntity.ok(AuthResponse.of(
                    userPrincipal.getUserId(),
                    userPrincipal.getUsername(),
                    roles,
                    permissions
            ));
        } catch (AuthenticationException e) {
            throw new UnauthorizedException("Invalid username or password");
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = getTokenFromCookie(request, REFRESH_TOKEN_COOKIE);

        if (refreshToken == null || !tokenProvider.validateToken(refreshToken) || !tokenProvider.isRefreshToken(refreshToken)) {
            throw new UnauthorizedException("Invalid refresh token");
        }

        String username = tokenProvider.getUsernameFromToken(refreshToken);

        // For mock users, use predefined data; for real users, fetch from Oracle
        Set<String> permissions;
        List<String> roles;
        String userId;

        if (MockAuthenticationProvider.isMockUser(username)) {
            permissions = MockAuthenticationProvider.getMockUserPermissions(username);
            roles = MockAuthenticationProvider.getMockUserRoles(username);
            userId = MockAuthenticationProvider.getMockUserId(username);
            logger.debug("Refreshed token for mock user {} with {} permissions", username, permissions.size());
        } else {
            permissions = authPermissionService.getUserPermissions(username);
            // TODO: look up real user details from database
            roles = List.of("USER");
            userId = "user-id";
            logger.debug("Refreshed token for user {} with {} permissions", username, permissions.size());
        }

        String newAccessToken = tokenProvider.generateToken(username, userId, roles, permissions);
        String newRefreshToken = tokenProvider.generateRefreshToken(username);

        // Set new httpOnly cookies
        addTokenCookie(response, ACCESS_TOKEN_COOKIE, newAccessToken, (int) (jwtExpiration / 1000));
        addTokenCookie(response, REFRESH_TOKEN_COOKIE, newRefreshToken, (int) (refreshExpiration / 1000));

        return ResponseEntity.ok(AuthResponse.of(
                userId,
                username,
                roles,
                permissions
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpServletResponse response) {
        // Clear cookies by setting maxAge to 0
        clearTokenCookie(response, ACCESS_TOKEN_COOKIE);
        clearTokenCookie(response, REFRESH_TOKEN_COOKIE);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Logged out successfully"
        ));
    }

    @GetMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateToken(HttpServletRequest request) {
        // Try to get token from cookie first, then from header
        String token = getTokenFromCookie(request, ACCESS_TOKEN_COOKIE);

        if (token == null) {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }
        }

        if (token == null) {
            throw new UnauthorizedException("No token provided");
        }

        if (!tokenProvider.validateToken(token)) {
            throw new UnauthorizedException("Invalid token");
        }

        return ResponseEntity.ok(Map.of(
                "valid", true,
                "username", tokenProvider.getUsernameFromToken(token),
                "userId", tokenProvider.getUserIdFromToken(token),
                "roles", tokenProvider.getRolesFromToken(token),
                "permissions", tokenProvider.getPermissionsFromToken(token)
        ));
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser(HttpServletRequest request) {
        String token = getTokenFromCookie(request, ACCESS_TOKEN_COOKIE);

        if (token == null) {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }
        }

        if (token == null || !tokenProvider.validateToken(token)) {
            throw new UnauthorizedException("Not authenticated");
        }

        return ResponseEntity.ok(Map.of(
                "userId", tokenProvider.getUserIdFromToken(token),
                "username", tokenProvider.getUsernameFromToken(token),
                "roles", tokenProvider.getRolesFromToken(token),
                "permissions", tokenProvider.getPermissionsFromToken(token)
        ));
    }

    private void addTokenCookie(HttpServletResponse response, String name, String value, int maxAgeSeconds) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(secureCookie);
        cookie.setPath("/");
        cookie.setMaxAge(maxAgeSeconds);
        // Note: SameSite attribute requires using response header for full control
        response.addCookie(cookie);

        // Add SameSite attribute via header (Cookie API doesn't support it directly)
        String cookieHeader = String.format("%s=%s; Max-Age=%d; Path=/; HttpOnly; SameSite=%s%s",
                name, value, maxAgeSeconds, sameSite, secureCookie ? "; Secure" : "");
        response.addHeader("Set-Cookie", cookieHeader);
    }

    private void clearTokenCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, "");
        cookie.setHttpOnly(true);
        cookie.setSecure(secureCookie);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    private String getTokenFromCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            return Arrays.stream(cookies)
                    .filter(c -> cookieName.equals(c.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}

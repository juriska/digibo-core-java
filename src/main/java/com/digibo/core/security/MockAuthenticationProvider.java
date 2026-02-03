package com.digibo.core.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Mock authentication provider for development/testing.
 * Handles user1/user2/user3 with predefined roles without LDAP.
 * For all other users, authentication is delegated to the next provider (LDAP).
 */
@Component
public class MockAuthenticationProvider implements AuthenticationProvider {

    private static final Logger logger = LoggerFactory.getLogger(MockAuthenticationProvider.class);

    /** Mock user definitions with userId, password, roles, and permissions */
    private static final Map<String, MockUser> MOCK_USERS = Map.of(
            "user1", new MockUser(
                    "mock-001",
                    "password1",
                    List.of("RBOFFORDERS", "RBOPAYMENT", "RBOPAYMENTVIEW", "ADMIN"),
                    Set.of(
                            "BO_CUSTOMER.FIND", "BO_CUSTOMER.CREATE", "BO_CUSTOMER.UPDATE",
                            "BO_PAYMENT.FIND", "BO_PAYMENT.CREATE", "BO_PAYMENT.UPDATE",
                            "BO_ORDERS.FIND", "BO_ORDERS.CREATE", "BO_ORDERS.UPDATE"
                    )
            ),
            "user2", new MockUser(
                    "mock-002",
                    "password2",
                    List.of("RBOFFORDERS"),
                    Set.of("BO_ORDERS.FIND", "BO_ORDERS.CREATE", "BO_ORDERS.UPDATE")
            ),
            "user3", new MockUser(
                    "mock-003",
                    "password3",
                    List.of("RBOPAYMENT", "RBOPAYMENTVIEW"),
                    Set.of("BO_PAYMENT.FIND", "BO_PAYMENT.CREATE", "BO_PAYMENT.UPDATE")
            )
    );

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        // Check if this is a mock user
        MockUser mockUser = MOCK_USERS.get(username.toLowerCase());

        if (mockUser == null) {
            // Not a mock user - return null to let next provider handle it
            logger.debug("User '{}' is not a mock user, delegating to next provider", username);
            return null;
        }

        // Validate password for mock user
        if (!mockUser.password.equals(password)) {
            logger.warn("Invalid password for mock user '{}'", username);
            throw new BadCredentialsException("Invalid password for user: " + username);
        }

        // Create UserPrincipal with roles and permissions
        UserPrincipal principal = UserPrincipal.create(
                mockUser.userId,
                username,
                password,
                mockUser.roles,
                mockUser.permissions
        );

        logger.info("Mock user '{}' authenticated successfully with roles: {}", username, mockUser.roles);

        return new UsernamePasswordAuthenticationToken(
                principal,
                password,
                principal.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    /** Internal record for mock user data */
    private record MockUser(
            String userId,
            String password,
            List<String> roles,
            Set<String> permissions
    ) {}
}

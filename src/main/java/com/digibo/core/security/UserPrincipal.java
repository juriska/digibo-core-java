package com.digibo.core.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {

    private final String userId;
    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Set<String> permissions;

    public UserPrincipal(String userId, String username, String password,
                         Collection<? extends GrantedAuthority> authorities,
                         Set<String> permissions) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.permissions = permissions != null ? new HashSet<>(permissions) : Collections.emptySet();
    }

    // Backward-compatible constructor
    public UserPrincipal(String userId, String username, String password,
                         Collection<? extends GrantedAuthority> authorities) {
        this(userId, username, password, authorities, Collections.emptySet());
    }

    public static UserPrincipal create(String userId, String username, String password, List<String> roles) {
        return create(userId, username, password, roles, Collections.emptySet());
    }

    public static UserPrincipal create(String userId, String username, String password,
                                        List<String> roles, Set<String> permissions) {
        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());

        return new UserPrincipal(userId, username, password, authorities, permissions);
    }

    public static UserPrincipal fromToken(String userId, String username, List<String> roles) {
        return fromToken(userId, username, roles, Collections.emptySet());
    }

    public static UserPrincipal fromToken(String userId, String username, List<String> roles, Set<String> permissions) {
        List<GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new UserPrincipal(userId, username, null, authorities, permissions);
    }

    /**
     * Get the user's Oracle PL/SQL permissions.
     * @return Set of permissions in format "PACKAGE.PROCEDURE"
     */
    public Set<String> getPermissions() {
        return Collections.unmodifiableSet(permissions);
    }

    /**
     * Check if user has a specific permission.
     * @param permission Permission string in format "PACKAGE.PROCEDURE"
     * @return true if user has the permission
     */
    public boolean hasPermission(String permission) {
        return permissions.contains(permission.toUpperCase());
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

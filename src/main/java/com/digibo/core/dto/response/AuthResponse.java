package com.digibo.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String userId;
    private String username;
    private List<String> roles;
    private Set<String> permissions;
    private String sessionId;

    public static AuthResponse of(String userId, String username,
                                   Collection<String> roles, Set<String> permissions) {
        return of(userId, username, roles, permissions, null);
    }

    public static AuthResponse of(String userId, String username,
                                   Collection<String> roles, Set<String> permissions,
                                   String sessionId) {
        return AuthResponse.builder()
                .userId(userId)
                .username(username)
                .roles(roles != null ? List.copyOf(roles) : List.of())
                .permissions(permissions != null ? Set.copyOf(permissions) : Set.of())
                .sessionId(sessionId)
                .build();
    }
}

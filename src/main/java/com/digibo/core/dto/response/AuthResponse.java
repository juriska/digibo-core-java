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
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private String userId;
    private String username;
    private List<String> roles;
    private Set<String> permissions;

    public static AuthResponse of(String accessToken, String refreshToken, String userId, String username) {
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .userId(userId)
                .username(username)
                .build();
    }

    public static AuthResponse of(String accessToken, String refreshToken, String userId, String username,
                                   Collection<String> roles, Set<String> permissions) {
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .userId(userId)
                .username(username)
                .roles(roles != null ? List.copyOf(roles) : List.of())
                .permissions(permissions != null ? Set.copyOf(permissions) : Set.of())
                .build();
    }
}

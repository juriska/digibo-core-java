package com.digibo.core.dto.request;

import lombok.Data;

@Data
public class CheckLoginRequest {
    private Long userId;
    private String login;
    private String license;
    private Long channelId;
}

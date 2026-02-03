package com.digibo.core.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private String error;
    private String message;
    private String code;
    private String details;
    private String packageName;
    private String procedure;
    private String stackTrace;
    private Map<String, String> fieldErrors;
}

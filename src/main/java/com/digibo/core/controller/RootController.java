package com.digibo.core.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * RootController - Handles root endpoint
 */
@RestController
public class RootController {

    @Value("${spring.profiles.active:unknown}")
    private String activeProfile;

    @GetMapping("/")
    public ResponseEntity<String> root() {
        return ResponseEntity.ok("API Gateway is up and running");
    }

    @GetMapping("/api/status")
    public ResponseEntity<Map<String, Object>> status() {
        return ResponseEntity.ok(Map.of(
                "status", "running",
                "profile", activeProfile,
                "timestamp", System.currentTimeMillis()
        ));
    }
}

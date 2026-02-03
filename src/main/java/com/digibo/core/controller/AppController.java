package com.digibo.core.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * AppController - REST controller for application-level operations
 * Maps to /api/app endpoints
 */
@RestController
@RequestMapping("/api/app")
public class AppController {

    /**
     * GET /api/app/user/modules
     * Get user modules with permissions
     */
    @GetMapping("/user/modules")
    public ResponseEntity<List<Map<String, Object>>> getUserModules() {
        List<Map<String, Object>> modules = List.of(
                Map.of("name", "payments", "canView", true, "canEdit", false),
                Map.of("name", "users", "canView", true)
        );
        return ResponseEntity.ok(modules);
    }
}

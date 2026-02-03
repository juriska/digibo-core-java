package com.digibo.core.controller;

import com.digibo.core.exception.ResourceNotFoundException;
import com.digibo.core.service.SMSService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * SMSController - REST controller for SMS operations
 * Maps to /api/sms endpoints
 */
@RestController
@RequestMapping("/api/sms")
public class SMSController {

    private final SMSService smsService;

    public SMSController(SMSService smsService) {
        this.smsService = smsService;
    }

    /**
     * GET /api/sms/users/:wocId
     * Get SMS user data using BOSMS.loadUserData()
     */
    @GetMapping("/users/{wocId}")
    public ResponseEntity<Map<String, Object>> getUserData(@PathVariable Long wocId) {
        Map<String, Object> result = smsService.loadUserData(wocId);

        if (result.get("userName") == null) {
            throw new ResourceNotFoundException("User", wocId);
        }

        return ResponseEntity.ok(result);
    }
}

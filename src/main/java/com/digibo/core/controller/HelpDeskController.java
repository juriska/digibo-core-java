package com.digibo.core.controller;

import com.digibo.core.exception.ValidationException;
import com.digibo.core.service.HelpDeskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * HelpDeskController - REST controller for help desk operations
 * Maps to /api/helpdesk endpoints
 */
@RestController
@RequestMapping("/api/helpdesk")
public class HelpDeskController {

    private final HelpDeskService helpDeskService;

    public HelpDeskController(HelpDeskService helpDeskService) {
        this.helpDeskService = helpDeskService;
    }

    /**
     * GET /api/helpdesk/users/search
     * Search for user channels
     *
     * @param login User login (partial match)
     * @param authDev Authentication device (partial match)
     * @param userName User name (partial match)
     * @param personalId Personal ID (exact match)
     */
    @GetMapping("/users/search")
    public ResponseEntity<List<Map<String, Object>>> findUserChannel(
            @RequestParam(required = false) String login,
            @RequestParam(required = false) String authDev,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String personalId) {

        List<Map<String, Object>> result = helpDeskService.findUserChannel(login, authDev, userName, personalId);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/helpdesk/users/{userId}/log
     * Load log entries for a user
     *
     * @param userId User ID
     * @param wocId WOC ID (optional)
     */
    @GetMapping("/users/{userId}/log")
    public ResponseEntity<List<Map<String, Object>>> loadLog(
            @PathVariable String userId,
            @RequestParam(required = false) String wocId) {

        List<Map<String, Object>> result = helpDeskService.loadLog(userId, wocId);
        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/helpdesk/users/{userId}/password
     * Set password for a user channel
     */
    @PostMapping("/users/{userId}/password")
    public ResponseEntity<Map<String, Object>> setPassword(
            @PathVariable String userId,
            @RequestBody Map<String, String> request) {

        String channelId = request.get("channelId");
        String password = request.get("password");

        if (channelId == null || channelId.isBlank() || password == null || password.isBlank()) {
            throw new ValidationException("Missing required fields: channelId, password");
        }

        int resultCode = helpDeskService.setPassword(channelId, userId, password);

        return ResponseEntity.ok(Map.of(
                "success", resultCode == 0,
                "resultCode", resultCode,
                "message", resultCode == 0 ? "Password updated successfully" : "Failed to update password"
        ));
    }

    /**
     * GET /api/helpdesk/channels/{id}
     * Load user channel details
     */
    @GetMapping("/channels/{id}")
    public ResponseEntity<Map<String, Object>> loadUserChannel(@PathVariable Long id) {
        Map<String, Object> result = helpDeskService.loadUserChannel(id);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/helpdesk/channels/{id}/auth
     * Load authentication info for a channel
     */
    @GetMapping("/channels/{id}/auth")
    public ResponseEntity<Map<String, Object>> loadAuthInfo(@PathVariable Long id) {
        Map<String, Object> result = helpDeskService.loadAuthInfo(id);
        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/helpdesk/channels/{id}/lock
     * Set lock status for a channel
     */
    @PostMapping("/channels/{id}/lock")
    public ResponseEntity<Map<String, Object>> setLock(
            @PathVariable String id,
            @RequestBody Map<String, Object> request) {

        String userId = (String) request.get("userId");
        Integer status = (Integer) request.get("status");
        Integer subStatus = (Integer) request.get("subStatus");

        if (userId == null || status == null || subStatus == null) {
            throw new ValidationException("Missing required fields: userId, status, subStatus");
        }

        boolean success = helpDeskService.setLock(id, userId, status, subStatus);

        return ResponseEntity.ok(Map.of(
                "success", success,
                "channelId", id,
                "userId", userId,
                "status", status,
                "subStatus", subStatus,
                "message", success ? "Lock status updated successfully" : "Failed to update lock status"
        ));
    }

    /**
     * POST /api/helpdesk/channels/{id}/reset-stolen
     * Reset stolen channel status
     */
    @PostMapping("/channels/{id}/reset-stolen")
    public ResponseEntity<Map<String, Object>> resetStolen(@PathVariable String id) {
        boolean success = helpDeskService.resetStolen(id);

        return ResponseEntity.ok(Map.of(
                "success", success,
                "channelId", id,
                "message", success ? "Stolen channel reset successfully" : "Failed to reset stolen channel"
        ));
    }
}

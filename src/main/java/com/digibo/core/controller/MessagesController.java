package com.digibo.core.controller;

import com.digibo.core.exception.ValidationException;
import com.digibo.core.service.MessageService;
import com.digibo.core.util.DateUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * MessagesController - REST controller for message operations
 * Maps to /api/messages endpoints
 */
@RestController
@RequestMapping("/api/messages")
public class MessagesController {

    private final MessageService messageService;

    public MessagesController(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * GET /api/messages/search
     * Search messages by various criteria
     *
     * @param userId User ID
     * @param userName User name (partial match)
     * @param login Login name (partial match)
     * @param officerId Officer ID
     * @param msgId Message ID (exact match)
     * @param message Message body text (partial match)
     * @param type Message type (Q, A)
     * @param custId Customer ID
     * @param custName Customer name (partial match)
     * @param statuses Comma-separated status IDs
     * @param classId Message class ID
     * @param dateFrom Date from (ISO format)
     * @param dateTill Date till (ISO format)
     * @param channelId Channel ID
     */
    @GetMapping("/search")
    public ResponseEntity<List<Map<String, Object>>> findMessages(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String login,
            @RequestParam(required = false) Long officerId,
            @RequestParam(required = false) String msgId,
            @RequestParam(required = false) String message,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String custId,
            @RequestParam(required = false) String custName,
            @RequestParam(required = false) String statuses,
            @RequestParam(required = false) Long classId,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTill,
            @RequestParam(required = false) String channelId) {

        List<Map<String, Object>> result = messageService.findMessages(
                userId, userName, login, officerId, msgId, message, type,
                custId, custName, statuses, classId,
                DateUtils.parseDate(dateFrom), DateUtils.parseDate(dateTill), channelId);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/messages/current
     * Get current messages for logged in officer
     *
     * @param classes Comma-separated class IDs (optional)
     */
    @GetMapping("/current")
    public ResponseEntity<List<Map<String, Object>>> findCurrent(
            @RequestParam(required = false) String classes) {
        List<Map<String, Object>> result = messageService.findCurrent(classes);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/messages/{wocId}/user-data
     * Load user data for a message
     *
     * @param wocId WOC ID
     * @param msgId Message ID (optional)
     */
    @GetMapping("/{wocId}/user-data")
    public ResponseEntity<Map<String, Object>> loadUserData(
            @PathVariable Long wocId,
            @RequestParam(required = false) String msgId) {
        Map<String, Object> result = messageService.loadUserData(wocId, msgId);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/messages/{wocId}/communication
     * Load communication history for a way of connection
     */
    @GetMapping("/{wocId}/communication")
    public ResponseEntity<List<Map<String, Object>>> loadCommunication(@PathVariable Long wocId) {
        List<Map<String, Object>> result = messageService.loadCommunication(wocId);
        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/messages/{id}/lock
     * Set lock on a message
     */
    @PostMapping("/{id}/lock")
    public ResponseEntity<Map<String, Object>> setLock(
            @PathVariable String id,
            @RequestBody Map<String, String> request) {

        String lockName = request.get("lockName");
        if (lockName == null || lockName.isBlank()) {
            throw new ValidationException("Missing required field: lockName");
        }

        Map<String, Object> result = messageService.setLock(lockName, id);
        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/messages/{id}/seen
     * Mark message as seen
     */
    @PostMapping("/{id}/seen")
    public ResponseEntity<Map<String, Object>> setSeen(@PathVariable Long id) {
        Map<String, Object> result = messageService.setSeen(id);
        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/messages/{id}/answer
     * Answer a message
     */
    @PostMapping("/{id}/answer")
    public ResponseEntity<Map<String, Object>> answer(
            @PathVariable Long id,
            @RequestBody Map<String, Object> request) {

        Long wocId = request.get("wocId") != null ? ((Number) request.get("wocId")).longValue() : null;
        Integer status = request.get("status") != null ? ((Number) request.get("status")).intValue() : null;
        Long classId = request.get("classId") != null ? ((Number) request.get("classId")).longValue() : null;
        String message = (String) request.get("message");

        if (wocId == null || status == null || classId == null || message == null || message.isBlank()) {
            throw new ValidationException("Missing required fields: wocId, status, classId, message");
        }

        Map<String, Object> result = messageService.answer(id, wocId, status, classId, message);
        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/messages/{id}/forward
     * Forward a message to another class
     */
    @PostMapping("/{id}/forward")
    public ResponseEntity<Map<String, Object>> forward(
            @PathVariable Long id,
            @RequestBody Map<String, Object> request) {

        Long classId = request.get("classId") != null ? ((Number) request.get("classId")).longValue() : null;
        if (classId == null) {
            throw new ValidationException("Missing required field: classId");
        }

        Map<String, Object> result = messageService.forward(id, classId);
        return ResponseEntity.ok(result);
    }
}

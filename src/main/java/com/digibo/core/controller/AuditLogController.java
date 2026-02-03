package com.digibo.core.controller;

import com.digibo.core.exception.ValidationException;
import com.digibo.core.service.AuditLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AuditLogController - REST controller for audit log operations
 * Maps to /api/auditlog endpoints
 */
@RestController
@RequestMapping("/api/auditlog")
public class AuditLogController {

    private final AuditLogService auditLogService;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    /**
     * GET /api/auditlog/search
     * Search audit log entries
     *
     * Query params:
     * - dfrom: Start date (ISO format)
     * - dto: End date (ISO format)
     * - events: Comma-separated event IDs
     * - object: Object identifier
     * - originator: Originator identifier
     * - channels: Comma-separated channel IDs
     * - limit: Maximum number of results (default: 1000)
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> search(
            @RequestParam(required = false) String dfrom,
            @RequestParam(required = false) String dto,
            @RequestParam(required = false) String events,
            @RequestParam(required = false) String object,
            @RequestParam(required = false) String originator,
            @RequestParam(required = false) String channels,
            @RequestParam(required = false, defaultValue = "1000") Integer limit) {

        Date dateFrom = parseDate(dfrom, "dfrom");
        Date dateTo = parseDate(dto, "dto");

        List<Map<String, Object>> entries = auditLogService.find(
                dateFrom, dateTo, events, object, originator, channels, limit);

        Map<String, Object> result = new HashMap<>();
        result.put("entries", entries);
        result.put("count", entries != null ? entries.size() : 0);

        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/auditlog/session/:sessionId
     * Get audit log entries for a specific session
     */
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<Map<String, Object>> getSession(@PathVariable String sessionId) {
        if (sessionId == null || sessionId.isBlank()) {
            throw new ValidationException("Session ID is required");
        }

        List<Map<String, Object>> entries = auditLogService.findSession(sessionId);

        Map<String, Object> result = new HashMap<>();
        result.put("entries", entries);
        result.put("count", entries != null ? entries.size() : 0);

        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/auditlog/tree
     * Get audit log tree structure
     */
    @GetMapping("/tree")
    public ResponseEntity<Map<String, Object>> getTree() {
        List<Map<String, Object>> treeNodes = auditLogService.getTree();

        Map<String, Object> result = new HashMap<>();
        result.put("tree", treeNodes);
        result.put("count", treeNodes != null ? treeNodes.size() : 0);

        return ResponseEntity.ok(result);
    }

    /**
     * Parse date string in ISO format (yyyy-MM-dd or yyyy-MM-ddTHH:mm:ss)
     */
    private Date parseDate(String dateString, String paramName) {
        if (dateString == null || dateString.isBlank()) {
            return null;
        }
        try {
            // Try full ISO datetime format first
            if (dateString.contains("T")) {
                synchronized (DATETIME_FORMAT) {
                    return DATETIME_FORMAT.parse(dateString);
                }
            }
            // Try date-only format
            synchronized (DATE_FORMAT) {
                return DATE_FORMAT.parse(dateString);
            }
        } catch (ParseException e) {
            throw new ValidationException(
                    "Invalid " + paramName + " date format. Use ISO format (YYYY-MM-DD or YYYY-MM-DDTHH:mm:ss)");
        }
    }
}

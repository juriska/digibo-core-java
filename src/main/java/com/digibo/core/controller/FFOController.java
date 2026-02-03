package com.digibo.core.controller;

import com.digibo.core.exception.ResourceNotFoundException;
import com.digibo.core.exception.ValidationException;
import com.digibo.core.service.FFOService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * FFOController - REST controller for FFO (Free Form Order) document operations
 * Maps to /api/ffo endpoints
 */
@RestController
@RequestMapping("/api/ffo")
public class FFOController {

    private final FFOService ffoService;

    public FFOController(FFOService ffoService) {
        this.ffoService = ffoService;
    }

    /**
     * GET /api/ffo/documents
     * Get user's FFO documents using BOFFO.find_my()
     * Returns array with 19 fields matching Oracle structure
     */
    @GetMapping("/documents")
    public ResponseEntity<List<Map<String, Object>>> findMy() {
        List<Map<String, Object>> result = ffoService.findMy();
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/ffo/documents/search
     * Search FFO documents by filters using BOFFO.find()
     *
     * Query params:
     * - custId: Customer ID
     * - custName: Customer name (partial match)
     * - userLogin: User login (partial match)
     * - officerId: Officer ID
     * - docClass: Document class
     * - subject: Subject (partial match)
     * - text: Text content (partial match)
     * - docId: Document ID
     * - channels: Comma-separated channel IDs
     * - statuses: Comma-separated status IDs
     * - createdFrom: Start date (ISO format)
     * - createdTill: End date (ISO format)
     * - assignee: Assignee officer ID
     * - categoryId: Category ID
     * - subcategoryId: Subcategory ID
     */
    @GetMapping("/documents/search")
    public ResponseEntity<List<Map<String, Object>>> search(
            @RequestParam(required = false) String custId,
            @RequestParam(required = false) String custName,
            @RequestParam(required = false) String userLogin,
            @RequestParam(required = false) Long officerId,
            @RequestParam(required = false) String docClass,
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) String text,
            @RequestParam(required = false) String docId,
            @RequestParam(required = false) String channels,
            @RequestParam(required = false) String statuses,
            @RequestParam(required = false) String createdFrom,
            @RequestParam(required = false) String createdTill,
            @RequestParam(required = false) Long assignee,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long subcategoryId) {

        Date createdFromDate = parseDate(createdFrom);
        Date createdTillDate = parseDate(createdTill);

        List<Map<String, Object>> result = ffoService.find(
                custId,
                custName,
                userLogin,
                officerId,
                docClass,
                subject,
                text,
                docId,
                channels,
                statuses,
                createdFromDate,
                createdTillDate,
                assignee,
                categoryId,
                subcategoryId
        );

        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/ffo/documents/:id/details
     * Get detailed FFO document info using BOFFO.ffo() procedure
     * Returns full document details including customer info, text, signatures
     */
    @GetMapping("/documents/{id}/details")
    public ResponseEntity<Map<String, Object>> getDetails(@PathVariable String id) {
        Map<String, Object> result = ffoService.ffo(id);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/ffo/documents/:id
     * Get specific FFO document by ID
     */
    @GetMapping("/documents/{id}")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable String id) {
        Map<String, Object> result = ffoService.getById(id);

        if (result == null) {
            throw new ResourceNotFoundException("FFO document", id);
        }

        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/ffo/categories
     * Get FFO categories using BOFFO.get_categories()
     */
    @GetMapping("/categories")
    public ResponseEntity<List<Map<String, Object>>> getCategories() {
        List<Map<String, Object>> result = ffoService.getCategories();
        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/ffo/documents/:id/categorize
     * Categorize FFO document using BOFFO.categorize()
     */
    @PostMapping("/documents/{id}/categorize")
    public ResponseEntity<Map<String, Object>> categorize(
            @PathVariable String id,
            @RequestBody Map<String, Object> request) {

        Long categoryId = getLongValue(request.get("categoryId"));
        Long subCategoryId = getLongValue(request.get("subCategoryId"));
        Long assignee = getLongValue(request.get("assignee"));

        if (categoryId == null || subCategoryId == null || assignee == null) {
            throw new ValidationException("Missing required fields: categoryId, subCategoryId, assignee");
        }

        Map<String, Object> result = ffoService.categorize(
                Long.parseLong(id),
                categoryId,
                subCategoryId,
                assignee
        );

        Boolean success = (Boolean) result.get("success");
        result.put("message", Boolean.TRUE.equals(success) ?
                "Document categorized successfully" : "Failed to categorize document");

        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/ffo/documents/:id/processing
     * Set processing status using BOFFO.set_processing()
     */
    @PostMapping("/documents/{id}/processing")
    public ResponseEntity<Map<String, Object>> setProcessing(
            @PathVariable String id,
            @RequestBody Map<String, Object> request) {

        String reason = (String) request.get("reason");
        Integer newStatus = getIntegerValue(request.get("newStatus"));
        Long messageId = getLongValue(request.get("messageId"));

        if (reason == null || newStatus == null || messageId == null) {
            throw new ValidationException("Missing required fields: reason, newStatus, messageId");
        }

        Map<String, Object> result = ffoService.setProcessing(id, reason, newStatus, messageId);

        Boolean success = (Boolean) result.get("success");
        result.put("message", Boolean.TRUE.equals(success) ?
                "Processing status updated successfully" : "Failed to update status");

        return ResponseEntity.ok(result);
    }

    private Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    private Long getLongValue(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Long) {
            return (Long) value;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return Long.parseLong(value.toString());
    }

    private Integer getIntegerValue(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return Integer.parseInt(value.toString());
    }
}

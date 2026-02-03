package com.digibo.core.controller;

import com.digibo.core.exception.ValidationException;
import com.digibo.core.service.CrontoDocService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * CrontoDocController - REST controller for Cronto document operations
 * Maps to /api/crontodoc endpoints
 */
@RestController
@RequestMapping("/api/crontodoc")
public class CrontoDocController {

    private final CrontoDocService crontoDocService;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public CrontoDocController(CrontoDocService crontoDocService) {
        this.crontoDocService = crontoDocService;
    }

    /**
     * GET /api/crontodoc/documents/search
     * Search Cronto documents using BOCRONTODOC.find()
     *
     * @param custId Customer ID
     * @param custName Customer name (partial match)
     * @param userLogin User login (partial match)
     * @param officerId Officer ID
     * @param pType Document type (comma-separated class IDs)
     * @param docId Document ID
     * @param statuses Comma-separated status IDs
     * @param createdFrom Start date (ISO format)
     * @param createdTill End date (ISO format)
     * @return List of matching documents
     */
    @GetMapping("/documents/search")
    public ResponseEntity<List<Map<String, Object>>> searchDocuments(
            @RequestParam(required = false) String custId,
            @RequestParam(required = false) String custName,
            @RequestParam(required = false) String userLogin,
            @RequestParam(required = false) Long officerId,
            @RequestParam(required = false) String pType,
            @RequestParam(required = false) String docId,
            @RequestParam(required = false) String statuses,
            @RequestParam(required = false) String createdFrom,
            @RequestParam(required = false) String createdTill) {

        Date dateFrom = parseDate(createdFrom, "createdFrom");
        Date dateTill = parseDate(createdTill, "createdTill");

        List<Map<String, Object>> result = crontoDocService.find(
                custId, custName, userLogin, officerId, pType, docId,
                statuses, dateFrom, dateTill);
        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/crontodoc/documents/{id}/set-processing
     * Mark document as being processed using BOCRONTODOC.set_processing()
     *
     * @param id Document ID
     * @return Processing result with officer ID and success message
     */
    @PostMapping("/documents/{id}/set-processing")
    public ResponseEntity<Map<String, Object>> setProcessing(@PathVariable String id) {
        int officerId = crontoDocService.setProcessing(id);

        return ResponseEntity.ok(Map.of(
                "success", officerId > 0,
                "officerId", officerId,
                "message", officerId > 0 ? "Document marked for processing" : "Failed to mark document"
        ));
    }

    /**
     * GET /api/crontodoc/documents/my
     * Get documents assigned to current officer using BOCRONTODOC.find_my()
     * Pass officerId=0 to get new unassigned documents
     *
     * @param officerId Officer ID (0 for new documents)
     * @return List of documents for the officer
     */
    @GetMapping("/documents/my")
    public ResponseEntity<List<Map<String, Object>>> getMyDocuments(
            @RequestParam(required = false, defaultValue = "0") Long officerId) {

        List<Map<String, Object>> result = crontoDocService.findMy(officerId);
        return ResponseEntity.ok(result);
    }

    /**
     * Parse date string in ISO format (yyyy-MM-dd)
     */
    private Date parseDate(String dateString, String paramName) {
        if (dateString == null || dateString.isBlank()) {
            return null;
        }
        try {
            synchronized (DATE_FORMAT) {
                return DATE_FORMAT.parse(dateString);
            }
        } catch (ParseException e) {
            throw new ValidationException(
                    "Invalid " + paramName + " date format. Use ISO format (YYYY-MM-DD)");
        }
    }
}

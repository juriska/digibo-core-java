package com.digibo.core.controller;

import com.digibo.core.exception.ValidationException;
import com.digibo.core.service.FiAccOpenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * FiAccOpenController - REST controller for FI Account Open document operations
 * Maps to /api/fiaccopen endpoints
 */
@RestController
@RequestMapping("/api/fiaccopen")
public class FiAccOpenController {

    private final FiAccOpenService fiAccOpenService;

    public FiAccOpenController(FiAccOpenService fiAccOpenService) {
        this.fiAccOpenService = fiAccOpenService;
    }

    /**
     * GET /api/fiaccopen/documents/search
     * Search FI Account Open documents using BOFiaccopen.find()
     *
     * Query params:
     * - custId: Customer ID
     * - custName: Customer name (partial match)
     * - userLogin: User login (partial match)
     * - docId: Document ID
     * - statuses: Comma-separated status IDs
     * - docClass: Document class IDs (default: 321,322)
     * - createdFrom: Start date (ISO format)
     * - createdTill: End date (ISO format)
     */
    @GetMapping("/documents/search")
    public ResponseEntity<List<Map<String, Object>>> search(
            @RequestParam(required = false) String custId,
            @RequestParam(required = false) String custName,
            @RequestParam(required = false) String userLogin,
            @RequestParam(required = false) String docId,
            @RequestParam(required = false) String statuses,
            @RequestParam(required = false, defaultValue = "321,322") String docClass,
            @RequestParam(required = false) String createdFrom,
            @RequestParam(required = false) String createdTill) {

        Date createdFromDate = parseDate(createdFrom);
        Date createdTillDate = parseDate(createdTill);

        List<Map<String, Object>> result = fiAccOpenService.find(
                custId,
                custName,
                userLogin,
                docId,
                statuses,
                docClass,
                createdFromDate,
                createdTillDate
        );

        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/fiaccopen/documents/my
     * Get documents assigned to current officer using BOFiaccopen.find_my()
     * Results depend on user's roles (RBOCUSTODYEDIT, RBO_FI_BO_EDIT)
     *
     * Query params:
     * - officerId: Officer ID (0 for new documents)
     */
    @GetMapping("/documents/my")
    public ResponseEntity<List<Map<String, Object>>> findMy(
            @RequestParam(required = false, defaultValue = "0") Long officerId) {

        List<Map<String, Object>> result = fiAccOpenService.findMy(officerId);
        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/fiaccopen/documents/:id/set-processing
     * Mark document for processing with status transition using BOFiaccopen.set_processing()
     */
    @PostMapping("/documents/{id}/set-processing")
    public ResponseEntity<Map<String, Object>> setProcessing(
            @PathVariable String id,
            @RequestBody Map<String, Object> request) {

        Integer statusIdFrom = (Integer) request.get("statusIdFrom");
        Integer statusIdTo = (Integer) request.get("statusIdTo");

        if (statusIdFrom == null || statusIdTo == null) {
            throw new ValidationException("statusIdFrom and statusIdTo are required");
        }

        Integer result = fiAccOpenService.setProcessing(id, statusIdFrom, statusIdTo);

        return ResponseEntity.ok(Map.of(
                "success", result > 0,
                "officerId", result,
                "message", result > 0 ?
                        "Document marked for processing" :
                        "Failed to mark document (status mismatch or not found)"
        ));
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
}

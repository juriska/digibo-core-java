package com.digibo.core.controller;

import com.digibo.core.exception.ValidationException;
import com.digibo.core.service.CredLimIncService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * CredLimIncController - REST controller for credit limit increase operations
 * Maps to /api/credliminc endpoints
 */
@RestController
@RequestMapping("/api/credliminc")
public class CredLimIncController {

    private final CredLimIncService credLimIncService;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public CredLimIncController(CredLimIncService credLimIncService) {
        this.credLimIncService = credLimIncService;
    }

    /**
     * GET /api/credliminc/orders/search
     * Search credit limit increase orders by filters using BOCredLimInc.find()
     *
     * @param custId Customer ID
     * @param custName Customer name (partial match)
     * @param userLogin User login (partial match)
     * @param docId Document ID
     * @param statuses Comma-separated status IDs
     * @param docClass Comma-separated document class IDs
     * @param createdFrom Start date (ISO format)
     * @param createdTill End date (ISO format)
     * @param officerId Officer ID
     * @param fromLocation Location code (e.g., LV, EE)
     * @return List of matching orders
     */
    @GetMapping("/orders/search")
    public ResponseEntity<List<Map<String, Object>>> searchOrders(
            @RequestParam(required = false) String custId,
            @RequestParam(required = false) String custName,
            @RequestParam(required = false) String userLogin,
            @RequestParam(required = false) String docId,
            @RequestParam(required = false) String statuses,
            @RequestParam(required = false) String docClass,
            @RequestParam(required = false) String createdFrom,
            @RequestParam(required = false) String createdTill,
            @RequestParam(required = false) Long officerId,
            @RequestParam(required = false) String fromLocation) {

        Date dateFrom = parseDate(createdFrom, "createdFrom");
        Date dateTill = parseDate(createdTill, "createdTill");

        List<Map<String, Object>> result = credLimIncService.find(
                custId, custName, userLogin, docId, statuses, docClass,
                dateFrom, dateTill, officerId, fromLocation);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/credliminc/orders/my
     * Get credit limit increase orders for a specific officer using BOCredLimInc.find_my()
     *
     * @param officerId Officer ID (defaults to 0 for new/unassigned orders)
     * @return List of orders for the officer
     */
    @GetMapping("/orders/my")
    public ResponseEntity<List<Map<String, Object>>> getMyOrders(
            @RequestParam(required = false, defaultValue = "0") Long officerId) {

        List<Map<String, Object>> result = credLimIncService.findMy(officerId);
        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/credliminc/orders/{id}/processing
     * Set processing status using BOCredLimInc.set_processing()
     * Assigns document to current officer and sets status to manual processing started
     *
     * @param id Document ID
     * @return Processing result with success status and message
     */
    @PostMapping("/orders/{id}/processing")
    public ResponseEntity<Map<String, Object>> setProcessing(@PathVariable String id) {
        Map<String, Object> result = credLimIncService.setProcessing(id);

        boolean success = result.containsKey("success") && Boolean.TRUE.equals(result.get("success"));
        result.put("message", success ? "Processing status updated successfully" : "Failed to update status");

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

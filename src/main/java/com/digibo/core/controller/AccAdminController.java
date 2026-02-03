package com.digibo.core.controller;

import com.digibo.core.exception.ValidationException;
import com.digibo.core.service.AccAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AccAdminController - REST controller for account administration operations
 * Maps to /api/accadmin endpoints
 */
@RestController
@RequestMapping("/api/accadmin")
public class AccAdminController {

    private final AccAdminService accAdminService;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public AccAdminController(AccAdminService accAdminService) {
        this.accAdminService = accAdminService;
    }

    /**
     * GET /api/accadmin/orders/search
     * Search account administration orders by filters
     *
     * Query params:
     * - custId: Customer ID
     * - custName: Customer name (partial match)
     * - userLogin: User login (partial match)
     * - officerId: Officer ID
     * - docId: Document ID
     * - statuses: Comma-separated status IDs
     * - docClass: Comma-separated document class IDs
     * - createdFrom: Start date (ISO format)
     * - createdTill: End date (ISO format)
     */
    @GetMapping("/orders/search")
    public ResponseEntity<Map<String, Object>> searchOrders(
            @RequestParam(required = false) String custId,
            @RequestParam(required = false) String custName,
            @RequestParam(required = false) String userLogin,
            @RequestParam(required = false) Long officerId,
            @RequestParam(required = false) String docId,
            @RequestParam(required = false) String statuses,
            @RequestParam(required = false) String docClass,
            @RequestParam(required = false) String createdFrom,
            @RequestParam(required = false) String createdTill) {

        Date dateFrom = parseDate(createdFrom, "createdFrom");
        Date dateTill = parseDate(createdTill, "createdTill");

        List<Map<String, Object>> orders = accAdminService.find(
                custId, custName, userLogin, officerId, docId,
                statuses, docClass, dateFrom, dateTill);

        Map<String, Object> result = new HashMap<>();
        result.put("orders", orders);
        result.put("count", orders != null ? orders.size() : 0);

        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/accadmin/orders/my
     * Get officer's account administration orders
     *
     * Query params:
     * - officerId: Officer ID (required)
     */
    @GetMapping("/orders/my")
    public ResponseEntity<Map<String, Object>> getMyOrders(
            @RequestParam Long officerId) {

        if (officerId == null) {
            throw new ValidationException("Missing required parameter: officerId");
        }

        List<Map<String, Object>> orders = accAdminService.findMy(officerId);

        Map<String, Object> result = new HashMap<>();
        result.put("orders", orders);
        result.put("count", orders != null ? orders.size() : 0);

        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/accadmin/orders/:id/processing
     * Set processing status for an order
     */
    @PostMapping("/orders/{id}/processing")
    public ResponseEntity<Map<String, Object>> setProcessing(@PathVariable String id) {
        int resultCode = accAdminService.setProcessing(id);

        Map<String, Object> result = new HashMap<>();
        boolean success = resultCode == 0;
        result.put("success", success);
        result.put("resultCode", resultCode);
        result.put("message", success ? "Processing status updated successfully" : "Failed to update status");

        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/accadmin/orders/:id/details
     * Get detailed account administration order info
     * Returns full order details including customer info, authorization details, signatures
     */
    @GetMapping("/orders/{id}/details")
    public ResponseEntity<Map<String, Object>> getOrderDetails(@PathVariable String id) {
        Map<String, Object> result = accAdminService.accadmin(id);
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

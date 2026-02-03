package com.digibo.core.controller;

import com.digibo.core.exception.ValidationException;
import com.digibo.core.service.CapfService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CapfController - REST controller for CAPF order operations
 * Maps to /api/capf endpoints
 */
@RestController
@RequestMapping("/api/capf")
public class CapfController {

    private final CapfService capfService;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public CapfController(CapfService capfService) {
        this.capfService = capfService;
    }

    /**
     * GET /api/capf/orders/search
     * Search CAPF orders by filters
     *
     * Query params:
     * - custId: Customer ID
     * - custName: Customer name (partial match)
     * - userLogin: User login (partial match)
     * - docId: Document ID
     * - statuses: Comma-separated status IDs
     * - docClass: Comma-separated class IDs
     * - createdFrom: Start date (ISO format)
     * - createdTill: End date (ISO format)
     * - pCustomerName: Customer name filter
     * - pLegalId: Legal ID filter
     */
    @GetMapping("/orders/search")
    public ResponseEntity<Map<String, Object>> searchOrders(
            @RequestParam(required = false) String custId,
            @RequestParam(required = false) String custName,
            @RequestParam(required = false) String userLogin,
            @RequestParam(required = false) String docId,
            @RequestParam(required = false) String statuses,
            @RequestParam(required = false) String docClass,
            @RequestParam(required = false) String createdFrom,
            @RequestParam(required = false) String createdTill,
            @RequestParam(required = false) String pCustomerName,
            @RequestParam(required = false) String pLegalId) {

        Date dateFrom = parseDate(createdFrom, "createdFrom");
        Date dateTill = parseDate(createdTill, "createdTill");

        List<Map<String, Object>> orders = capfService.find(
                custId, custName, userLogin, docId, statuses, docClass,
                dateFrom, dateTill, pCustomerName, pLegalId);

        Map<String, Object> result = new HashMap<>();
        result.put("orders", orders);
        result.put("count", orders != null ? orders.size() : 0);

        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/capf/orders/my
     * Get officer's CAPF orders
     *
     * Query params:
     * - officerId: Officer ID (default: 0 for new orders)
     */
    @GetMapping("/orders/my")
    public ResponseEntity<Map<String, Object>> getMyOrders(
            @RequestParam(required = false, defaultValue = "0") Long officerId) {

        List<Map<String, Object>> orders = capfService.findMy(officerId);

        Map<String, Object> result = new HashMap<>();
        result.put("orders", orders);
        result.put("count", orders != null ? orders.size() : 0);

        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/capf/orders/:id/details
     * Get detailed CAPF order info
     * Returns full order details including customer info, authorization, signatures
     */
    @GetMapping("/orders/{id}/details")
    public ResponseEntity<Map<String, Object>> getOrderDetails(@PathVariable String id) {
        Map<String, Object> result = capfService.capforder(id);
        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/capf/orders/:id/processing
     * Set processing status for a CAPF order
     */
    @PostMapping("/orders/{id}/processing")
    public ResponseEntity<Map<String, Object>> setProcessing(@PathVariable String id) {
        int resultCode = capfService.setProcessing(id);

        Map<String, Object> result = new HashMap<>();
        boolean success = resultCode > 0;
        result.put("success", success);
        result.put("resultCode", resultCode);
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

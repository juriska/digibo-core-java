package com.digibo.core.controller;

import com.digibo.core.exception.ValidationException;
import com.digibo.core.service.AmexOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AmexOrderController - REST controller for AMEX order operations
 * Maps to /api/amexorder endpoints
 */
@RestController
@RequestMapping("/api/amexorder")
public class AmexOrderController {

    private final AmexOrderService amexOrderService;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public AmexOrderController(AmexOrderService amexOrderService) {
        this.amexOrderService = amexOrderService;
    }

    /**
     * GET /api/amexorder/orders
     * Get user's AMEX orders
     *
     * Query params:
     * - officerId: Officer ID (default 0 for new orders)
     */
    @GetMapping("/orders")
    public ResponseEntity<Map<String, Object>> getOrders(
            @RequestParam(required = false, defaultValue = "0") Long officerId) {

        List<Map<String, Object>> orders = amexOrderService.findMy(officerId);

        Map<String, Object> result = new HashMap<>();
        result.put("orders", orders);
        result.put("count", orders != null ? orders.size() : 0);

        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/amexorder/orders/search
     * Search AMEX orders by filters
     *
     * Query params:
     * - custId: Customer ID
     * - custName: Customer name (partial match)
     * - userLogin: User login (partial match)
     * - docId: Document ID
     * - statuses: Comma-separated status IDs
     * - docClass: Comma-separated document class IDs
     * - fromLocation: Location (partial match)
     * - createdFrom: Start date (ISO format)
     * - createdTill: End date (ISO format)
     * - customerName: Customer name from extensions (partial match)
     * - legalId: Legal ID (partial match)
     * - formType: Form type (CREDIT_SCORE, CREDIT_CARD, KPP)
     */
    @GetMapping("/orders/search")
    public ResponseEntity<Map<String, Object>> searchOrders(
            @RequestParam(required = false) String custId,
            @RequestParam(required = false) String custName,
            @RequestParam(required = false) String userLogin,
            @RequestParam(required = false) String docId,
            @RequestParam(required = false) String statuses,
            @RequestParam(required = false) String docClass,
            @RequestParam(required = false) String fromLocation,
            @RequestParam(required = false) String createdFrom,
            @RequestParam(required = false) String createdTill,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String legalId,
            @RequestParam(required = false) String formType) {

        Date dateFrom = parseDate(createdFrom, "createdFrom");
        Date dateTill = parseDate(createdTill, "createdTill");

        List<Map<String, Object>> orders = amexOrderService.find(
                custId, custName, userLogin, docId, statuses, docClass,
                fromLocation, dateFrom, dateTill, customerName, legalId, formType);

        Map<String, Object> result = new HashMap<>();
        result.put("orders", orders);
        result.put("count", orders != null ? orders.size() : 0);

        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/amexorder/orders/:id/details
     * Get detailed AMEX order info
     * Returns full order details including customer info, auth person, signatures
     */
    @GetMapping("/orders/{id}/details")
    public ResponseEntity<Map<String, Object>> getOrderDetails(@PathVariable String id) {
        Map<String, Object> result = amexOrderService.amexorder(id);
        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/amexorder/orders/:id/processing
     * Set processing status for an AMEX order
     * Assigns the order to the current officer for manual processing
     */
    @PostMapping("/orders/{id}/processing")
    public ResponseEntity<Map<String, Object>> setProcessing(@PathVariable String id) {
        int resultCode = amexOrderService.setProcessing(id);

        Map<String, Object> result = new HashMap<>();
        boolean success = resultCode > 0;
        result.put("success", success);
        result.put("officerId", resultCode);
        result.put("message", success ? "Processing status updated successfully" : "Failed to update status");

        if (!success) {
            return ResponseEntity.badRequest().body(result);
        }

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

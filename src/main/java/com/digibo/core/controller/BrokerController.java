package com.digibo.core.controller;

import com.digibo.core.exception.ValidationException;
import com.digibo.core.service.BrokerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BrokerController - REST controller for broker order operations
 * Maps to /api/broker endpoints
 */
@RestController
@RequestMapping("/api/broker")
public class BrokerController {

    private final BrokerService brokerService;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public BrokerController(BrokerService brokerService) {
        this.brokerService = brokerService;
    }

    /**
     * GET /api/broker/orders/search
     * Search broker orders by filters
     *
     * Query params:
     * - custId: Customer ID
     * - custName: Customer name (partial match)
     * - userLogin: User login (partial match)
     * - userPassword: User password
     * - docClass: Comma-separated document class IDs
     * - operationType: Operation type
     * - docCount: Limit number of documents
     * - currencies: Comma-separated currency codes
     * - expiryFrom: Expiry start date (ISO format)
     * - expiryTill: Expiry end date (ISO format)
     * - docId: Document ID
     * - statuses: Comma-separated status IDs
     * - createdFrom: Start date (ISO format)
     * - createdTill: End date (ISO format)
     */
    @GetMapping("/orders/search")
    public ResponseEntity<Map<String, Object>> searchOrders(
            @RequestParam(required = false) String custId,
            @RequestParam(required = false) String custName,
            @RequestParam(required = false) String userLogin,
            @RequestParam(required = false) String userPassword,
            @RequestParam(required = false) String docClass,
            @RequestParam(required = false) String operationType,
            @RequestParam(required = false) Integer docCount,
            @RequestParam(required = false) String currencies,
            @RequestParam(required = false) String expiryFrom,
            @RequestParam(required = false) String expiryTill,
            @RequestParam(required = false) String docId,
            @RequestParam(required = false) String statuses,
            @RequestParam(required = false) String createdFrom,
            @RequestParam(required = false) String createdTill) {

        Date expiryFromDate = parseDate(expiryFrom, "expiryFrom");
        Date expiryTillDate = parseDate(expiryTill, "expiryTill");
        Date createdFromDate = parseDate(createdFrom, "createdFrom");
        Date createdTillDate = parseDate(createdTill, "createdTill");

        List<Map<String, Object>> orders = brokerService.find(
                custId, custName, userLogin, userPassword, docClass, operationType,
                docCount, currencies, expiryFromDate, expiryTillDate, docId, statuses,
                createdFromDate, createdTillDate);

        Map<String, Object> result = new HashMap<>();
        result.put("orders", orders);
        result.put("count", orders != null ? orders.size() : 0);

        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/broker/orders/my
     * Get user's broker orders
     *
     * Query params:
     * - docClass: Comma-separated document class IDs (optional)
     */
    @GetMapping("/orders/my")
    public ResponseEntity<Map<String, Object>> getMyOrders(
            @RequestParam(required = false) String docClass) {

        List<Map<String, Object>> orders = brokerService.findMy(docClass);

        Map<String, Object> result = new HashMap<>();
        result.put("orders", orders);
        result.put("count", orders != null ? orders.size() : 0);

        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/broker/orders/:id/details
     * Get detailed broker order info
     * Returns full order details including securities info, portfolio details
     */
    @GetMapping("/orders/{id}/details")
    public ResponseEntity<Map<String, Object>> getOrderDetails(@PathVariable String id) {
        Map<String, Object> result = brokerService.broker(id);
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

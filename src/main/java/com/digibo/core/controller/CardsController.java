package com.digibo.core.controller;

import com.digibo.core.exception.ValidationException;
import com.digibo.core.service.CardsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CardsController - REST controller for card order operations
 * Maps to /api/cards endpoints
 */
@RestController
@RequestMapping("/api/cards")
public class CardsController {

    private final CardsService cardsService;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public CardsController(CardsService cardsService) {
        this.cardsService = cardsService;
    }

    /**
     * GET /api/cards/orders/search
     * Search card orders by filters
     *
     * Query params:
     * - custId: Customer ID
     * - custName: Customer name (partial match)
     * - userLogin: User login (partial match)
     * - officerId: Officer ID
     * - docClass: Comma-separated document class IDs
     * - fromLocation: Location code
     * - docId: Document ID
     * - statuses: Comma-separated status IDs
     * - createdFrom: Start date (ISO format)
     * - createdTill: End date (ISO format)
     * - channels: Comma-separated channel IDs
     */
    @GetMapping("/orders/search")
    public ResponseEntity<Map<String, Object>> searchOrders(
            @RequestParam(required = false) String custId,
            @RequestParam(required = false) String custName,
            @RequestParam(required = false) String userLogin,
            @RequestParam(required = false) Long officerId,
            @RequestParam(required = false) String docClass,
            @RequestParam(required = false) String fromLocation,
            @RequestParam(required = false) String docId,
            @RequestParam(required = false) String statuses,
            @RequestParam(required = false) String createdFrom,
            @RequestParam(required = false) String createdTill,
            @RequestParam(required = false) String channels) {

        Date dateFrom = parseDate(createdFrom, "createdFrom");
        Date dateTill = parseDate(createdTill, "createdTill");

        List<Map<String, Object>> orders = cardsService.find(
                custId, custName, userLogin, officerId, docClass, fromLocation,
                docId, statuses, dateFrom, dateTill, channels);

        Map<String, Object> result = new HashMap<>();
        result.put("orders", orders);
        result.put("count", orders != null ? orders.size() : 0);

        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/cards/orders/my
     * Get officer's card orders
     *
     * Query params:
     * - officerId: Officer ID (required)
     * - docClass: Comma-separated document class IDs (optional)
     */
    @GetMapping("/orders/my")
    public ResponseEntity<Map<String, Object>> getMyOrders(
            @RequestParam Long officerId,
            @RequestParam(required = false) String docClass) {

        if (officerId == null) {
            throw new ValidationException("Missing required parameter: officerId");
        }

        List<Map<String, Object>> orders = cardsService.findMy(officerId, docClass);

        Map<String, Object> result = new HashMap<>();
        result.put("orders", orders);
        result.put("count", orders != null ? orders.size() : 0);

        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/cards/orders/:id/extensions
     * Get card order extensions
     */
    @GetMapping("/orders/{id}/extensions")
    public ResponseEntity<Map<String, Object>> getExtensions(@PathVariable String id) {
        List<Map<String, Object>> extensions = cardsService.getExtensions(id);

        Map<String, Object> result = new HashMap<>();
        result.put("extensions", extensions);
        result.put("count", extensions != null ? extensions.size() : 0);

        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/cards/orders/:id/processing
     * Set processing status for a card order
     *
     * Body: {
     *   statusIdFrom: number (required)
     * }
     */
    @PostMapping("/orders/{id}/processing")
    public ResponseEntity<Map<String, Object>> setProcessing(
            @PathVariable String id,
            @RequestBody Map<String, Object> request) {

        Object statusIdFromObj = request.get("statusIdFrom");
        if (statusIdFromObj == null) {
            throw new ValidationException("Missing required field: statusIdFrom");
        }

        Integer statusIdFrom;
        if (statusIdFromObj instanceof Integer) {
            statusIdFrom = (Integer) statusIdFromObj;
        } else if (statusIdFromObj instanceof Number) {
            statusIdFrom = ((Number) statusIdFromObj).intValue();
        } else {
            throw new ValidationException("Invalid statusIdFrom: must be a number");
        }

        int resultCode = cardsService.setProcessing(id, statusIdFrom);

        Map<String, Object> result = new HashMap<>();
        boolean success = resultCode == 0;
        result.put("success", success);
        result.put("resultCode", resultCode);
        result.put("message", success ? "Processing status updated successfully" : "Failed to update status");

        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/cards/orders/:id/details
     * Get detailed card order info
     * Returns full order details including customer info, card details, signatures
     */
    @GetMapping("/orders/{id}/details")
    public ResponseEntity<Map<String, Object>> getOrderDetails(@PathVariable String id) {
        Map<String, Object> result = cardsService.card(id);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/cards/orders/:id/lost-address
     * Get lost address information
     */
    @GetMapping("/orders/{id}/lost-address")
    public ResponseEntity<Map<String, Object>> getLostAddress(@PathVariable String id) {
        Map<String, Object> result = cardsService.getLostAddr(id);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/cards/orders/:id/issue-address
     * Get issue address information
     */
    @GetMapping("/orders/{id}/issue-address")
    public ResponseEntity<Map<String, Object>> getIssueAddress(@PathVariable String id) {
        Map<String, Object> result = cardsService.getIssueAddr(id);
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

package com.digibo.core.controller;

import com.digibo.core.exception.ValidationException;
import com.digibo.core.service.CustodyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * CustodyController - REST controller for custody order operations
 * Maps to /api/custody endpoints
 */
@RestController
@RequestMapping("/api/custody")
public class CustodyController {

    private final CustodyService custodyService;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public CustodyController(CustodyService custodyService) {
        this.custodyService = custodyService;
    }

    /**
     * GET /api/custody/orders/search
     * Search custody orders by filters using BOCustody.find()
     *
     * @param custId Customer ID
     * @param custName Customer name (partial match)
     * @param userLogin User login (partial match)
     * @param docId Document ID
     * @param statuses Comma-separated status IDs
     * @param docClass Document class
     * @param createdFrom Start date (ISO format)
     * @param createdTill End date (ISO format)
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
            @RequestParam(required = false) String createdTill) {

        Date dateFrom = parseDate(createdFrom, "createdFrom");
        Date dateTill = parseDate(createdTill, "createdTill");

        List<Map<String, Object>> result = custodyService.find(
                custId, custName, userLogin, docId, statuses, docClass,
                dateFrom, dateTill);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/custody/orders/my
     * Get user's custody orders using BOCustody.findMy()
     *
     * @param officerId Officer ID (required)
     * @return List of orders for the officer
     */
    @GetMapping("/orders/my")
    public ResponseEntity<List<Map<String, Object>>> getMyOrders(
            @RequestParam String officerId) {

        if (officerId == null || officerId.isBlank()) {
            throw new ValidationException("Missing required parameter: officerId");
        }

        List<Map<String, Object>> result = custodyService.findMy(officerId);
        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/custody/orders/{id}/processing
     * Set processing status using BOCustody.setProcessing()
     *
     * @param id Document ID
     * @return Processing result with success status and message
     */
    @PostMapping("/orders/{id}/processing")
    public ResponseEntity<Map<String, Object>> setProcessing(@PathVariable String id) {
        Map<String, Object> result = custodyService.setProcessing(id);

        boolean success = result.containsKey("success") && Boolean.TRUE.equals(result.get("success"));
        result.put("message", success ? "Processing status updated successfully" : "Failed to update status");

        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/custody/orders/{id}/details
     * Get detailed custody order info using BOCustody.custody() procedure
     *
     * @param id Document ID
     * @return Full order details including customer info, authorization details, signatures
     */
    @GetMapping("/orders/{id}/details")
    public ResponseEntity<Map<String, Object>> getOrderDetails(@PathVariable String id) {
        Map<String, Object> result = custodyService.custody(id);
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

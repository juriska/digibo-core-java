package com.digibo.core.controller;

import com.digibo.core.exception.ValidationException;
import com.digibo.core.service.LeaseWebService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * LeaseWebController - REST controller for lease web operations
 * Maps to /api/leaseweb endpoints
 */
@RestController
@RequestMapping("/api/leaseweb")
public class LeaseWebController {

    private final LeaseWebService leaseWebService;

    public LeaseWebController(LeaseWebService leaseWebService) {
        this.leaseWebService = leaseWebService;
    }

    /**
     * GET /api/leaseweb/orders/search
     * Search lease web orders by filters
     *
     * @param custId Customer ID
     * @param custName Customer name (partial match)
     * @param userLogin User login (partial match)
     * @param docId Document ID
     * @param statuses Comma-separated status IDs
     * @param docClass Comma-separated class IDs
     * @param createdFrom Start date (ISO format)
     * @param createdTill End date (ISO format)
     * @param pCustomerName Customer name filter
     * @param pLegalId Legal ID filter
     */
    @GetMapping("/orders/search")
    public ResponseEntity<List<Map<String, Object>>> find(
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

        Date createdFromDate = parseDate(createdFrom);
        Date createdTillDate = parseDate(createdTill);

        List<Map<String, Object>> result = leaseWebService.find(
                custId, custName, userLogin, docId, statuses, docClass,
                createdFromDate, createdTillDate, pCustomerName, pLegalId);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/leaseweb/orders/my
     * Get officer's lease web orders
     *
     * @param officerId Officer ID (default: 0 for new orders)
     */
    @GetMapping("/orders/my")
    public ResponseEntity<List<Map<String, Object>>> findMy(
            @RequestParam(required = false, defaultValue = "0") Long officerId) {
        List<Map<String, Object>> result = leaseWebService.findMy(officerId);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/leaseweb/orders/{id}/details
     * Get detailed lease web order info
     */
    @GetMapping("/orders/{id}/details")
    public ResponseEntity<Map<String, Object>> amexorder(@PathVariable String id) {
        Map<String, Object> result = leaseWebService.amexorder(id);
        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/leaseweb/orders/{id}/processing
     * Set processing status
     */
    @PostMapping("/orders/{id}/processing")
    public ResponseEntity<Map<String, Object>> setProcessing(
            @PathVariable String id,
            @RequestBody Map<String, Object> request) {

        Long statusIdFrom = getLongValue(request.get("statusIdFrom"));
        if (statusIdFrom == null) {
            throw new ValidationException("Missing required field: statusIdFrom");
        }

        Map<String, Object> result = leaseWebService.setProcessing(id, statusIdFrom);

        boolean success = result.get("success") != null && (Boolean) result.get("success");
        return ResponseEntity.ok(Map.of(
                "success", success,
                "docId", id,
                "message", success ? "Processing status updated successfully" : "Failed to update status",
                "data", result
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
}

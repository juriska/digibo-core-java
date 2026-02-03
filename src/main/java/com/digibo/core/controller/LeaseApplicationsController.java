package com.digibo.core.controller;

import com.digibo.core.service.LeaseApplicationsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * LeaseApplicationsController - REST controller for lease application operations
 * Maps to /api/leaseapplications endpoints
 */
@RestController
@RequestMapping("/api/leaseapplications")
public class LeaseApplicationsController {

    private final LeaseApplicationsService leaseApplicationsService;

    public LeaseApplicationsController(LeaseApplicationsService leaseApplicationsService) {
        this.leaseApplicationsService = leaseApplicationsService;
    }

    /**
     * GET /api/leaseapplications/orders/search
     * Search lease application orders by filters
     *
     * @param custId Customer ID
     * @param custName Customer name (partial match)
     * @param userLogin User login (partial match)
     * @param docId Document ID
     * @param statuses Comma-separated status IDs
     * @param docClass Comma-separated document class IDs
     * @param createdFrom Start date (ISO format)
     * @param createdTill End date (ISO format)
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
            @RequestParam(required = false) String createdTill) {

        Date createdFromDate = parseDate(createdFrom);
        Date createdTillDate = parseDate(createdTill);

        List<Map<String, Object>> result = leaseApplicationsService.find(
                custId, custName, userLogin, docId, statuses, docClass, createdFromDate, createdTillDate);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/leaseapplications/orders/my
     * Get lease application orders for a specific officer
     *
     * @param officerId Officer ID (defaults to 0 for new/unassigned orders)
     */
    @GetMapping("/orders/my")
    public ResponseEntity<List<Map<String, Object>>> findMy(
            @RequestParam(required = false, defaultValue = "0") Long officerId) {
        List<Map<String, Object>> result = leaseApplicationsService.findMy(officerId);
        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/leaseapplications/orders/{id}/processing
     * Set processing status - assigns document to current officer
     */
    @PostMapping("/orders/{id}/processing")
    public ResponseEntity<Map<String, Object>> setProcessing(
            @PathVariable String id,
            @RequestBody(required = false) Map<String, Object> request) {

        Long statusIdFrom = request != null ? getLongValue(request.get("statusIdFrom")) : null;
        Map<String, Object> result = leaseApplicationsService.setProcessing(id, statusIdFrom);

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

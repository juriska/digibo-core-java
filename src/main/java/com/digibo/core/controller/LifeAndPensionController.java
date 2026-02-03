package com.digibo.core.controller;

import com.digibo.core.service.LifeAndPensionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * LifeAndPensionController - REST controller for life and pension operations
 * Maps to /api/lifeandpension endpoints
 */
@RestController
@RequestMapping("/api/lifeandpension")
public class LifeAndPensionController {

    private final LifeAndPensionService lifeAndPensionService;

    public LifeAndPensionController(LifeAndPensionService lifeAndPensionService) {
        this.lifeAndPensionService = lifeAndPensionService;
    }

    /**
     * GET /api/lifeandpension/orders/search
     * Search life and pension orders by filters
     *
     * @param custId Customer ID
     * @param custName Customer name (partial match)
     * @param userLogin User login (partial match)
     * @param docId Document ID
     * @param statuses Comma-separated status IDs
     * @param docClass Comma-separated class IDs
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

        List<Map<String, Object>> result = lifeAndPensionService.find(
                custId, custName, userLogin, docId, statuses, docClass, createdFromDate, createdTillDate);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/lifeandpension/orders/my
     * Get officer's life and pension orders
     *
     * @param officerId Officer ID (default: 0 for new orders)
     */
    @GetMapping("/orders/my")
    public ResponseEntity<List<Map<String, Object>>> findMy(
            @RequestParam(required = false, defaultValue = "0") Long officerId) {
        List<Map<String, Object>> result = lifeAndPensionService.findMy(officerId);
        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/lifeandpension/orders/{id}/processing
     * Set processing status
     */
    @PostMapping("/orders/{id}/processing")
    public ResponseEntity<Map<String, Object>> setProcessing(@PathVariable String id) {
        Map<String, Object> result = lifeAndPensionService.setProcessing(id);

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
}

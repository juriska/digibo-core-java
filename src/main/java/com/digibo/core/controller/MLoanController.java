package com.digibo.core.controller;

import com.digibo.core.service.MLoanService;
import com.digibo.core.util.DateUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * MLoanController - REST controller for mortgage loan operations
 * Maps to /api/mloan endpoints
 */
@RestController
@RequestMapping("/api/mloan")
public class MLoanController {

    private final MLoanService mloanService;

    public MLoanController(MLoanService mloanService) {
        this.mloanService = mloanService;
    }

    /**
     * GET /api/mloan/orders/search
     * Search mortgage loan orders by filters
     *
     * @param custId Customer ID
     * @param custName Customer name (partial match)
     * @param userLogin User login (partial match)
     * @param docId Document ID
     * @param statuses Comma-separated status IDs
     * @param createdFrom Start date (ISO format)
     * @param createdTill End date (ISO format)
     * @param docClass Comma-separated document class IDs
     * @param fromLocation Location code (e.g., LV, EE)
     */
    @GetMapping("/orders/search")
    public ResponseEntity<List<Map<String, Object>>> find(
            @RequestParam(required = false) String custId,
            @RequestParam(required = false) String custName,
            @RequestParam(required = false) String userLogin,
            @RequestParam(required = false) String docId,
            @RequestParam(required = false) String statuses,
            @RequestParam(required = false) String createdFrom,
            @RequestParam(required = false) String createdTill,
            @RequestParam(required = false) String docClass,
            @RequestParam(required = false) String fromLocation) {

        List<Map<String, Object>> result = mloanService.find(
                custId, custName, userLogin, docId, statuses,
                DateUtils.parseDate(createdFrom), DateUtils.parseDate(createdTill), docClass, fromLocation);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/mloan/orders/my
     * Get mortgage loan orders for a specific officer
     *
     * @param officerId Officer ID (defaults to 0 for new/unassigned orders)
     */
    @GetMapping("/orders/my")
    public ResponseEntity<List<Map<String, Object>>> findMy(
            @RequestParam(required = false, defaultValue = "0") Long officerId) {
        List<Map<String, Object>> result = mloanService.findMy(officerId);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/mloan/orders/{id}/details
     * Get detailed mortgage loan info
     */
    @GetMapping("/orders/{id}/details")
    public ResponseEntity<Map<String, Object>> mloan(@PathVariable String id) {
        Map<String, Object> result = mloanService.mloan(id);
        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/mloan/orders/{id}/processing
     * Set processing status - assigns document to current officer
     */
    @PostMapping("/orders/{id}/processing")
    public ResponseEntity<Map<String, Object>> setProcessing(@PathVariable String id) {
        Map<String, Object> result = mloanService.setProcessing(id);

        boolean success = result.get("success") != null && (Boolean) result.get("success");
        return ResponseEntity.ok(Map.of(
                "success", success,
                "docId", id,
                "message", success ? "Processing status updated successfully" : "Failed to update status",
                "data", result
        ));
    }
}

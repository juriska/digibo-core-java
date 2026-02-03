package com.digibo.core.controller;

import com.digibo.core.service.ProdKitService;
import com.digibo.core.util.DateUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * ProdKitController - REST controller for ProdKit custody operations
 * Maps to /api/prodkit endpoints
 */
@RestController
@RequestMapping("/api/prodkit")
public class ProdKitController {

    private final ProdKitService prodKitService;

    public ProdKitController(ProdKitService prodKitService) {
        this.prodKitService = prodKitService;
    }

    /**
     * GET /api/prodkit/orders/search
     * Search custody orders by filters using BOProdKit.find()
     *
     * Query params:
     * - custId: Customer ID
     * - custName: Customer name (partial match)
     * - userLogin: User login (partial match)
     * - docId: Document ID
     * - statuses: Comma-separated status IDs
     * - docClass: Comma-separated document class IDs
     * - createdFrom: Start date (ISO format)
     * - createdTill: End date (ISO format)
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
        List<Map<String, Object>> result = prodKitService.find(
                custId, custName, userLogin, docId, statuses, docClass,
                DateUtils.parseDate(createdFrom), DateUtils.parseDate(createdTill));
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/prodkit/orders/my
     * Get custody orders for current officer using BOProdKit.find_my()
     *
     * Query params:
     * - officerId: Officer ID (0 for new orders, default: 0)
     */
    @GetMapping("/orders/my")
    public ResponseEntity<List<Map<String, Object>>> findMyOrders(
            @RequestParam(required = false, defaultValue = "0") Integer officerId) {
        List<Map<String, Object>> result = prodKitService.findMy(officerId);
        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/prodkit/orders/:id/processing
     * Set processing status using BOProdKit.set_processing()
     *
     * Returns the change_officer_id if successful
     */
    @PostMapping("/orders/{id}/processing")
    public ResponseEntity<Map<String, Object>> setProcessing(@PathVariable String id) {
        Map<String, Object> result = prodKitService.setProcessing(id);
        Boolean success = (Boolean) result.get("success");
        result.put("message", Boolean.TRUE.equals(success) ? "Processing status updated successfully" : "Failed to update status");
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/prodkit/orders/:id/details
     * Get detailed custody order info using BOProdKit.prodkit() procedure
     * Returns full document details including customer info, signatures, authorized person data
     */
    @GetMapping("/orders/{id}/details")
    public ResponseEntity<Map<String, Object>> getOrderDetails(@PathVariable String id) {
        Map<String, Object> result = prodKitService.prodkit(id);
        return ResponseEntity.ok(result);
    }
}

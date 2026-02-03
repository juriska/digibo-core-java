package com.digibo.core.controller;

import com.digibo.core.exception.ResourceNotFoundException;
import com.digibo.core.exception.ValidationException;
import com.digibo.core.service.OTSEService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * OTSEController - REST controller for OTSE operations
 * Maps to /api/otse endpoints
 */
@RestController
@RequestMapping("/api/otse")
public class OTSEController {

    private final OTSEService otseService;

    public OTSEController(OTSEService otseService) {
        this.otseService = otseService;
    }

    /**
     * GET /api/otse/orders/search
     * Search OTSE documents by filters using BOOTSE.find()
     *
     * Query params:
     * - custId: Customer ID
     * - custName: Customer name (partial match)
     * - userLogin: User login (partial match)
     * - personalId: Personal ID
     * - docId: Document ID
     */
    @GetMapping("/orders/search")
    public ResponseEntity<List<Map<String, Object>>> searchOrders(
            @RequestParam(required = false) String custId,
            @RequestParam(required = false) String custName,
            @RequestParam(required = false) String userLogin,
            @RequestParam(required = false) String personalId,
            @RequestParam(required = false) String docId) {
        List<Map<String, Object>> result = otseService.find(custId, custName, userLogin, personalId, docId);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/otse/orders/new
     * Get new OTSE documents using BOOTSE.find_new()
     */
    @GetMapping("/orders/new")
    public ResponseEntity<List<Map<String, Object>>> findNewOrders() {
        List<Map<String, Object>> result = otseService.findNew();
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/otse/customers/:id
     * Get customer information using BOOTSE.get_customer()
     */
    @GetMapping("/customers/{id}")
    public ResponseEntity<Map<String, Object>> getCustomer(@PathVariable String id) {
        Map<String, Object> result = otseService.getCustomer(id);

        Integer resultCode = (Integer) result.get("resultCode");
        if (resultCode != null && resultCode != 0) {
            throw new ResourceNotFoundException("Customer not found or replication failed. Result code: " + resultCode);
        }

        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/otse/bind
     * Bind customer to WOC using BOOTSE.bind()
     *
     * Body: {
     *   wocId: string,
     *   custId: string,
     *   userId: string,
     *   docId: string
     * }
     */
    @PostMapping("/bind")
    public ResponseEntity<Map<String, Object>> bindCustomer(@RequestBody Map<String, String> request) {
        String wocId = request.get("wocId");
        String custId = request.get("custId");
        String userId = request.get("userId");
        String docId = request.get("docId");

        if (wocId == null || custId == null || userId == null || docId == null) {
            throw new ValidationException("Missing required fields: wocId, custId, userId, docId");
        }

        Map<String, Object> result = otseService.bind(wocId, custId, userId, docId);
        Boolean success = (Boolean) result.get("success");
        result.put("message", Boolean.TRUE.equals(success) ? "Customer bound successfully" : "Failed to bind customer");
        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/otse/woc/:wocId/status
     * Set WOC status using BOOTSE.set_woc_status()
     *
     * Body: {
     *   status: number,
     *   subStatus: number
     * }
     */
    @PostMapping("/woc/{wocId}/status")
    public ResponseEntity<Map<String, Object>> setWocStatus(
            @PathVariable String wocId,
            @RequestBody Map<String, Object> request) {
        Integer status = (Integer) request.get("status");
        Integer subStatus = (Integer) request.get("subStatus");

        if (status == null || subStatus == null) {
            throw new ValidationException("Missing required fields: status, subStatus");
        }

        Map<String, Object> result = otseService.setWocStatus(wocId, status, subStatus);
        Boolean success = (Boolean) result.get("success");
        result.put("message", Boolean.TRUE.equals(success) ? "WOC status updated successfully" : "Failed to update WOC status");
        return ResponseEntity.ok(result);
    }
}

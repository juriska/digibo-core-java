package com.digibo.core.controller;

import com.digibo.core.service.PamoService;
import com.digibo.core.util.DateUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * PamoController - REST controller for PAMO operations
 * Maps to /api/pamo endpoints
 */
@RestController
@RequestMapping("/api/pamo")
public class PamoController {

    private final PamoService pamoService;

    public PamoController(PamoService pamoService) {
        this.pamoService = pamoService;
    }

    /**
     * GET /api/pamo/orders/search
     * Search PAMO documents using BOPAMO.find()
     *
     * Query params:
     * - custId: Customer ID
     * - custName: Customer name (partial match)
     * - userLogin: User login (partial match)
     * - docClass: Document class (comma-separated)
     * - pIsin: ISIN code (partial match)
     * - docId: Document ID
     * - statuses: Comma-separated status IDs
     * - createdFrom: Start date (ISO format)
     * - createdTill: End date (ISO format)
     */
    @GetMapping("/orders/search")
    public ResponseEntity<List<Map<String, Object>>> searchOrders(
            @RequestParam(required = false) String custId,
            @RequestParam(required = false) String custName,
            @RequestParam(required = false) String userLogin,
            @RequestParam(required = false) String docClass,
            @RequestParam(required = false) String pIsin,
            @RequestParam(required = false) String docId,
            @RequestParam(required = false) String statuses,
            @RequestParam(required = false) String createdFrom,
            @RequestParam(required = false) String createdTill) {
        List<Map<String, Object>> result = pamoService.find(
                custId, custName, userLogin, docClass, pIsin, docId, statuses,
                DateUtils.parseDate(createdFrom), DateUtils.parseDate(createdTill));
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/pamo/orders/my
     * Get user's PAMO documents using BOPAMO.find_my()
     *
     * Query params:
     * - docClass: Document class (comma-separated, defaults to '50')
     */
    @GetMapping("/orders/my")
    public ResponseEntity<List<Map<String, Object>>> findMyOrders(
            @RequestParam(required = false, defaultValue = "50") String docClass) {
        List<Map<String, Object>> result = pamoService.findMy(docClass);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/pamo/orders/:id/details
     * Get detailed PAMO order info using BOPAMO.pamo() procedure
     * Returns full order details including portfolio, fund, and operation info
     */
    @GetMapping("/orders/{id}/details")
    public ResponseEntity<Map<String, Object>> getOrderDetails(@PathVariable String id) {
        Map<String, Object> result = pamoService.pamo(id);
        return ResponseEntity.ok(result);
    }
}

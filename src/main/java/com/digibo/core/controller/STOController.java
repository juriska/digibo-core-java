package com.digibo.core.controller;

import com.digibo.core.service.STOService;
import com.digibo.core.util.DateUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * STOController - REST controller for Standing Order (STO) operations
 * Maps to /api/sto endpoints
 */
@RestController
@RequestMapping("/api/sto")
public class STOController {

    private final STOService stoService;

    public STOController(STOService stoService) {
        this.stoService = stoService;
    }

    /**
     * GET /api/sto/orders/search
     * Search STO documents using BOSTO.find()
     *
     * Query params:
     * - custId: Customer ID
     * - custName: Customer name (partial match)
     * - userLogin: User login (partial match)
     * - officerId: Officer ID
     * - pType: Payment type (comma-separated)
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
            @RequestParam(required = false) Long officerId,
            @RequestParam(required = false) String pType,
            @RequestParam(required = false) String docId,
            @RequestParam(required = false) String statuses,
            @RequestParam(required = false) String createdFrom,
            @RequestParam(required = false) String createdTill) {

        List<Map<String, Object>> result = stoService.find(
                custId, custName, userLogin, officerId,
                pType, docId, statuses,
                DateUtils.parseDate(createdFrom), DateUtils.parseDate(createdTill));
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/sto/orders/:id/details
     * Get detailed STO order info using BOSTO.sto() procedure
     * Returns full standing order details including beneficiary, schedule, and amounts
     */
    @GetMapping("/orders/{id}/details")
    public ResponseEntity<Map<String, Object>> getOrderDetails(@PathVariable String id) {
        Map<String, Object> result = stoService.sto(id);
        return ResponseEntity.ok(result);
    }
}

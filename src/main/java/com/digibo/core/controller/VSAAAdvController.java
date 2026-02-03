package com.digibo.core.controller;

import com.digibo.core.service.VSAAAdvService;
import com.digibo.core.util.DateUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * VSAAAdvController - REST controller for VSAA Advisory document operations
 * Maps to /api/vsaaadv endpoints
 */
@RestController
@RequestMapping("/api/vsaaadv")
public class VSAAAdvController {

    private final VSAAAdvService vsaaAdvService;

    public VSAAAdvController(VSAAAdvService vsaaAdvService) {
        this.vsaaAdvService = vsaaAdvService;
    }

    /**
     * GET /api/vsaaadv/search
     * Search VSAA Advisory documents by filters using BOVsaaAdv.find()
     *
     * Query params:
     * - custId: Customer ID
     * - custName: Customer name (partial match)
     * - docId: Document ID
     * - statuses: Comma-separated status IDs
     * - createdFrom: Start date (ISO format)
     * - createdTill: End date (ISO format)
     */
    @GetMapping("/search")
    public ResponseEntity<List<Map<String, Object>>> search(
            @RequestParam(required = false) String custId,
            @RequestParam(required = false) String custName,
            @RequestParam(required = false) String docId,
            @RequestParam(required = false) String statuses,
            @RequestParam(required = false) String createdFrom,
            @RequestParam(required = false) String createdTill) {

        List<Map<String, Object>> result = vsaaAdvService.find(
                custId, custName, docId, statuses,
                DateUtils.parseDate(createdFrom), DateUtils.parseDate(createdTill));
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/vsaaadv/:id/details
     * Get detailed VSAA Advisory document info using BOVsaaAdv.adv() procedure
     * Returns advisory document details including officer and sent dates
     */
    @GetMapping("/{id}/details")
    public ResponseEntity<Map<String, Object>> getDetails(@PathVariable String id) {
        Map<String, Object> result = vsaaAdvService.adv(id);
        return ResponseEntity.ok(result);
    }
}

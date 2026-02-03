package com.digibo.core.controller;

import com.digibo.core.service.VSAAService;
import com.digibo.core.util.DateUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * VSAAController - REST controller for VSAA document operations
 * Maps to /api/vsaa endpoints
 */
@RestController
@RequestMapping("/api/vsaa")
public class VSAAController {

    private final VSAAService vsaaService;

    public VSAAController(VSAAService vsaaService) {
        this.vsaaService = vsaaService;
    }

    /**
     * GET /api/vsaa/search
     * Search VSAA documents by filters using BOVSAA.find()
     *
     * Query params:
     * - userName: User name (partial match)
     * - legalId: Legal ID
     * - officerId: Officer ID
     * - docId: Document ID
     * - statuses: Comma-separated status IDs
     * - createdFrom: Start date (ISO format)
     * - createdTill: End date (ISO format)
     */
    @GetMapping("/search")
    public ResponseEntity<List<Map<String, Object>>> search(
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String legalId,
            @RequestParam(required = false) Long officerId,
            @RequestParam(required = false) String docId,
            @RequestParam(required = false) String statuses,
            @RequestParam(required = false) String createdFrom,
            @RequestParam(required = false) String createdTill) {

        List<Map<String, Object>> result = vsaaService.find(
                userName, legalId, officerId,
                docId, statuses,
                DateUtils.parseDate(createdFrom), DateUtils.parseDate(createdTill));
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/vsaa/:id/details
     * Get detailed VSAA document info using BOVSAA.vsaa() procedure
     * Returns full document details including personal info, address, contact details
     */
    @GetMapping("/{id}/details")
    public ResponseEntity<Map<String, Object>> getDetails(@PathVariable String id) {
        Map<String, Object> result = vsaaService.vsaa(id);
        return ResponseEntity.ok(result);
    }
}

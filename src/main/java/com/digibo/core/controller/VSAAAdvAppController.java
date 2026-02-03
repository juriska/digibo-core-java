package com.digibo.core.controller;

import com.digibo.core.service.VSAAAdvAppService;
import com.digibo.core.util.DateUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * VSAAAdvAppController - REST controller for VSAA Advisory Application document operations
 * Maps to /api/vsaaadvapp endpoints
 */
@RestController
@RequestMapping("/api/vsaaadvapp")
public class VSAAAdvAppController {

    private final VSAAAdvAppService vsaaAdvAppService;

    public VSAAAdvAppController(VSAAAdvAppService vsaaAdvAppService) {
        this.vsaaAdvAppService = vsaaAdvAppService;
    }

    /**
     * GET /api/vsaaadvapp/search
     * Search VSAA Advisory Application documents by filters using BOVsaaAdvApp.find()
     *
     * Query params:
     * - custId: Customer ID
     * - custName: Customer name (partial match)
     * - userLogin: User login (partial match)
     * - docClass: Document class
     * - legalId: Legal ID
     * - docId: Document ID
     * - statuses: Comma-separated status IDs
     * - createdFrom: Start date (ISO format)
     * - createdTill: End date (ISO format)
     */
    @GetMapping("/search")
    public ResponseEntity<List<Map<String, Object>>> search(
            @RequestParam(required = false) String custId,
            @RequestParam(required = false) String custName,
            @RequestParam(required = false) String userLogin,
            @RequestParam(required = false) String docClass,
            @RequestParam(required = false) String legalId,
            @RequestParam(required = false) String docId,
            @RequestParam(required = false) String statuses,
            @RequestParam(required = false) String createdFrom,
            @RequestParam(required = false) String createdTill) {

        List<Map<String, Object>> result = vsaaAdvAppService.find(
                custId, custName, userLogin, docClass,
                legalId, docId, statuses,
                DateUtils.parseDate(createdFrom), DateUtils.parseDate(createdTill));
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/vsaaadvapp/:id/details
     * Get detailed VSAA Advisory Application document info using BOVsaaAdvApp.advapp() procedure
     * Returns application details including user, officer, and customer information
     */
    @GetMapping("/{id}/details")
    public ResponseEntity<Map<String, Object>> getDetails(@PathVariable String id) {
        Map<String, Object> result = vsaaAdvAppService.advapp(id);
        return ResponseEntity.ok(result);
    }
}

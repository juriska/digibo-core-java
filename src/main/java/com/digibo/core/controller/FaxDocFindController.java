package com.digibo.core.controller;

import com.digibo.core.service.FaxDocFindService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * FaxDocFindController - REST controller for searching fax documents
 * Maps to /api/faxdocfind endpoints
 */
@RestController
@RequestMapping("/api/faxdocfind")
public class FaxDocFindController {

    private final FaxDocFindService faxDocFindService;

    public FaxDocFindController(FaxDocFindService faxDocFindService) {
        this.faxDocFindService = faxDocFindService;
    }

    /**
     * GET /api/faxdocfind/search
     * Search fax documents using BOFaxDocFind.find()
     *
     * Query params:
     * - faxId: Fax ID
     * - fromFax: From fax number (partial match)
     * - fromCSid: From CSID (partial match)
     * - docId: Document ID
     * - custId: Customer ID
     * - fromAccount: From account number (partial match)
     * - amountFrom: Amount from
     * - amountTo: Amount to
     * - docCcy: Document currency code (e.g., EUR, USD)
     * - officerId: Officer ID
     * - docClass: Document class ID
     * - classes: Comma-separated class IDs (e.g., "1,2,3")
     * - statuses: Comma-separated status IDs (e.g., "1,2,3")
     * - partner: Partner name (partial match)
     * - subj: Subject (partial match)
     * - recvTimeFrom: Receive time from (Unix timestamp)
     * - recvTimeTo: Receive time to (Unix timestamp)
     */
    @GetMapping("/search")
    public ResponseEntity<List<Map<String, Object>>> search(
            @RequestParam(required = false) String faxId,
            @RequestParam(required = false) String fromFax,
            @RequestParam(required = false) String fromCSid,
            @RequestParam(required = false) String docId,
            @RequestParam(required = false) String custId,
            @RequestParam(required = false) String fromAccount,
            @RequestParam(required = false) String amountFrom,
            @RequestParam(required = false) String amountTo,
            @RequestParam(required = false) String docCcy,
            @RequestParam(required = false) Long officerId,
            @RequestParam(required = false) Integer docClass,
            @RequestParam(required = false) String classes,
            @RequestParam(required = false) String statuses,
            @RequestParam(required = false) String partner,
            @RequestParam(required = false) String subj,
            @RequestParam(required = false) Long recvTimeFrom,
            @RequestParam(required = false) Long recvTimeTo) {

        List<Map<String, Object>> result = faxDocFindService.find(
                faxId,
                fromFax,
                fromCSid,
                docId,
                custId,
                fromAccount,
                amountFrom,
                amountTo,
                docCcy,
                officerId,
                docClass,
                classes,
                statuses,
                partner,
                subj,
                recvTimeFrom,
                recvTimeTo
        );

        return ResponseEntity.ok(result);
    }
}

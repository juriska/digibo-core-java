package com.digibo.core.controller;

import com.digibo.core.service.FaxFindService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * FaxFindController - REST controller for searching fax records
 * Maps to /api/faxfind endpoints
 */
@RestController
@RequestMapping("/api/faxfind")
public class FaxFindController {

    private final FaxFindService faxFindService;

    public FaxFindController(FaxFindService faxFindService) {
        this.faxFindService = faxFindService;
    }

    /**
     * GET /api/faxfind/search
     * Search fax documents by filters using BOFaxFind.find()
     *
     * Query params:
     * - faxId: Fax ID
     * - fromFax: From fax number (partial match)
     * - fromCSid: From CSID (partial match)
     * - faxStatus: Fax status (0 = all, 1 = new, 2 = divided)
     * - recvTimeFrom: Receive time from (Unix timestamp)
     * - recvTimeTo: Receive time to (Unix timestamp)
     */
    @GetMapping("/search")
    public ResponseEntity<List<Map<String, Object>>> search(
            @RequestParam(required = false) String faxId,
            @RequestParam(required = false) String fromFax,
            @RequestParam(required = false) String fromCSid,
            @RequestParam(required = false, defaultValue = "0") Integer faxStatus,
            @RequestParam(required = false, defaultValue = "0") Long recvTimeFrom,
            @RequestParam(required = false, defaultValue = "0") Long recvTimeTo) {

        List<Map<String, Object>> result = faxFindService.find(
                faxId,
                fromFax,
                fromCSid,
                faxStatus,
                recvTimeFrom,
                recvTimeTo
        );

        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/faxfind/new
     * Get new fax documents using BOFaxFind.find_new()
     * Returns faxes with status = 1 (New)
     */
    @GetMapping("/new")
    public ResponseEntity<List<Map<String, Object>>> findNew() {
        List<Map<String, Object>> result = faxFindService.findNew();
        return ResponseEntity.ok(result);
    }
}

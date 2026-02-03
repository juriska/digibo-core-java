package com.digibo.core.controller;

import com.digibo.core.service.MarginService;
import com.digibo.core.util.DateUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * MarginController - REST controller for margin trading operations
 * Maps to /api/margin endpoints
 */
@RestController
@RequestMapping("/api/margin")
public class MarginController {

    private final MarginService marginService;

    public MarginController(MarginService marginService) {
        this.marginService = marginService;
    }

    /**
     * GET /api/margin/orders/search
     * Search margin orders by filters
     *
     * @param custId Customer ID
     * @param custName Customer name (partial match)
     * @param userLogin User login (partial match)
     * @param userPassword User password (if required)
     * @param docClass Document class
     * @param rateFrom Minimum exchange rate
     * @param rateTill Maximum exchange rate
     * @param orderCCY Order currency code
     * @param contraryCCY Contrary currency code
     * @param expiryFrom Start expiry date (ISO format)
     * @param expiryTill End expiry date (ISO format)
     * @param docId Document ID
     * @param statuses Comma-separated status IDs
     * @param createdFrom Start date (ISO format)
     * @param createdTill End date (ISO format)
     */
    @GetMapping("/orders/search")
    public ResponseEntity<List<Map<String, Object>>> find(
            @RequestParam(required = false) String custId,
            @RequestParam(required = false) String custName,
            @RequestParam(required = false) String userLogin,
            @RequestParam(required = false) String userPassword,
            @RequestParam(required = false) String docClass,
            @RequestParam(required = false) Double rateFrom,
            @RequestParam(required = false) Double rateTill,
            @RequestParam(required = false) String orderCCY,
            @RequestParam(required = false) String contraryCCY,
            @RequestParam(required = false) String expiryFrom,
            @RequestParam(required = false) String expiryTill,
            @RequestParam(required = false) String docId,
            @RequestParam(required = false) String statuses,
            @RequestParam(required = false) String createdFrom,
            @RequestParam(required = false) String createdTill) {

        List<Map<String, Object>> result = marginService.find(
                custId, custName, userLogin, userPassword, docClass,
                rateFrom, rateTill, orderCCY, contraryCCY,
                DateUtils.parseDate(expiryFrom), DateUtils.parseDate(expiryTill), docId, statuses,
                DateUtils.parseDate(createdFrom), DateUtils.parseDate(createdTill));
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/margin/orders/my
     * Get user's margin orders
     *
     * @param docClass Document class (optional)
     */
    @GetMapping("/orders/my")
    public ResponseEntity<List<Map<String, Object>>> findMy(
            @RequestParam(required = false) String docClass) {
        List<Map<String, Object>> result = marginService.findMy(docClass);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/margin/orders/{id}/details
     * Get detailed margin order info
     */
    @GetMapping("/orders/{id}/details")
    public ResponseEntity<Map<String, Object>> margin(@PathVariable String id) {
        Map<String, Object> result = marginService.margin(id);
        return ResponseEntity.ok(result);
    }
}

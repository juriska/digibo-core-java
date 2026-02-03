package com.digibo.core.controller;

import com.digibo.core.service.InsuranceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * InsuranceController - REST controller for insurance operations
 * Maps to /api/insurance endpoints
 */
@RestController
@RequestMapping("/api/insurance")
public class InsuranceController {

    private final InsuranceService insuranceService;

    public InsuranceController(InsuranceService insuranceService) {
        this.insuranceService = insuranceService;
    }

    /**
     * GET /api/insurance/orders/search
     * Search insurance orders by filters
     *
     * @param custId Customer ID
     * @param custName Customer name (partial match)
     * @param userLogin User login (partial match)
     * @param officerId Officer ID
     * @param docClass Document class
     * @param docId Document ID
     * @param channels Comma-separated channel IDs
     * @param statuses Comma-separated status IDs
     * @param createdFrom Start date (ISO format)
     * @param createdTill End date (ISO format)
     * @param fromLocation Location code
     */
    @GetMapping("/orders/search")
    public ResponseEntity<List<Map<String, Object>>> find(
            @RequestParam(required = false) String custId,
            @RequestParam(required = false) String custName,
            @RequestParam(required = false) String userLogin,
            @RequestParam(required = false) Long officerId,
            @RequestParam(required = false) String docClass,
            @RequestParam(required = false) String docId,
            @RequestParam(required = false) String channels,
            @RequestParam(required = false) String statuses,
            @RequestParam(required = false) String createdFrom,
            @RequestParam(required = false) String createdTill,
            @RequestParam(required = false) String fromLocation) {

        Date createdFromDate = parseDate(createdFrom);
        Date createdTillDate = parseDate(createdTill);

        List<Map<String, Object>> result = insuranceService.find(
                custId, custName, userLogin, officerId, docClass, docId,
                channels, statuses, createdFromDate, createdTillDate, fromLocation);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/insurance/orders/my
     * Get user's insurance orders
     */
    @GetMapping("/orders/my")
    public ResponseEntity<List<Map<String, Object>>> findMy() {
        List<Map<String, Object>> result = insuranceService.findMy();
        return ResponseEntity.ok(result);
    }

    private Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }
}

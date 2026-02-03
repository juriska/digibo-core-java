package com.digibo.core.controller;

import com.digibo.core.exception.ValidationException;
import com.digibo.core.service.CRUService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * CRUController - REST controller for CRU document operations
 * Maps to /api/cru endpoints
 */
@RestController
@RequestMapping("/api/cru")
public class CRUController {

    private final CRUService cruService;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public CRUController(CRUService cruService) {
        this.cruService = cruService;
    }

    /**
     * GET /api/cru/orders/search
     * Search CRU documents by filters using BOCRU.find()
     *
     * @param custId Customer ID
     * @param docId Document ID
     * @param statuses Comma-separated status IDs
     * @param createdFrom Start date (ISO format)
     * @param createdTill End date (ISO format)
     * @return List of matching documents
     */
    @GetMapping("/orders/search")
    public ResponseEntity<List<Map<String, Object>>> searchOrders(
            @RequestParam(required = false) String custId,
            @RequestParam(required = false) String docId,
            @RequestParam(required = false) String statuses,
            @RequestParam(required = false) String createdFrom,
            @RequestParam(required = false) String createdTill) {

        Date dateFrom = parseDate(createdFrom, "createdFrom");
        Date dateTill = parseDate(createdTill, "createdTill");

        List<Map<String, Object>> result = cruService.find(
                custId, docId, statuses, dateFrom, dateTill);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/cru/orders/{id}/details
     * Get detailed CRU document information using BOCRU.cru() procedure
     *
     * @param id Document ID
     * @return Full document details including customer info, signatures
     */
    @GetMapping("/orders/{id}/details")
    public ResponseEntity<Map<String, Object>> getOrderDetails(@PathVariable String id) {
        Map<String, Object> result = cruService.cru(id);
        return ResponseEntity.ok(result);
    }

    /**
     * Parse date string in ISO format (yyyy-MM-dd)
     */
    private Date parseDate(String dateString, String paramName) {
        if (dateString == null || dateString.isBlank()) {
            return null;
        }
        try {
            synchronized (DATE_FORMAT) {
                return DATE_FORMAT.parse(dateString);
            }
        } catch (ParseException e) {
            throw new ValidationException(
                    "Invalid " + paramName + " date format. Use ISO format (YYYY-MM-DD)");
        }
    }
}

package com.digibo.core.controller;

import com.digibo.core.exception.ValidationException;
import com.digibo.core.service.DRService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * DRController - REST controller for deposit request (DR) document operations
 * Maps to /api/dr endpoints
 */
@RestController
@RequestMapping("/api/dr")
public class DRController {

    private final DRService drService;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public DRController(DRService drService) {
        this.drService = drService;
    }

    /**
     * GET /api/dr/orders/search
     * Search deposit request documents by filters using BODR.find()
     *
     * @param custId Customer ID
     * @param custName Customer name (partial match)
     * @param userLogin User login (partial match)
     * @param officerId Officer ID
     * @param pClassId Document class ID
     * @param pTerm Deposit term
     * @param amountFrom Minimum amount
     * @param amountTill Maximum amount
     * @param currencies Currency code
     * @param docId Document ID
     * @param statuses Comma-separated status IDs
     * @param createdFrom Start date (ISO format)
     * @param createdTill End date (ISO format)
     * @return List of matching documents
     */
    @GetMapping("/orders/search")
    public ResponseEntity<List<Map<String, Object>>> searchOrders(
            @RequestParam(required = false) String custId,
            @RequestParam(required = false) String custName,
            @RequestParam(required = false) String userLogin,
            @RequestParam(required = false) Long officerId,
            @RequestParam(required = false) Long pClassId,
            @RequestParam(required = false) String pTerm,
            @RequestParam(required = false) String amountFrom,
            @RequestParam(required = false) String amountTill,
            @RequestParam(required = false) String currencies,
            @RequestParam(required = false) String docId,
            @RequestParam(required = false) String statuses,
            @RequestParam(required = false) String createdFrom,
            @RequestParam(required = false) String createdTill) {

        Date dateFrom = parseDate(createdFrom, "createdFrom");
        Date dateTill = parseDate(createdTill, "createdTill");

        List<Map<String, Object>> result = drService.find(
                custId, custName, userLogin, officerId, pClassId, pTerm,
                amountFrom, amountTill, currencies, docId, statuses,
                dateFrom, dateTill);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/dr/orders/{id}/details
     * Get detailed deposit request information using BODR.dr() procedure
     *
     * @param id Document ID
     * @return Full document details including customer info, deposit terms
     */
    @GetMapping("/orders/{id}/details")
    public ResponseEntity<Map<String, Object>> getOrderDetails(@PathVariable String id) {
        Map<String, Object> result = drService.dr(id);
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

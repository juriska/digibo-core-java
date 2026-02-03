package com.digibo.core.controller;

import com.digibo.core.exception.ValidationException;
import com.digibo.core.service.DDService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * DDController - REST controller for direct debit (DD) document operations
 * Maps to /api/dd endpoints
 */
@RestController
@RequestMapping("/api/dd")
public class DDController {

    private final DDService ddService;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public DDController(DDService ddService) {
        this.ddService = ddService;
    }

    /**
     * GET /api/dd/orders/search
     * Search DD documents using BODD.find()
     *
     * @param custId Customer ID
     * @param custName Customer name (partial match)
     * @param userLogin User login (partial match)
     * @param officerId Officer ID
     * @param pType Payment type (comma-separated)
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
            @RequestParam(required = false) String pType,
            @RequestParam(required = false) String docId,
            @RequestParam(required = false) String statuses,
            @RequestParam(required = false) String createdFrom,
            @RequestParam(required = false) String createdTill) {

        Date dateFrom = parseDate(createdFrom, "createdFrom");
        Date dateTill = parseDate(createdTill, "createdTill");

        List<Map<String, Object>> result = ddService.find(
                custId, custName, userLogin, officerId, pType, docId,
                statuses, dateFrom, dateTill);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/dd/orders/{id}/details
     * Get detailed DD order info using BODD.dd() procedure
     *
     * @param id Document ID
     * @return Full direct debit details including beneficiary, schedule, and abonent info
     */
    @GetMapping("/orders/{id}/details")
    public ResponseEntity<Map<String, Object>> getOrderDetails(@PathVariable String id) {
        Map<String, Object> result = ddService.dd(id);
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

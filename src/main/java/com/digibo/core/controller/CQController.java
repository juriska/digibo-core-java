package com.digibo.core.controller;

import com.digibo.core.exception.ValidationException;
import com.digibo.core.service.CQService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * CQController - REST controller for client questionnaire operations
 * Maps to /api/cq endpoints
 */
@RestController
@RequestMapping("/api/cq")
public class CQController {

    private final CQService cqService;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public CQController(CQService cqService) {
        this.cqService = cqService;
    }

    /**
     * GET /api/cq/orders/search
     * Search client questionnaire documents by filters using BOCQ.find()
     *
     * @param custId Customer ID
     * @param custName Customer name (partial match)
     * @param userLogin User login (partial match)
     * @param officerId Officer ID
     * @param docClass Comma-separated document class IDs
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
            @RequestParam(required = false) String docClass,
            @RequestParam(required = false) String docId,
            @RequestParam(required = false) String statuses,
            @RequestParam(required = false) String createdFrom,
            @RequestParam(required = false) String createdTill) {

        Date dateFrom = parseDate(createdFrom, "createdFrom");
        Date dateTill = parseDate(createdTill, "createdTill");

        List<Map<String, Object>> result = cqService.find(
                custId, custName, userLogin, officerId, docClass, docId,
                statuses, dateFrom, dateTill);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/cq/orders/my
     * Get user's CQ documents using BOCQ.find_my()
     *
     * @param docClass Comma-separated document class IDs (required)
     * @return List of user's documents
     */
    @GetMapping("/orders/my")
    public ResponseEntity<List<Map<String, Object>>> getMyOrders(
            @RequestParam String docClass) {

        if (docClass == null || docClass.isBlank()) {
            throw new ValidationException("Missing required parameter: docClass");
        }

        List<Map<String, Object>> result = cqService.findMy(docClass);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/cq/orders/{id}/details
     * Get detailed client questionnaire information using BOCQ.cq() procedure
     *
     * @param id Document ID
     * @return Full document details including customer info, authorization details
     */
    @GetMapping("/orders/{id}/details")
    public ResponseEntity<Map<String, Object>> getOrderDetails(@PathVariable String id) {
        Map<String, Object> result = cqService.cq(id);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/cq/orders/{id}/extensions
     * Get document extensions using BOCQ.get_extensions()
     *
     * @param id Document ID
     * @return Document extensions
     */
    @GetMapping("/orders/{id}/extensions")
    public ResponseEntity<List<Map<String, Object>>> getOrderExtensions(@PathVariable String id) {
        List<Map<String, Object>> result = cqService.getExtensions(id);
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

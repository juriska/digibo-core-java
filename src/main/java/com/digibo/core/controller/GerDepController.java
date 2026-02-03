package com.digibo.core.controller;

import com.digibo.core.exception.ValidationException;
import com.digibo.core.service.GerDepService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * GerDepController - REST controller for German deposit operations
 * Maps to /api/gerdep endpoints
 */
@RestController
@RequestMapping("/api/gerdep")
public class GerDepController {

    private final GerDepService gerDepService;

    public GerDepController(GerDepService gerDepService) {
        this.gerDepService = gerDepService;
    }

    /**
     * GET /api/gerdep/orders/new
     * Get new German deposit orders
     */
    @GetMapping("/orders/new")
    public ResponseEntity<List<Map<String, Object>>> findNew() {
        List<Map<String, Object>> result = gerDepService.findNew();
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/gerdep/orders/search
     * Search German deposit orders by filters
     *
     * @param docId Document ID
     * @param custId Customer ID
     * @param custName Customer name (partial match)
     * @param idDocNo ID document number
     * @param login User login (partial match)
     * @param status Status
     * @param orderDateFrom Start date (ISO format)
     * @param orderDateTo End date (ISO format)
     */
    @GetMapping("/orders/search")
    public ResponseEntity<List<Map<String, Object>>> findByFilter(
            @RequestParam(required = false) String docId,
            @RequestParam(required = false) String custId,
            @RequestParam(required = false) String custName,
            @RequestParam(required = false) String idDocNo,
            @RequestParam(required = false) String login,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String orderDateFrom,
            @RequestParam(required = false) String orderDateTo) {

        Date orderDateFromDate = parseDate(orderDateFrom);
        Date orderDateToDate = parseDate(orderDateTo);

        List<Map<String, Object>> result = gerDepService.findByFilter(
                docId, custId, custName, idDocNo, login, status, orderDateFromDate, orderDateToDate);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/gerdep/customers/{custId}
     * Select customer for German deposit
     */
    @GetMapping("/customers/{custId}")
    public ResponseEntity<Map<String, Object>> selectCustomer(@PathVariable String custId) {
        Map<String, Object> result = gerDepService.selectCustomer(custId);

        Integer rv = (Integer) result.get("rv");
        if (rv != null && rv != 0) {
            return ResponseEntity.status(404).body(Map.of(
                    "error", "Customer not found or not eligible",
                    "rv", rv,
                    "customerId", custId
            ));
        }

        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/gerdep/orders/{docId}/bind
     * Bind German deposit order to customer
     */
    @PostMapping("/orders/{docId}/bind")
    public ResponseEntity<Map<String, Object>> bindToCustomer(
            @PathVariable String docId,
            @RequestBody Map<String, String> request) {

        String custId = request.get("custId");
        if (custId == null || custId.isBlank()) {
            throw new ValidationException("Missing required field: custId");
        }

        Map<String, Object> result = gerDepService.bindToCustomer(docId, custId);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "docId", docId,
                "custId", custId,
                "message", "Order successfully bound to customer",
                "data", result
        ));
    }

    /**
     * GET /api/gerdep/orders/{docId}/account-exists
     * Check if account exists for German deposit order
     */
    @GetMapping("/orders/{docId}/account-exists")
    public ResponseEntity<Map<String, Object>> accountExists(@PathVariable String docId) {
        Map<String, Object> result = gerDepService.accountExists(docId);
        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/gerdep/orders/{docId}/create-user
     * Create user for German deposit
     */
    @PostMapping("/orders/{docId}/create-user")
    public ResponseEntity<Map<String, Object>> createUser(
            @PathVariable String docId,
            @RequestBody Map<String, String> request) {

        String tanCardId = request.get("tanCardId");
        if (tanCardId == null || tanCardId.isBlank()) {
            throw new ValidationException("Missing required field: tanCardId");
        }

        Map<String, Object> result = gerDepService.createUser(docId, tanCardId);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "docId", docId,
                "message", "User created successfully",
                "data", result
        ));
    }

    /**
     * POST /api/gerdep/orders/{docId}/reject
     * Reject German deposit order
     */
    @PostMapping("/orders/{docId}/reject")
    public ResponseEntity<Map<String, Object>> reject(
            @PathVariable String docId,
            @RequestBody Map<String, String> request) {

        String reason = request.get("reason");
        if (reason == null || reason.isBlank()) {
            throw new ValidationException("Missing required field: reason");
        }

        Map<String, Object> result = gerDepService.reject(docId, reason);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "docId", docId,
                "message", "Order rejected successfully",
                "data", result
        ));
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

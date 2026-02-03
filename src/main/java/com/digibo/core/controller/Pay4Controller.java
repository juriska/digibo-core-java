package com.digibo.core.controller;

import com.digibo.core.service.Pay4Service;
import com.digibo.core.util.DateUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Pay4Controller - REST controller for Pay4 payment operations
 * Maps to /api/pay4 endpoints
 */
@RestController
@RequestMapping("/api/pay4")
public class Pay4Controller {

    private final Pay4Service pay4Service;

    public Pay4Controller(Pay4Service pay4Service) {
        this.pay4Service = pay4Service;
    }

    /**
     * GET /api/pay4/payments/search
     * Search payment documents using BOPayment.find()
     *
     * Query params:
     * - custId: Customer ID
     * - custName: Customer name (partial match)
     * - userLogin: User login (partial match)
     * - officerId: Officer ID
     * - benName: Beneficiary name (partial match)
     * - pmtDetails: Payment details (partial match)
     * - fromContract: From contract (partial match)
     * - amountFrom: Minimum amount
     * - amountTill: Maximum amount
     * - currencies: Currency filter
     * - pmtClass: Payment class (comma-separated)
     * - effectFrom: Effect date from
     * - effectTill: Effect date till
     * - paymentId: Payment ID
     * - channels: Comma-separated channel IDs
     * - statuses: Comma-separated status IDs
     * - createdFrom: Start date (ISO format)
     * - createdTill: End date (ISO format)
     */
    @GetMapping("/payments/search")
    public ResponseEntity<List<Map<String, Object>>> searchPayments(
            @RequestParam(required = false) String custId,
            @RequestParam(required = false) String custName,
            @RequestParam(required = false) String userLogin,
            @RequestParam(required = false) Long officerId,
            @RequestParam(required = false) String benName,
            @RequestParam(required = false) String pmtDetails,
            @RequestParam(required = false) String fromContract,
            @RequestParam(required = false) String amountFrom,
            @RequestParam(required = false) String amountTill,
            @RequestParam(required = false) String currencies,
            @RequestParam(required = false) String pmtClass,
            @RequestParam(required = false) String effectFrom,
            @RequestParam(required = false) String effectTill,
            @RequestParam(required = false) String paymentId,
            @RequestParam(required = false) String channels,
            @RequestParam(required = false) String statuses,
            @RequestParam(required = false) String createdFrom,
            @RequestParam(required = false) String createdTill) {
        List<Map<String, Object>> result = pay4Service.find(
                custId, custName, userLogin, officerId, benName, pmtDetails, fromContract,
                amountFrom, amountTill, currencies, pmtClass,
                DateUtils.parseDate(effectFrom), DateUtils.parseDate(effectTill),
                paymentId, channels, statuses,
                DateUtils.parseDate(createdFrom), DateUtils.parseDate(createdTill));
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/pay4/payments/:id/details
     * Get detailed payment info using BOPayment.payment() procedure
     * Returns full payment details including beneficiary, ordering customer, bank details
     */
    @GetMapping("/payments/{id}/details")
    public ResponseEntity<Map<String, Object>> getPaymentDetails(@PathVariable String id) {
        Map<String, Object> result = pay4Service.payment(id);
        return ResponseEntity.ok(result);
    }
}

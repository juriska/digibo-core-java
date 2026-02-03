package com.digibo.core.controller;

import com.digibo.core.service.RequestToPayService;
import com.digibo.core.util.DateUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * RequestToPayController - REST controller for Request to Pay operations
 * Maps to /api/requesttopay endpoints
 */
@RestController
@RequestMapping("/api/requesttopay")
public class RequestToPayController {

    private final RequestToPayService requestToPayService;

    public RequestToPayController(RequestToPayService requestToPayService) {
        this.requestToPayService = requestToPayService;
    }

    /**
     * GET /api/requesttopay/search
     * Search Request to Pay records by filters using BORequestToPay.find()
     *
     * Query params:
     * Remitter:
     * - custId: Customer ID
     * - custName: Customer name (partial match)
     * - userLogin: User login (partial match)
     * - officerId: Officer ID
     *
     * Payment:
     * - benName: Beneficiary name (partial match)
     * - fromContract: From contract/account
     * - fromLocation: From location code
     * - pmtDetails: Payment details (partial match)
     * - amountFrom: Minimum amount
     * - amountTill: Maximum amount
     * - currencies: Comma-separated currency codes
     * - pmtClass: Comma-separated payment class IDs
     * - effectFrom: Effect date from (ISO format)
     * - effectTill: Effect date till (ISO format)
     *
     * System:
     * - paymentId: Payment ID
     * - cbPaymentId: Core banking payment ID
     * - channels: Comma-separated channel IDs
     * - statuses: Comma-separated status IDs
     * - createdFrom: Created date from (ISO format)
     * - createdTill: Created date till (ISO format)
     */
    @GetMapping("/search")
    public ResponseEntity<List<Map<String, Object>>> search(
            // Remitter filters
            @RequestParam(required = false) String custId,
            @RequestParam(required = false) String custName,
            @RequestParam(required = false) String userLogin,
            @RequestParam(required = false) Long officerId,
            // Payment filters
            @RequestParam(required = false) String benName,
            @RequestParam(required = false) String fromContract,
            @RequestParam(required = false) String fromLocation,
            @RequestParam(required = false) String pmtDetails,
            @RequestParam(required = false) String amountFrom,
            @RequestParam(required = false) String amountTill,
            @RequestParam(required = false) String currencies,
            @RequestParam(required = false) String pmtClass,
            @RequestParam(required = false) String effectFrom,
            @RequestParam(required = false) String effectTill,
            // System filters
            @RequestParam(required = false) String paymentId,
            @RequestParam(required = false) String cbPaymentId,
            @RequestParam(required = false) String channels,
            @RequestParam(required = false) String statuses,
            @RequestParam(required = false) String createdFrom,
            @RequestParam(required = false) String createdTill) {
        List<Map<String, Object>> result = requestToPayService.find(
                custId, custName, userLogin, officerId,
                benName, fromContract, fromLocation, pmtDetails, amountFrom, amountTill,
                currencies, pmtClass,
                DateUtils.parseDate(effectFrom), DateUtils.parseDate(effectTill),
                paymentId, cbPaymentId, channels, statuses,
                DateUtils.parseDate(createdFrom), DateUtils.parseDate(createdTill));
        return ResponseEntity.ok(result);
    }
}

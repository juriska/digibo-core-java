package com.digibo.core.controller;

import com.digibo.core.dto.request.PaymentSearchRequest;
import com.digibo.core.dto.response.PaymentDetailsResponse;
import com.digibo.core.dto.response.PaymentSearchResponse;
import com.digibo.core.exception.ResourceNotFoundException;
import com.digibo.core.exception.ValidationException;
import com.digibo.core.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * PaymentController - REST controller for payment operations
 * Maps to /api/payments endpoints
 *
 * All endpoints require authentication and specific permissions.
 */
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * POST /api/payments/find
     * Find payments with various filters
     */
    @PostMapping("/find")
    @PreAuthorize("hasPermission(null, 'BO_PAYMENT.FIND')")
    public ResponseEntity<PaymentSearchResponse> findPayments(@RequestBody PaymentSearchRequest filters) {
        PaymentSearchResponse result = paymentService.find(filters);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/payments/:id
     * Get detailed payment information by ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'BO_PAYMENT.GET_DETAILS')")
    public ResponseEntity<PaymentDetailsResponse> getPaymentDetails(@PathVariable String id) {
        PaymentDetailsResponse details = paymentService.getPaymentDetails(id);

        if (details.getUserId() == null) {
            throw new ResourceNotFoundException("Payment", id);
        }

        return ResponseEntity.ok(details);
    }

    /**
     * POST /api/payments/:id/template-group
     * Change template group for a payment
     */
    @PostMapping("/{id}/template-group")
    @PreAuthorize("hasPermission(null, 'BO_PAYMENT.CHANGE_TEMPLATE_GROUP')")
    public ResponseEntity<Map<String, Object>> changeTemplateGroup(
            @PathVariable String id,
            @RequestBody Map<String, String> request) {

        String groupId = request.get("groupId");
        if (groupId == null || groupId.isBlank()) {
            throw new ValidationException("Missing required field: groupId");
        }

        paymentService.changeTemplateGroup(id, groupId);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "paymentId", id,
                "groupId", groupId,
                "message", "Template group changed successfully"
        ));
    }
}

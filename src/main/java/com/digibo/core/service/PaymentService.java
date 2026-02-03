package com.digibo.core.service;

import com.digibo.core.dto.request.PaymentSearchRequest;
import com.digibo.core.dto.response.PaymentDetailsResponse;
import com.digibo.core.dto.response.PaymentSearchResponse;

/**
 * PaymentService - Service interface for BOPayment Oracle package
 * Handles payment search and details operations
 */
public interface PaymentService {

    /**
     * Find payments with various filters
     * @param filters Payment search filters
     * @return Search results with payments and pmtClass
     */
    PaymentSearchResponse find(PaymentSearchRequest filters);

    /**
     * Get detailed payment information by ID
     * @param paymentId Payment ID
     * @return Payment details
     */
    PaymentDetailsResponse getPaymentDetails(String paymentId);

    /**
     * Change template group for a payment
     * @param paymentId Payment ID
     * @param groupId Group ID
     */
    void changeTemplateGroup(String paymentId, String groupId);
}

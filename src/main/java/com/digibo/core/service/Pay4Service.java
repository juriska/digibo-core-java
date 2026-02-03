package com.digibo.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Pay4Service - Service interface for BOPayment Oracle package
 * Handles payment document operations
 */
public interface Pay4Service {

    /**
     * Find payment documents by filters
     * @param custId Customer ID
     * @param custName Customer name
     * @param userLogin User login
     * @param officerId Officer ID
     * @param benName Beneficiary name
     * @param pmtDetails Payment details
     * @param fromContract From contract
     * @param amountFrom Amount from
     * @param amountTill Amount till
     * @param currencies Currencies (comma-separated)
     * @param pmtClass Payment class (comma-separated)
     * @param effectFrom Effect date from
     * @param effectTill Effect date till
     * @param paymentId Payment ID
     * @param channels Channels (comma-separated)
     * @param statuses Statuses (comma-separated)
     * @param createdFrom Created from date
     * @param createdTill Created till date
     * @return List of matching payment documents
     */
    List<Map<String, Object>> find(String custId, String custName, String userLogin, Long officerId,
                                    String benName, String pmtDetails, String fromContract,
                                    String amountFrom, String amountTill, String currencies, String pmtClass,
                                    Date effectFrom, Date effectTill,
                                    String paymentId, String channels, String statuses,
                                    Date createdFrom, Date createdTill);

    /**
     * Get detailed payment document information
     * @param paymentId Payment document ID
     * @return Detailed payment info map
     */
    Map<String, Object> payment(String paymentId);
}

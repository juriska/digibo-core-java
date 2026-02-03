package com.digibo.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * RequestToPayService - Service interface for BORequestToPay Oracle package
 * Handles Request to Pay operations and related functionality
 */
public interface RequestToPayService {

    /**
     * Find Request to Pay records by filters
     * @param custId Customer ID
     * @param custName Customer name
     * @param userLogin User login
     * @param officerId Officer ID
     * @param benName Beneficiary name
     * @param fromContract From contract
     * @param fromLocation From location
     * @param pmtDetails Payment details
     * @param amountFrom Amount from
     * @param amountTill Amount till
     * @param currencies Currencies (comma-separated)
     * @param pmtClass Payment class (comma-separated)
     * @param effectFrom Effect date from
     * @param effectTill Effect date till
     * @param paymentId Payment ID
     * @param cbPaymentId CB Payment ID
     * @param channels Channels (comma-separated)
     * @param statuses Statuses (comma-separated)
     * @param createdFrom Created from date
     * @param createdTill Created till date
     * @return List of matching Request to Pay records
     */
    List<Map<String, Object>> find(String custId, String custName, String userLogin, Long officerId,
                                    String benName, String fromContract, String fromLocation, String pmtDetails,
                                    String amountFrom, String amountTill, String currencies, String pmtClass,
                                    Date effectFrom, Date effectTill,
                                    String paymentId, String cbPaymentId, String channels, String statuses,
                                    Date createdFrom, Date createdTill);
}

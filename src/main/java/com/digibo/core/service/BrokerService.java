package com.digibo.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * BrokerService - Service interface for BOBroker Oracle package
 * Handles Secure (Broker) orders
 */
public interface BrokerService {

    /**
     * Find broker orders by filters
     * @param custId Customer ID
     * @param custName Customer name
     * @param userLogin User login
     * @param userPassword User password
     * @param docClass Document class
     * @param operationType Operation type
     * @param docCount Document count
     * @param currencies Comma-separated currencies
     * @param expiryFrom Expiry start date
     * @param expiryTill Expiry end date
     * @param docId Document ID
     * @param statuses Comma-separated status IDs
     * @param createdFrom Start date filter
     * @param createdTill End date filter
     * @return List of matching orders
     */
    List<Map<String, Object>> find(String custId, String custName, String userLogin, String userPassword,
                                   String docClass, String operationType, Integer docCount, String currencies,
                                   Date expiryFrom, Date expiryTill,
                                   String docId, String statuses, Date createdFrom, Date createdTill);

    /**
     * Get user's broker orders
     * @param docClass Document class filter
     * @return List of orders
     */
    List<Map<String, Object>> findMy(String docClass);

    /**
     * Get detailed broker order information
     * @param docId Document ID
     * @return Detailed order info as map
     */
    Map<String, Object> broker(String docId);
}

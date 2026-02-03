package com.digibo.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * CustodyService - Service interface for BOCustody Oracle package
 * Handles custody order operations
 */
public interface CustodyService {

    /**
     * Find custody orders by filters
     * @param custId Customer ID
     * @param custName Customer name
     * @param userLogin User login
     * @param docId Document ID
     * @param statuses Status filter (comma-separated)
     * @param docClass Document class
     * @param createdFrom Created from date
     * @param createdTill Created till date
     * @return List of matching custody orders
     */
    List<Map<String, Object>> find(String custId, String custName, String userLogin,
                                    String docId, String statuses, String docClass,
                                    Date createdFrom, Date createdTill);

    /**
     * Get user's custody orders using findMy function
     * @param officerId Officer ID
     * @return List of custody orders
     */
    List<Map<String, Object>> findMy(String officerId);

    /**
     * Set processing status for custody order
     * @param orderId Order ID
     * @return Result map with success, orderId, and result code
     */
    Map<String, Object> setProcessing(String orderId);

    /**
     * Get detailed custody order information
     * @param orderId Order ID
     * @return Detailed custody order info as Map
     */
    Map<String, Object> custody(String orderId);
}

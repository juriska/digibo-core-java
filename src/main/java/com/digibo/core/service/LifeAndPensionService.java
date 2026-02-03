package com.digibo.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * LifeAndPensionService - Service interface for BOLifeAndPension Oracle package
 * Handles life and pension orders and related operations
 */
public interface LifeAndPensionService {

    /**
     * Find life and pension orders by filters
     * @param custId Customer ID
     * @param custName Customer name
     * @param userLogin User login
     * @param docId Document ID
     * @param statuses Comma-separated status IDs
     * @param docClass Document class
     * @param createdFrom Created date from
     * @param createdTill Created date till
     * @return List of matching life and pension orders
     */
    List<Map<String, Object>> find(String custId, String custName, String userLogin,
                                    String docId, String statuses, String docClass,
                                    Date createdFrom, Date createdTill);

    /**
     * Get officer's life and pension orders
     * @param officerId Officer ID (0 for new orders)
     * @return List of life and pension orders
     */
    List<Map<String, Object>> findMy(Long officerId);

    /**
     * Set processing status for life and pension order
     * @param orderId Order ID
     * @return Map containing success flag, orderId, and result
     */
    Map<String, Object> setProcessing(String orderId);
}

package com.digibo.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * AmexOrderService - Service interface for BOamexorder Oracle package
 * Handles Web Orders (AMEX orders) and related operations
 */
public interface AmexOrderService {

    /**
     * Find AMEX orders by filters
     * @param custId Customer ID
     * @param custName Customer name
     * @param userLogin User login
     * @param docId Document ID
     * @param statuses Comma-separated status IDs
     * @param docClass Document class
     * @param fromLocation Location filter
     * @param createdFrom Start date filter
     * @param createdTill End date filter
     * @param customerName Customer name filter
     * @param legalId Legal ID filter
     * @param formType Form type filter
     * @return List of matching orders
     */
    List<Map<String, Object>> find(String custId, String custName, String userLogin,
                                   String docId, String statuses, String docClass, String fromLocation,
                                   Date createdFrom, Date createdTill,
                                   String customerName, String legalId, String formType);

    /**
     * Get user's AMEX orders (find_my)
     * @param officerId Officer ID (0 for new orders)
     * @return List of orders
     */
    List<Map<String, Object>> findMy(Long officerId);

    /**
     * Get detailed AMEX order information
     * @param docId Document ID
     * @return Detailed order info as map
     */
    Map<String, Object> amexorder(String docId);

    /**
     * Set processing status for AMEX order
     * @param docId Document ID
     * @return Officer ID if successful, 0 or negative otherwise
     */
    int setProcessing(String docId);
}

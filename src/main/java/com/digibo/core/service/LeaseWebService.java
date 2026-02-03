package com.digibo.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * LeaseWebService - Service interface for BOLeaseWEB Oracle package
 * Handles lease web orders and related operations
 */
public interface LeaseWebService {

    /**
     * Find lease web orders by filters
     * @param custId Customer ID
     * @param custName Customer name
     * @param userLogin User login
     * @param docId Document ID
     * @param statuses Comma-separated status IDs
     * @param docClass Document class
     * @param createdFrom Created date from
     * @param createdTill Created date till
     * @param customerName Customer name filter
     * @param legalId Legal ID
     * @return List of matching lease web orders
     */
    List<Map<String, Object>> find(String custId, String custName, String userLogin,
                                    String docId, String statuses, String docClass,
                                    Date createdFrom, Date createdTill,
                                    String customerName, String legalId);

    /**
     * Get officer's lease web orders
     * @param officerId Officer ID (0 for new orders)
     * @return List of lease web orders
     */
    List<Map<String, Object>> findMy(Long officerId);

    /**
     * Get detailed AMEX order information
     * @param orderId Order ID
     * @return Map containing all order details
     */
    Map<String, Object> amexorder(String orderId);

    /**
     * Set processing status for lease web order
     * @param orderId Order ID
     * @param statusIdFrom Current status ID to check
     * @return Map containing success flag, orderId, and result
     */
    Map<String, Object> setProcessing(String orderId, Long statusIdFrom);
}

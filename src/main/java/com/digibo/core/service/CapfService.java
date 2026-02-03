package com.digibo.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * CapfService - Service interface for BOCapf Oracle package
 * Handles CAPF orders and related operations
 */
public interface CapfService {

    /**
     * Find CAPF orders by filters
     * @param custId Customer ID
     * @param custName Customer name
     * @param userLogin User login
     * @param docId Document ID
     * @param statuses Comma-separated status IDs
     * @param docClass Document class
     * @param createdFrom Start date filter
     * @param createdTill End date filter
     * @param customerName Customer name filter
     * @param legalId Legal ID filter
     * @return List of matching CAPF orders
     */
    List<Map<String, Object>> find(String custId, String custName, String userLogin,
                                   String docId, String statuses, String docClass,
                                   Date createdFrom, Date createdTill,
                                   String customerName, String legalId);

    /**
     * Get officer's CAPF orders using find_my function
     * @param officerId Officer ID (0 for new orders)
     * @return List of CAPF orders
     */
    List<Map<String, Object>> findMy(Long officerId);

    /**
     * Get detailed CAPF order information
     * @param orderId Order ID
     * @return Detailed CAPF order info as map
     */
    Map<String, Object> capforder(String orderId);

    /**
     * Set processing status for CAPF order
     * @param orderId Order ID
     * @return Result code (positive = success)
     */
    int setProcessing(String orderId);
}

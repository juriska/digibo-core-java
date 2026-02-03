package com.digibo.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ProdKitService - Service interface for BOProdKit Oracle package
 * Handles Custody orders and related operations
 */
public interface ProdKitService {

    /**
     * Find custody orders by filters
     * @param custId Customer ID
     * @param custName Customer name
     * @param userLogin User login
     * @param docId Document ID
     * @param statuses Status filter (comma-separated)
     * @param docClass Document class filter (comma-separated)
     * @param createdFrom Created from date
     * @param createdTill Created till date
     * @return List of matching custody orders
     */
    List<Map<String, Object>> find(String custId, String custName, String userLogin,
                                    String docId, String statuses, String docClass,
                                    Date createdFrom, Date createdTill);

    /**
     * Get custody orders for specific officer
     * @param officerId Officer ID (0 for new orders)
     * @return List of custody orders
     */
    List<Map<String, Object>> findMy(Integer officerId);

    /**
     * Set processing status for custody order
     * @param docId Document ID
     * @return Result map with success indicator and changeOfficerId
     */
    Map<String, Object> setProcessing(String docId);

    /**
     * Get detailed custody order information
     * @param docId Document ID
     * @return Detailed custody order info map
     */
    Map<String, Object> prodkit(String docId);
}

package com.digibo.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * AccAdminService - Service interface for BOAccAdmin Oracle package
 * Handles Account Administration orders
 */
public interface AccAdminService {

    /**
     * Find account administration orders by filters
     * @param custId Customer ID
     * @param custName Customer name
     * @param userLogin User login
     * @param officerId Officer ID
     * @param docId Document ID
     * @param statuses Comma-separated status IDs
     * @param docClass Document class
     * @param createdFrom Start date filter
     * @param createdTill End date filter
     * @return List of matching orders
     */
    List<Map<String, Object>> find(String custId, String custName, String userLogin, Long officerId,
                                   String docId, String statuses, String docClass,
                                   Date createdFrom, Date createdTill);

    /**
     * Get officer's account administration orders
     * @param officerId Officer ID
     * @return List of orders
     */
    List<Map<String, Object>> findMy(Long officerId);

    /**
     * Set processing status for account administration order
     * @param docId Document ID
     * @return Result code (0 = success)
     */
    int setProcessing(String docId);

    /**
     * Get detailed account administration order information
     * @param docId Document ID
     * @return Detailed order info as map
     */
    Map<String, Object> accadmin(String docId);
}

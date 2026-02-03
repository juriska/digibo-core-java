package com.digibo.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * CardsService - Service interface for BOCards Oracle package
 * Handles Credit Card orders
 */
public interface CardsService {

    /**
     * Find card orders by filters
     * @param custId Customer ID
     * @param custName Customer name
     * @param userLogin User login
     * @param officerId Officer ID
     * @param docClass Document class
     * @param fromLocation Location filter
     * @param docId Document ID
     * @param statuses Comma-separated status IDs
     * @param createdFrom Start date filter
     * @param createdTill End date filter
     * @param channels Comma-separated channel IDs
     * @return List of matching orders
     */
    List<Map<String, Object>> find(String custId, String custName, String userLogin, Long officerId,
                                   String docClass, String fromLocation,
                                   String docId, String statuses, Date createdFrom, Date createdTill,
                                   String channels);

    /**
     * Get officer's card orders
     * @param officerId Officer ID
     * @param docClass Document class filter
     * @return List of orders
     */
    List<Map<String, Object>> findMy(Long officerId, String docClass);

    /**
     * Get card order extensions
     * @param docId Document ID
     * @return List of extensions
     */
    List<Map<String, Object>> getExtensions(String docId);

    /**
     * Set processing status for card order
     * @param docId Document ID
     * @param statusIdFrom Current status ID
     * @return Result code (0 = success)
     */
    int setProcessing(String docId, Integer statusIdFrom);

    /**
     * Get detailed card order information
     * @param docId Document ID
     * @return Detailed order info as map
     */
    Map<String, Object> card(String docId);

    /**
     * Get lost address information
     * @param docId Document ID
     * @return Lost address info as map
     */
    Map<String, Object> getLostAddr(String docId);

    /**
     * Get issue address information
     * @param docId Document ID
     * @return Issue address info as map
     */
    Map<String, Object> getIssueAddr(String docId);
}

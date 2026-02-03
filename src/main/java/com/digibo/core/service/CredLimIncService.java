package com.digibo.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * CredLimIncService - Service interface for BOCredLimInc Oracle package
 * Handles Credit Limit Increase orders and related operations
 */
public interface CredLimIncService {

    /**
     * Find credit limit increase orders by filters
     * @param custId Customer ID
     * @param custName Customer name
     * @param userLogin User login
     * @param docId Document ID
     * @param statuses Status filter (comma-separated)
     * @param docClass Document class
     * @param createdFrom Created from date
     * @param createdTill Created till date
     * @param officerId Officer ID
     * @param fromLocation From location filter
     * @return List of matching credit limit increase documents
     */
    List<Map<String, Object>> find(String custId, String custName, String userLogin,
                                    String docId, String statuses, String docClass,
                                    Date createdFrom, Date createdTill, Long officerId,
                                    String fromLocation);

    /**
     * Get credit limit increase orders for a specific officer
     * @param officerId Officer ID (0 for new/unassigned orders)
     * @return List of credit limit increase documents
     */
    List<Map<String, Object>> findMy(Long officerId);

    /**
     * Set processing status for credit limit increase document
     * @param docId Document ID
     * @return Result map with success indicator, documentId, officerId, and result
     */
    Map<String, Object> setProcessing(String docId);
}

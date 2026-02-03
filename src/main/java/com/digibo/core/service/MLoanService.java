package com.digibo.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * MLoanService - Service interface for BOMLoan Oracle package
 * Handles Mortgage Loan orders and related operations
 */
public interface MLoanService {

    /**
     * Find mortgage loan orders by filters
     * @param custId Customer ID
     * @param custName Customer name
     * @param userLogin User login
     * @param docId Document ID
     * @param statuses Comma-separated status IDs
     * @param createdFrom Created date from
     * @param createdTill Created date till
     * @param docClass Document class
     * @param fromLocation From location
     * @return List of matching mortgage loan documents
     */
    List<Map<String, Object>> find(String custId, String custName, String userLogin,
                                    String docId, String statuses,
                                    Date createdFrom, Date createdTill,
                                    String docClass, String fromLocation);

    /**
     * Get mortgage loan orders for a specific officer
     * @param officerId Officer ID (0 for new/unassigned orders)
     * @return List of mortgage loan documents
     */
    List<Map<String, Object>> findMy(Long officerId);

    /**
     * Get detailed mortgage loan information
     * @param docId Document ID
     * @return Map containing all document details
     */
    Map<String, Object> mloan(String docId);

    /**
     * Set processing status for mortgage loan document
     * @param docId Document ID
     * @return Map containing success flag, documentId, officerId, and result
     */
    Map<String, Object> setProcessing(String docId);
}

package com.digibo.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * LeaseApplicationsService - Service interface for BOLeaseApplications Oracle package
 * Handles Lease Application orders and related operations
 */
public interface LeaseApplicationsService {

    /**
     * Find lease application orders by filters
     * @param custId Customer ID
     * @param custName Customer name
     * @param userLogin User login
     * @param docId Document ID
     * @param statuses Comma-separated status IDs
     * @param docClass Document class
     * @param createdFrom Created date from
     * @param createdTill Created date till
     * @return List of matching lease application documents
     */
    List<Map<String, Object>> find(String custId, String custName, String userLogin,
                                    String docId, String statuses, String docClass,
                                    Date createdFrom, Date createdTill);

    /**
     * Get lease application orders for a specific officer
     * @param officerId Officer ID (0 for new/unassigned orders)
     * @return List of lease application documents
     */
    List<Map<String, Object>> findMy(Long officerId);

    /**
     * Set processing status for lease application document
     * @param docId Document ID
     * @param statusIdFrom Current status ID to check before update
     * @return Map containing success flag, documentId, statusIdFrom, officerId, and result
     */
    Map<String, Object> setProcessing(String docId, Long statusIdFrom);
}

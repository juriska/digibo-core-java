package com.digibo.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * CrontoDocService - Service interface for BOCRONTODOC Oracle package
 * Handles Cronto authentication device document operations
 */
public interface CrontoDocService {

    /**
     * Find Cronto documents by filters
     * @param custId Customer ID
     * @param custName Customer name
     * @param userLogin User login
     * @param officerId Officer ID
     * @param pType Document type
     * @param docId Document ID
     * @param statuses Status filter (comma-separated)
     * @param createdFrom Created from date
     * @param createdTill Created till date
     * @return List of matching Cronto documents
     */
    List<Map<String, Object>> find(String custId, String custName, String userLogin,
                                    Long officerId, String pType, String docId,
                                    String statuses, Date createdFrom, Date createdTill);

    /**
     * Set document processing status
     * @param docId Document ID
     * @return Result (officer ID if success, 0 if failed)
     */
    int setProcessing(String docId);

    /**
     * Find documents assigned to current officer
     * @param officerId Officer ID (0 for new unassigned documents)
     * @return List of documents
     */
    List<Map<String, Object>> findMy(Long officerId);
}

package com.digibo.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * CRUService - Service interface for BOCRU Oracle package
 * Handles Confirmation of Risks Undertaken and related operations
 */
public interface CRUService {

    /**
     * Find CRU documents by filters
     * @param custId Customer ID
     * @param docId Document ID
     * @param statuses Status filter (comma-separated)
     * @param createdFrom Created from date
     * @param createdTill Created till date
     * @return List of matching CRU documents
     */
    List<Map<String, Object>> find(String custId, String docId, String statuses,
                                    Date createdFrom, Date createdTill);

    /**
     * Get detailed CRU document information
     * @param docId Document ID
     * @return Detailed CRU document info as Map
     */
    Map<String, Object> cru(String docId);
}

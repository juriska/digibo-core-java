package com.digibo.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * DDService - Service interface for BODD Oracle package
 * Handles Direct Debit order operations
 */
public interface DDService {

    /**
     * Find DD documents by filters
     * @param custId Customer ID
     * @param custName Customer name
     * @param userLogin User login
     * @param officerId Officer ID
     * @param pType Document type
     * @param docId Document ID
     * @param statuses Status filter (comma-separated)
     * @param createdFrom Created from date
     * @param createdTill Created till date
     * @return List of matching DD documents
     */
    List<Map<String, Object>> find(String custId, String custName, String userLogin,
                                    Long officerId, String pType, String docId,
                                    String statuses, Date createdFrom, Date createdTill);

    /**
     * Get detailed DD document information
     * @param ddId DD document ID
     * @return Detailed DD info as Map
     */
    Map<String, Object> dd(String ddId);
}

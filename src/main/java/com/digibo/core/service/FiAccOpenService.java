package com.digibo.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * FiAccOpenService - Service interface for BOFiaccopen Oracle package
 * Handles Financial Instruments Account Opening document operations
 */
public interface FiAccOpenService {

    /**
     * Find FI Account Open documents by filters
     *
     * @param custId Customer ID
     * @param custName Customer name
     * @param userLogin User login
     * @param docId Document ID
     * @param statuses Comma-separated status IDs
     * @param docClass Document class (default "321,322")
     * @param createdFrom Created date from
     * @param createdTill Created date till
     * @return List of matching documents
     */
    List<Map<String, Object>> find(
            String custId,
            String custName,
            String userLogin,
            String docId,
            String statuses,
            String docClass,
            Date createdFrom,
            Date createdTill
    );

    /**
     * Find documents assigned to current officer (role-based)
     *
     * @param officerId Officer ID (0 for new unassigned documents)
     * @return List of documents
     */
    List<Map<String, Object>> findMy(Long officerId);

    /**
     * Set document processing status with status transition
     *
     * @param docId Document ID
     * @param statusIdFrom Current status ID
     * @param statusIdTo Target status ID
     * @return Result (officer ID if success, 0 if failed)
     */
    Integer setProcessing(String docId, Integer statusIdFrom, Integer statusIdTo);
}

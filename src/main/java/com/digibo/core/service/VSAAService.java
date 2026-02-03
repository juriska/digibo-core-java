package com.digibo.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * VSAAService - Service interface for BOVSAA Oracle package
 * Handles VSAA (Virtual Safe Deposit Box Agreement) documents and operations
 */
public interface VSAAService {

    /**
     * Find VSAA documents by filters
     * @param userName User name
     * @param legalId Legal ID
     * @param officerId Officer ID
     * @param docId Document ID
     * @param statuses Status filter
     * @param createdFrom Created from date
     * @param createdTill Created till date
     * @return Array of matching VSAA documents
     */
    List<Map<String, Object>> find(String userName, String legalId, Long officerId,
                                    String docId, String statuses,
                                    Date createdFrom, Date createdTill);

    /**
     * Get detailed VSAA document information
     * @param docId Document ID
     * @return Detailed VSAA document info
     */
    Map<String, Object> vsaa(String docId);
}

package com.digibo.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * VSAAAdvService - Service interface for BOVsaaAdv Oracle package
 * Handles VSAA Advisory documents and operations
 */
public interface VSAAAdvService {

    /**
     * Find VSAA Advisory documents by filters
     * @param custId Customer ID
     * @param custName Customer name
     * @param docId Document ID
     * @param statuses Status filter
     * @param createdFrom Created from date
     * @param createdTill Created till date
     * @return Array of matching VSAA Advisory documents
     */
    List<Map<String, Object>> find(String custId, String custName, String docId, String statuses,
                                    Date createdFrom, Date createdTill);

    /**
     * Get detailed VSAA Advisory document information
     * @param docId Document ID
     * @return Detailed VSAA Advisory document info
     */
    Map<String, Object> adv(String docId);
}

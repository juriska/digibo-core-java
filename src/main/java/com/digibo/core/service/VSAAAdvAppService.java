package com.digibo.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * VSAAAdvAppService - Service interface for BOVsaaAdvApp Oracle package
 * Handles VSAA Advisory Application documents and operations
 */
public interface VSAAAdvAppService {

    /**
     * Find VSAA Advisory Application documents by filters
     * @param custId Customer ID
     * @param custName Customer name
     * @param userLogin User login
     * @param docClass Document class
     * @param legalId Legal ID
     * @param docId Document ID
     * @param statuses Status filter
     * @param createdFrom Created from date
     * @param createdTill Created till date
     * @return Array of matching VSAA Advisory Application documents
     */
    List<Map<String, Object>> find(String custId, String custName, String userLogin, String docClass,
                                    String legalId, String docId, String statuses,
                                    Date createdFrom, Date createdTill);

    /**
     * Get detailed VSAA Advisory Application document information
     * @param docId Document ID
     * @return Detailed VSAA Advisory Application document info
     */
    Map<String, Object> advapp(String docId);
}

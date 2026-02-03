package com.digibo.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * DRService - Service interface for BODR Oracle package
 * Handles Deposit Requests and related operations
 */
public interface DRService {

    /**
     * Find deposit requests by filters
     * @param custId Customer ID
     * @param custName Customer name
     * @param userLogin User login
     * @param officerId Officer ID
     * @param pClassId Class ID
     * @param pTerm Term filter
     * @param amountFrom Amount from
     * @param amountTill Amount till
     * @param currencies Currency filter
     * @param docId Document ID
     * @param statuses Status filter (comma-separated)
     * @param createdFrom Created from date
     * @param createdTill Created till date
     * @return List of matching DR documents
     */
    List<Map<String, Object>> find(String custId, String custName, String userLogin,
                                    Long officerId, Long pClassId, String pTerm,
                                    String amountFrom, String amountTill, String currencies,
                                    String docId, String statuses,
                                    Date createdFrom, Date createdTill);

    /**
     * Get detailed deposit request information
     * @param docId Document ID
     * @return Detailed DR document info as Map
     */
    Map<String, Object> dr(String docId);
}

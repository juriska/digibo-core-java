package com.digibo.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * InsuranceService - Service interface for BOInsurance Oracle package
 * Handles insurance order operations
 */
public interface InsuranceService {

    /**
     * Find insurance orders by filters
     * @param custId Customer ID
     * @param custName Customer name
     * @param userLogin User login
     * @param officerId Officer ID
     * @param docClass Document class
     * @param docId Document ID
     * @param channels Comma-separated channel IDs
     * @param statuses Comma-separated status IDs
     * @param createdFrom Created date from
     * @param createdTill Created date till
     * @param fromLocation From location
     * @return List of matching insurance orders
     */
    List<Map<String, Object>> find(String custId, String custName, String userLogin,
                                    Long officerId, String docClass, String docId,
                                    String channels, String statuses,
                                    Date createdFrom, Date createdTill, String fromLocation);

    /**
     * Get user's insurance orders
     * @return List of insurance orders
     */
    List<Map<String, Object>> findMy();
}

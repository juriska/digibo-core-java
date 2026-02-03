package com.digibo.core.service;

import java.util.List;
import java.util.Map;

/**
 * OTSEService - Service interface for BOOTSE Oracle package
 * Handles OTSE (onboarding/customer binding) operations
 */
public interface OTSEService {

    /**
     * Find OTSE documents by filters
     * @param custId Customer ID
     * @param custName Customer name
     * @param userLogin User login
     * @param personalId Personal ID
     * @param docId Document ID
     * @return List of matching OTSE documents
     */
    List<Map<String, Object>> find(String custId, String custName, String userLogin,
                                    String personalId, String docId);

    /**
     * Find new OTSE documents
     * @return List of new OTSE documents
     */
    List<Map<String, Object>> findNew();

    /**
     * Get customer information
     * @param customerId Customer ID
     * @return Map containing resultCode and customer data
     */
    Map<String, Object> getCustomer(String customerId);

    /**
     * Bind customer to WOC
     * @param wocId Way of Connection ID
     * @param custId Customer ID
     * @param userId User ID
     * @param docId Document ID
     * @return Result map
     */
    Map<String, Object> bind(String wocId, String custId, String userId, String docId);

    /**
     * Set WOC status
     * @param wocId Way of Connection ID
     * @param status Status ID
     * @param subStatus Sub-status ID
     * @return Result map
     */
    Map<String, Object> setWocStatus(String wocId, Integer status, Integer subStatus);
}

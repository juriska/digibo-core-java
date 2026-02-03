package com.digibo.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * PamoService - Service interface for BOPAMO Oracle package
 * Handles PAM (Portfolio Asset Management) order operations
 */
public interface PamoService {

    /**
     * Find PAMO documents by filters
     * @param custId Customer ID
     * @param custName Customer name
     * @param userLogin User login
     * @param docClass Document class
     * @param pIsin ISIN code
     * @param docId Document ID
     * @param statuses Status filter (comma-separated)
     * @param createdFrom Created from date
     * @param createdTill Created till date
     * @return List of matching PAMO documents
     */
    List<Map<String, Object>> find(String custId, String custName, String userLogin,
                                    String docClass, String pIsin, String docId,
                                    String statuses, Date createdFrom, Date createdTill);

    /**
     * Get user's PAMO documents
     * @param docClass Document class filter
     * @return List of PAMO documents
     */
    List<Map<String, Object>> findMy(String docClass);

    /**
     * Get detailed PAMO document information
     * @param pamoId PAMO document ID
     * @return Detailed PAMO info map
     */
    Map<String, Object> pamo(String pamoId);
}

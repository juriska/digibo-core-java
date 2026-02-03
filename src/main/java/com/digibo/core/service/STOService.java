package com.digibo.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * STOService - Service interface for BOSTO Oracle package
 * Handles Standing Order operations
 */
public interface STOService {

    /**
     * Find STO documents by filters
     * @param custId Customer ID
     * @param custName Customer name
     * @param userLogin User login
     * @param officerId Officer ID
     * @param pType Document type
     * @param docId Document ID
     * @param statuses Status filter
     * @param createdFrom Created from date
     * @param createdTill Created till date
     * @return Array of matching STO documents
     */
    List<Map<String, Object>> find(String custId, String custName, String userLogin, Long officerId,
                                    String pType, String docId, String statuses,
                                    Date createdFrom, Date createdTill);

    /**
     * Get detailed STO document information
     * @param stoId STO document ID
     * @return Detailed STO info
     */
    Map<String, Object> sto(String stoId);
}

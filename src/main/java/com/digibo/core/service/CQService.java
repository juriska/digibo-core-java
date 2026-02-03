package com.digibo.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * CQService - Service interface for BOCQ Oracle package
 * Handles Client Questionnaire and related operations
 */
public interface CQService {

    /**
     * Find client questionnaires by filters
     * @param custId Customer ID
     * @param custName Customer name
     * @param userLogin User login
     * @param officerId Officer ID
     * @param docClass Document class
     * @param docId Document ID
     * @param statuses Status filter (comma-separated)
     * @param createdFrom Created from date
     * @param createdTill Created till date
     * @return List of matching CQ documents
     */
    List<Map<String, Object>> find(String custId, String custName, String userLogin,
                                    Long officerId, String docClass, String docId,
                                    String statuses, Date createdFrom, Date createdTill);

    /**
     * Find user's CQ documents using find_my function
     * @param docClass Document class
     * @return List of CQ documents
     */
    List<Map<String, Object>> findMy(String docClass);

    /**
     * Get detailed client questionnaire information
     * @param docId Document ID
     * @return Detailed CQ document info as Map
     */
    Map<String, Object> cq(String docId);

    /**
     * Get document extensions
     * @param docId Document ID
     * @return List of extensions
     */
    List<Map<String, Object>> getExtensions(String docId);
}

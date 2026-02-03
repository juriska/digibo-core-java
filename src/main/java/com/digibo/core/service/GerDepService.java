package com.digibo.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * GerDepService - Service interface for BOGERDEP Oracle package
 * Handles German Deposit operations and related functionality
 */
public interface GerDepService {

    /**
     * Find new German deposit orders
     * @return List of new German deposit orders
     */
    List<Map<String, Object>> findNew();

    /**
     * Find German deposit orders by filters
     * @param docId Document ID
     * @param custId Customer ID
     * @param custName Customer name
     * @param idDocNo ID document number
     * @param login User login
     * @param status Status
     * @param orderDateFrom Order date from
     * @param orderDateTo Order date to
     * @return List of matching German deposit orders
     */
    List<Map<String, Object>> findByFilter(String docId, String custId, String custName,
                                            String idDocNo, String login, String status,
                                            Date orderDateFrom, Date orderDateTo);

    /**
     * Select customer for German deposit
     * @param custId Customer ID
     * @return Map containing rv (result code) and customers list
     */
    Map<String, Object> selectCustomer(String custId);

    /**
     * Bind German deposit order to customer
     * @param docId Document ID
     * @param custId Customer ID
     * @return Map containing success status and IDs
     */
    Map<String, Object> bindToCustomer(String docId, String custId);

    /**
     * Check if account exists for German deposit order
     * @param docId Document ID
     * @return Map containing documentId, accountExists flag, and exists boolean
     */
    Map<String, Object> accountExists(String docId);

    /**
     * Create user for German deposit
     * @param docId Document ID
     * @param tanCardId TAN card ID
     * @return Map containing success status and IDs
     */
    Map<String, Object> createUser(String docId, String tanCardId);

    /**
     * Reject German deposit order
     * @param docId Document ID
     * @param reason Rejection reason
     * @return Map containing success status and details
     */
    Map<String, Object> reject(String docId, String reason);
}

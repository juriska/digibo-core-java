package com.digibo.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * MarginService - Service interface for BOMargin Oracle package
 * Handles margin trading order operations
 */
public interface MarginService {

    /**
     * Find margin orders by filters
     * @param custId Customer ID
     * @param custName Customer name
     * @param userLogin User login
     * @param userPassword User password
     * @param docClass Document class
     * @param rateFrom Rate from
     * @param rateTill Rate till
     * @param orderCCY Order currency
     * @param contraryCCY Contrary currency
     * @param expiryFrom Expiry date from
     * @param expiryTill Expiry date till
     * @param docId Document ID
     * @param statuses Comma-separated status IDs
     * @param createdFrom Created date from
     * @param createdTill Created date till
     * @return List of matching margin orders
     */
    List<Map<String, Object>> find(String custId, String custName, String userLogin, String userPassword,
                                    String docClass, Double rateFrom, Double rateTill,
                                    String orderCCY, String contraryCCY,
                                    Date expiryFrom, Date expiryTill,
                                    String docId, String statuses,
                                    Date createdFrom, Date createdTill);

    /**
     * Get user's margin orders
     * @param docClass Document class
     * @return List of margin orders
     */
    List<Map<String, Object>> findMy(String docClass);

    /**
     * Get detailed margin order information
     * @param orderId Order ID
     * @return Map containing all order details
     */
    Map<String, Object> margin(String orderId);
}

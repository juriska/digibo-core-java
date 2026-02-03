package com.digibo.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * SmsDocService - Service interface for BOSMSDocument Oracle package
 * Handles SMS service registration document operations
 */
public interface SmsDocService {

    /**
     * Find SMS documents by filters
     * @param custId Customer ID
     * @param custName Customer name
     * @param userLogin User login
     * @param officerId Officer ID
     * @param pType Document type
     * @param mobile Mobile number
     * @param docId Document ID
     * @param statuses Status filter
     * @param createdFrom Created from date
     * @param createdTill Created till date
     * @return Array of matching SMS documents
     */
    List<Map<String, Object>> find(String custId, String custName, String userLogin, Long officerId,
                                    String pType, String mobile, String docId, String statuses,
                                    Date createdFrom, Date createdTill);

    /**
     * Get detailed SMS document information
     * @param docId Document ID
     * @return Detailed SMS document info
     */
    Map<String, Object> sms(String docId);

    /**
     * Check if phone number already exists
     * @param phone Phone number
     * @return Count of existing registrations
     */
    int alreadyExists(String phone);

    /**
     * Update/process SMS document
     * @param docId Document ID
     * @param reason Reason for status change
     * @param newStatus New status ID
     * @param messageId Message ID for audit
     */
    void updateDocument(String docId, String reason, Integer newStatus, Long messageId);
}

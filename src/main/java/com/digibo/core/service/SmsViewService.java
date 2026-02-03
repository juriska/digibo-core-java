package com.digibo.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * SmsViewService - Service interface for bosmsview Oracle package
 * Handles SMS message monitoring and viewing operations
 */
public interface SmsViewService {

    /**
     * Get SMS message types
     * @return Array of message types
     */
    List<Map<String, Object>> getTypes();

    /**
     * Find SMS messages by filters
     * @param custId Customer ID
     * @param custName Customer name
     * @param userLogin User login
     * @param officerId Officer ID
     * @param pType Message type
     * @param mobile Mobile number
     * @param text Text search
     * @param statuses Status filter
     * @param createdFrom Created from date
     * @param createdTill Created till date
     * @return Array of matching SMS messages
     */
    List<Map<String, Object>> find(String custId, String custName, String userLogin, Long officerId,
                                    String pType, String mobile, String text, String statuses,
                                    Date createdFrom, Date createdTill);

    /**
     * Get detailed SMS message information
     * @param messageId Message ID
     * @return Detailed SMS message info
     */
    Map<String, Object> sms(String messageId);
}

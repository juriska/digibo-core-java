package com.digibo.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * MessageService - Service interface for BOMessage Oracle package
 * Handles customer messages and communication
 */
public interface MessageService {

    /**
     * Find messages by various search criteria
     * @param userId User ID
     * @param userName User name
     * @param login Login
     * @param officerId Officer ID
     * @param msgId Message ID
     * @param message Message text
     * @param type Message type
     * @param custId Customer ID
     * @param custName Customer name
     * @param statuses Comma-separated status IDs
     * @param classId Class ID
     * @param dateFrom Date from
     * @param dateTill Date till
     * @param channelId Channel ID
     * @return List of messages
     */
    List<Map<String, Object>> findMessages(String userId, String userName, String login,
                                            Long officerId, String msgId, String message,
                                            String type, String custId, String custName,
                                            String statuses, Long classId,
                                            Date dateFrom, Date dateTill, String channelId);

    /**
     * Find current messages for logged in officer
     * @param classes Comma-separated class IDs (optional)
     * @return List of current messages
     */
    List<Map<String, Object>> findCurrent(String classes);

    /**
     * Load user data for a message
     * @param wocId Way of connection ID
     * @param msgId Message ID (optional)
     * @return Map containing userName, login, fromCust, custId, and customers list
     */
    Map<String, Object> loadUserData(Long wocId, String msgId);

    /**
     * Load communication history for a way of connection
     * @param wocId Way of connection ID
     * @return List of messages
     */
    List<Map<String, Object>> loadCommunication(Long wocId);

    /**
     * Set lock on a message
     * @param lockName Lock name
     * @param id Message ID
     * @return Map containing success, lockStatus, id, officerName, officerPhone, message
     */
    Map<String, Object> setLock(String lockName, String id);

    /**
     * Mark message as seen
     * @param id Message ID
     * @return Map containing success, messageId, message
     */
    Map<String, Object> setSeen(Long id);

    /**
     * Answer a message
     * @param id Message ID
     * @param wocId Way of connection ID
     * @param status New status
     * @param classId Class ID
     * @param message Answer message text
     * @return Map containing success, messageId, message
     */
    Map<String, Object> answer(Long id, Long wocId, Integer status, Long classId, String message);

    /**
     * Forward a message to another class
     * @param id Message ID
     * @param classId New class ID
     * @return Map containing success, messageId, newClassId, message
     */
    Map<String, Object> forward(Long id, Long classId);
}

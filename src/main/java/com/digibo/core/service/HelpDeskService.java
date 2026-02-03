package com.digibo.core.service;

import java.util.List;
import java.util.Map;

/**
 * HelpDeskService - Service interface for BOHelpDesk Oracle package
 * Handles user channel management, authentication, and password operations
 */
public interface HelpDeskService {

    /**
     * Find user channels by search criteria
     * @param login User login
     * @param authDev Authentication device
     * @param userName User name
     * @param personalId Personal ID
     * @return List of user channels
     */
    List<Map<String, Object>> findUserChannel(String login, String authDev, String userName, String personalId);

    /**
     * Load log entries for a user
     * @param userId User ID
     * @param wocId WOC ID (optional)
     * @return List of log entries
     */
    List<Map<String, Object>> loadLog(String userId, String wocId);

    /**
     * Set password for a user channel
     * @param channelId Channel ID
     * @param userId User ID
     * @param password New password
     * @return Result code (0 = success)
     */
    int setPassword(String channelId, String userId, String password);

    /**
     * Load user channel details
     * @param id Channel ID
     * @return Map containing channelData list and userInfo map
     */
    Map<String, Object> loadUserChannel(Long id);

    /**
     * Load authentication info for a user channel
     * @param id Channel ID
     * @return Map containing id, stdQ, specQ, and answer
     */
    Map<String, Object> loadAuthInfo(Long id);

    /**
     * Set lock status for a user channel
     * @param channelId Channel ID
     * @param userId User ID
     * @param status New status
     * @param subStatus New sub-status
     * @return true if successful
     */
    boolean setLock(String channelId, String userId, Integer status, Integer subStatus);

    /**
     * Reset stolen channel
     * @param channelId Channel ID
     * @return true if successful
     */
    boolean resetStolen(String channelId);
}

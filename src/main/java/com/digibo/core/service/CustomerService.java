package com.digibo.core.service;

import com.digibo.core.dto.response.ChannelResponse;
import com.digibo.core.dto.response.UserResponse;

import java.util.List;
import java.util.Map;

/**
 * CustomerService - Service interface for BOCustomer Oracle package
 * Handles customer and user operations
 */
public interface CustomerService {

    /**
     * Check if customer exists
     * @param id Customer ID
     * @return 1 if exists, 0 otherwise
     */
    int customerExists(String id);

    /**
     * Load user channels
     * @param id User ID
     * @return List of user channels
     */
    List<Map<String, Object>> loadUserChannels(String id);

    /**
     * Load user info
     * @param id User ID
     * @return List of user info records
     */
    List<Map<String, Object>> loadUserInfo(Long id);

    /**
     * Load user history
     * @param id User ID
     * @return List of user history records
     */
    List<Map<String, Object>> loadUserHistory(Long id);

    /**
     * Load customer tree
     * @param custId Customer ID
     * @param location Location code
     * @return List of customer tree nodes
     */
    List<Map<String, Object>> loadCustomerTree(String custId, String location);

    /**
     * Load licenses for customer
     * @param custId Customer ID
     * @return List of licenses
     */
    List<Map<String, Object>> loadLicenses(String custId);

    /**
     * Check if license is valid
     * @param id License ID
     * @return 1 if valid, 0 otherwise
     */
    int checkLicense(String id);

    /**
     * Check login validity
     * @param userId User ID
     * @param login Login name
     * @param license License ID
     * @param channelId Channel ID
     * @return Check result code (0 = success)
     */
    int checkLogin(Long userId, String login, String license, Long channelId);

    /**
     * Load users by criteria
     * @param custId Customer ID
     * @param channel Channel ID
     * @param license License ID
     * @param location Location code
     * @return List of users
     */
    List<Map<String, Object>> loadUsers(String custId, Long channel, String license, String location);

    /**
     * Load user details by ID
     * @param id User ID
     * @return User details
     */
    UserResponse loadUser(Long id);

    /**
     * Load channel information
     * @param wocId WOC ID
     * @param custId Customer ID
     * @return Channel information
     */
    ChannelResponse loadChannel(String wocId, String custId);
}

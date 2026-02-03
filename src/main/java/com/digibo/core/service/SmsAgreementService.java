package com.digibo.core.service;

import java.util.List;
import java.util.Map;

/**
 * SmsAgreementService - Service interface for BOSMSAgreement Oracle package
 * Handles SMS agreement operations for web online channels
 */
public interface SmsAgreementService {

    /**
     * Get list of operators
     * @return Array of operators
     */
    List<Map<String, Object>> getOperators();

    /**
     * Get accounts for customer
     * @param custId Customer ID
     * @param location Location code
     * @return Array of accounts
     */
    List<Map<String, Object>> getAccounts(String custId, String location);

    /**
     * Get logins for user
     * @param userId User ID
     * @param custId Customer ID
     * @param location Location code
     * @return Array of logins
     */
    List<Map<String, Object>> getLogins(Long userId, Long custId, String location);

    /**
     * Load rights level 1 for channel
     * @param wocId Web online channel ID
     * @param custId Customer ID
     * @param location Location code
     * @return Array of level 1 rights
     */
    List<Map<String, Object>> loadRights1(Long wocId, Long custId, String location);

    /**
     * Load rights level 2 for channel
     * @param wocId Web online channel ID
     * @param custId Customer ID
     * @param location Location code
     * @return Array of level 2 rights
     */
    List<Map<String, Object>> loadRights2(Long wocId, Long custId, String location);

    /**
     * Load card rights for channel
     * @param wocId Web online channel ID
     * @param custId Customer ID
     * @param location Location code
     * @return Array of card rights
     */
    List<Map<String, Object>> loadCardRights(Long wocId, Long custId, String location);

    /**
     * Check if login is valid
     * @param login Login to check
     * @return Result code (0 = valid, non-zero = invalid)
     */
    int checkLogin(String login);

    /**
     * Get login count
     * @param login Login to count
     * @return Number of logins
     */
    int getLoginCount(String login);

    /**
     * Check if login exists for customer
     * @param wocId Web online channel ID
     * @param custId Customer ID
     * @param location Location code
     * @param login Login to check
     * @return 1 if exists, 0 if not
     */
    int loginForCustomerExists(Long wocId, Long custId, String location, String login);

    /**
     * Load channel information
     * @param wocId Web online channel ID
     * @return Channel information with multiple OUT parameters
     */
    Map<String, Object> loadChannel(String wocId);
}

package com.digibo.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * FindUsersService - Service interface for BOFindUsers Oracle package
 * Handles user search operations
 */
public interface FindUsersService {

    /**
     * Find users by various search criteria
     *
     * @param userId User ID
     * @param globusUserId Globus user ID
     * @param userName User name
     * @param personalId Personal ID
     * @param officerId Officer ID
     * @param phone Phone number
     * @param fax Fax number
     * @param email Email address
     * @param channelId Channel ID
     * @param login Login name
     * @param cDevNum Device number
     * @param channel Channel
     * @param custId Customer ID
     * @param custResidence Customer residence
     * @param custType Customer type
     * @param dateFrom Date from
     * @param dateTill Date till
     * @param status Status
     * @return List of users with userId, userName, personalId, passportNo, issuerCountry,
     *         country, phone, mobile, fax, email, regDate
     */
    List<Map<String, Object>> findUsers(
            String userId,
            String globusUserId,
            String userName,
            String personalId,
            Long officerId,
            String phone,
            String fax,
            String email,
            String channelId,
            String login,
            String cDevNum,
            Long channel,
            String custId,
            String custResidence,
            String custType,
            Date dateFrom,
            Date dateTill,
            Integer status
    );
}

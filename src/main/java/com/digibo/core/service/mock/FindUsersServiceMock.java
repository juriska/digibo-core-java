package com.digibo.core.service.mock;

import com.digibo.core.service.FindUsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * FindUsersServiceMock - Mock implementation of FindUsersService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class FindUsersServiceMock implements FindUsersService {

    private static final Logger logger = LoggerFactory.getLogger(FindUsersServiceMock.class);

    @Override
    public List<Map<String, Object>> findUsers(
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
    ) {
        logger.debug("[MOCK] findUsers()");

        List<Map<String, Object>> results = new ArrayList<>();

        Map<String, Object> user1 = new HashMap<>();
        user1.put("USER_ID", "USR001");
        user1.put("USER_NAME", "John Doe");
        user1.put("PERSONAL_ID", "123456789");
        user1.put("PASSPORT_NO", "AB123456");
        user1.put("ISSUER_COUNTRY", "US");
        user1.put("COUNTRY", "US");
        user1.put("PHONE", "+1-555-1234");
        user1.put("MOBILE", "+1-555-5678");
        user1.put("FAX", "+1-555-9999");
        user1.put("EMAIL", "john.doe@example.com");
        user1.put("REG_DATE", new Date(System.currentTimeMillis() - 365L * 24 * 60 * 60 * 1000));
        user1.put("STATUS", 1);
        user1.put("CHANNEL_ID", "CH001");
        user1.put("LOGIN", "jdoe");
        user1.put("CUST_ID", "CUST001");
        results.add(user1);

        Map<String, Object> user2 = new HashMap<>();
        user2.put("USER_ID", "USR002");
        user2.put("USER_NAME", "Jane Smith");
        user2.put("PERSONAL_ID", "987654321");
        user2.put("PASSPORT_NO", "CD789012");
        user2.put("ISSUER_COUNTRY", "US");
        user2.put("COUNTRY", "US");
        user2.put("PHONE", "+1-555-2345");
        user2.put("MOBILE", "+1-555-6789");
        user2.put("FAX", "+1-555-8888");
        user2.put("EMAIL", "jane.smith@example.com");
        user2.put("REG_DATE", new Date(System.currentTimeMillis() - 180L * 24 * 60 * 60 * 1000));
        user2.put("STATUS", 1);
        user2.put("CHANNEL_ID", "CH002");
        user2.put("LOGIN", "jsmith");
        user2.put("CUST_ID", "CUST002");
        results.add(user2);

        return results;
    }
}

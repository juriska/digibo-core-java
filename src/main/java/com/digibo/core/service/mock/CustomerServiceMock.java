package com.digibo.core.service.mock;

import com.digibo.core.dto.response.ChannelResponse;
import com.digibo.core.dto.response.UserResponse;
import com.digibo.core.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * CustomerServiceMock - Mock implementation of CustomerService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class CustomerServiceMock implements CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceMock.class);

    @Override
    public int customerExists(String id) {
        logger.debug("[MOCK] customerExists({})", id);
        return "CUST001".equals(id) || "CUST002".equals(id) ? 1 : 0;
    }

    @Override
    public List<Map<String, Object>> loadUserChannels(String id) {
        logger.debug("[MOCK] loadUserChannels({})", id);
        List<Map<String, Object>> channels = new ArrayList<>();

        Map<String, Object> channel1 = new HashMap<>();
        channel1.put("CHANNEL_ID", 1L);
        channel1.put("CHANNEL_NAME", "Web Banking");
        channel1.put("STATUS", "ACTIVE");
        channels.add(channel1);

        Map<String, Object> channel2 = new HashMap<>();
        channel2.put("CHANNEL_ID", 2L);
        channel2.put("CHANNEL_NAME", "Mobile Banking");
        channel2.put("STATUS", "ACTIVE");
        channels.add(channel2);

        return channels;
    }

    @Override
    public List<Map<String, Object>> loadUserInfo(Long id) {
        logger.debug("[MOCK] loadUserInfo({})", id);
        List<Map<String, Object>> infoList = new ArrayList<>();

        Map<String, Object> info = new HashMap<>();
        info.put("USER_ID", id);
        info.put("USER_NAME", "John Doe");
        info.put("EMAIL", "john.doe@example.com");
        info.put("PHONE", "+1234567890");
        infoList.add(info);

        return infoList;
    }

    @Override
    public List<Map<String, Object>> loadUserHistory(Long id) {
        logger.debug("[MOCK] loadUserHistory({})", id);
        List<Map<String, Object>> history = new ArrayList<>();

        Map<String, Object> entry1 = new HashMap<>();
        entry1.put("ACTION", "LOGIN");
        entry1.put("TIMESTAMP", new Date());
        entry1.put("IP_ADDRESS", "192.168.1.1");
        history.add(entry1);

        Map<String, Object> entry2 = new HashMap<>();
        entry2.put("ACTION", "PASSWORD_CHANGE");
        entry2.put("TIMESTAMP", new Date(System.currentTimeMillis() - 86400000));
        entry2.put("IP_ADDRESS", "192.168.1.2");
        history.add(entry2);

        return history;
    }

    @Override
    public List<Map<String, Object>> loadCustomerTree(String custId, String location) {
        logger.debug("[MOCK] loadCustomerTree({}, {})", custId, location);
        List<Map<String, Object>> tree = new ArrayList<>();

        Map<String, Object> node1 = new HashMap<>();
        node1.put("NODE_ID", 1L);
        node1.put("NODE_NAME", "Main Office");
        node1.put("PARENT_ID", null);
        node1.put("LEVEL", 0);
        tree.add(node1);

        Map<String, Object> node2 = new HashMap<>();
        node2.put("NODE_ID", 2L);
        node2.put("NODE_NAME", "Branch A");
        node2.put("PARENT_ID", 1L);
        node2.put("LEVEL", 1);
        tree.add(node2);

        return tree;
    }

    @Override
    public List<Map<String, Object>> loadLicenses(String custId) {
        logger.debug("[MOCK] loadLicenses({})", custId);
        List<Map<String, Object>> licenses = new ArrayList<>();

        Map<String, Object> license1 = new HashMap<>();
        license1.put("LICENSE_ID", "LIC001");
        license1.put("LICENSE_TYPE", "PREMIUM");
        license1.put("VALID_FROM", new Date());
        license1.put("VALID_TO", new Date(System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000));
        licenses.add(license1);

        return licenses;
    }

    @Override
    public int checkLicense(String id) {
        logger.debug("[MOCK] checkLicense({})", id);
        return "LIC001".equals(id) ? 1 : 0;
    }

    @Override
    public int checkLogin(Long userId, String login, String license, Long channelId) {
        logger.debug("[MOCK] checkLogin({}, {}, {}, {})", userId, login, license, channelId);
        // 0 = success, 1 = user not found, 2 = invalid channel, 3 = invalid license
        if (userId == null || userId <= 0) return 1;
        if (channelId == null || channelId > 10) return 2;
        if (license == null || !license.startsWith("LIC")) return 3;
        return 0;
    }

    @Override
    public List<Map<String, Object>> loadUsers(String custId, Long channel, String license, String location) {
        logger.debug("[MOCK] loadUsers({}, {}, {}, {})", custId, channel, license, location);
        List<Map<String, Object>> users = new ArrayList<>();

        Map<String, Object> user1 = new HashMap<>();
        user1.put("USER_ID", 1001L);
        user1.put("USER_NAME", "John Doe");
        user1.put("LOGIN", "jdoe");
        user1.put("EMAIL", "john.doe@example.com");
        users.add(user1);

        Map<String, Object> user2 = new HashMap<>();
        user2.put("USER_ID", 1002L);
        user2.put("USER_NAME", "Jane Smith");
        user2.put("LOGIN", "jsmith");
        user2.put("EMAIL", "jane.smith@example.com");
        users.add(user2);

        return users;
    }

    @Override
    public UserResponse loadUser(Long id) {
        logger.debug("[MOCK] loadUser({})", id);
        return UserResponse.builder()
                .id(id)
                .name("John Doe")
                .issuerCountry("US")
                .personalId("123456789")
                .passportNo("AB123456")
                .street("123 Main St")
                .city("New York")
                .country("US")
                .zip("10001")
                .phone("+1-555-1234")
                .mobile("+1-555-5678")
                .fax("+1-555-9999")
                .email("john.doe@example.com")
                .apart("4A")
                .house("12B")
                .stdQ(1)
                .specQ("What is your pet's name?")
                .answer("Fluffy")
                .regDate(new Date(System.currentTimeMillis() - 365L * 24 * 60 * 60 * 1000))
                .changeDate(new Date())
                .changeOfficerId("OFF001")
                .changeLogin("admin")
                .customerId(1001L)
                .migrStatus(0)
                .hasAgreementInGlobus(1)
                .build();
    }

    @Override
    public ChannelResponse loadChannel(String wocId, String custId) {
        logger.debug("[MOCK] loadChannel({}, {})", wocId, custId);
        return ChannelResponse.builder()
                .wocId(wocId)
                .custId(custId)
                .cdevType(1)
                .cdevNum("DEV001")
                .sellerId(100L)
                .distribCenterId(200L)
                .level(1)
                .tmpLevel(0)
                .changeOfficer("OFF001")
                .specRate(5)
                .info2Bank(1)
                .dfAccessRight(3)
                .build();
    }
}

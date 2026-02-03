package com.digibo.core.service.mock;

import com.digibo.core.service.HelpDeskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * HelpDeskServiceMock - Mock implementation of HelpDeskService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class HelpDeskServiceMock implements HelpDeskService {

    private static final Logger logger = LoggerFactory.getLogger(HelpDeskServiceMock.class);

    private final List<Map<String, Object>> mockUserChannels = new ArrayList<>();

    public HelpDeskServiceMock() {
        initMockData();
    }

    private void initMockData() {
        Map<String, Object> channel1 = new HashMap<>();
        channel1.put("CHANNEL_ID", 1001L);
        channel1.put("LOGIN", "jdoe");
        channel1.put("AUTH_DEV", "Digipass");
        channel1.put("USER_NAME", "John Doe");
        channel1.put("PERSONAL_ID", "123456-12345");
        channel1.put("STATUS", 1);
        channel1.put("SUB_STATUS", 0);
        mockUserChannels.add(channel1);

        Map<String, Object> channel2 = new HashMap<>();
        channel2.put("CHANNEL_ID", 1002L);
        channel2.put("LOGIN", "jsmith");
        channel2.put("AUTH_DEV", "SmartID");
        channel2.put("USER_NAME", "Jane Smith");
        channel2.put("PERSONAL_ID", "654321-54321");
        channel2.put("STATUS", 1);
        channel2.put("SUB_STATUS", 0);
        mockUserChannels.add(channel2);
    }

    @Override
    public List<Map<String, Object>> findUserChannel(String login, String authDev, String userName, String personalId) {
        logger.debug("[MOCK] findUserChannel({}, {}, {}, {})", login, authDev, userName, personalId);

        return mockUserChannels.stream()
                .filter(ch -> login == null || ((String) ch.get("LOGIN")).toLowerCase().contains(login.toLowerCase()))
                .filter(ch -> authDev == null || ((String) ch.get("AUTH_DEV")).toLowerCase().contains(authDev.toLowerCase()))
                .filter(ch -> userName == null || ((String) ch.get("USER_NAME")).toLowerCase().contains(userName.toLowerCase()))
                .filter(ch -> personalId == null || personalId.equals(ch.get("PERSONAL_ID")))
                .toList();
    }

    @Override
    public List<Map<String, Object>> loadLog(String userId, String wocId) {
        logger.debug("[MOCK] loadLog({}, {})", userId, wocId);

        List<Map<String, Object>> logs = new ArrayList<>();

        Map<String, Object> log1 = new HashMap<>();
        log1.put("LOG_ID", 1L);
        log1.put("USER_ID", userId);
        log1.put("WOC_ID", wocId);
        log1.put("ACTION", "LOGIN");
        log1.put("TIMESTAMP", new Date());
        log1.put("IP_ADDRESS", "192.168.1.100");
        logs.add(log1);

        Map<String, Object> log2 = new HashMap<>();
        log2.put("LOG_ID", 2L);
        log2.put("USER_ID", userId);
        log2.put("WOC_ID", wocId);
        log2.put("ACTION", "PASSWORD_CHANGE");
        log2.put("TIMESTAMP", new Date(System.currentTimeMillis() - 3600000));
        log2.put("IP_ADDRESS", "192.168.1.101");
        logs.add(log2);

        return logs;
    }

    @Override
    public int setPassword(String channelId, String userId, String password) {
        logger.debug("[MOCK] setPassword({}, {})", channelId, userId);
        return 0; // Success
    }

    @Override
    public Map<String, Object> loadUserChannel(Long id) {
        logger.debug("[MOCK] loadUserChannel({})", id);

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", id);
        userInfo.put("login", "jdoe");
        userInfo.put("authDev", "Digipass");
        userInfo.put("status", 1);
        userInfo.put("subStatus", 0);
        userInfo.put("userId", "USR001");
        userInfo.put("userName", "John Doe");
        userInfo.put("personalId", "123456-12345");
        userInfo.put("phone", "+1-555-1234");
        userInfo.put("mobile", "+1-555-5678");
        userInfo.put("fax", "+1-555-9999");
        userInfo.put("email", "john.doe@example.com");
        userInfo.put("street", "123 Main St");
        userInfo.put("city", "New York");
        userInfo.put("country", "US");
        userInfo.put("zip", "10001");
        userInfo.put("regDate", new Date(System.currentTimeMillis() - 365L * 24 * 60 * 60 * 1000));
        userInfo.put("passwordChangeAllowed", 1);
        userInfo.put("hasCronto", 0);
        userInfo.put("password", null);

        List<Map<String, Object>> channelData = new ArrayList<>();
        Map<String, Object> data = new HashMap<>();
        data.put("CHANNEL_TYPE", "WEB");
        data.put("CHANNEL_STATUS", "ACTIVE");
        channelData.add(data);

        Map<String, Object> response = new HashMap<>();
        response.put("channelData", channelData);
        response.put("userInfo", userInfo);
        return response;
    }

    @Override
    public Map<String, Object> loadAuthInfo(Long id) {
        logger.debug("[MOCK] loadAuthInfo({})", id);

        Map<String, Object> response = new HashMap<>();
        response.put("id", id);
        response.put("stdQ", 1);
        response.put("specQ", "What is your pet's name?");
        response.put("answer", "Fluffy");
        return response;
    }

    @Override
    public boolean setLock(String channelId, String userId, Integer status, Integer subStatus) {
        logger.debug("[MOCK] setLock({}, {}, {}, {})", channelId, userId, status, subStatus);
        return true;
    }

    @Override
    public boolean resetStolen(String channelId) {
        logger.debug("[MOCK] resetStolen({})", channelId);
        return true;
    }
}

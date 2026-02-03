package com.digibo.core.service.mock;

import com.digibo.core.service.SMSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * SMSServiceMock - Mock implementation of SMSService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class SMSServiceMock implements SMSService {

    private static final Logger logger = LoggerFactory.getLogger(SMSServiceMock.class);

    @Override
    public Map<String, Object> loadUserData(Long wocId) {
        logger.debug("[MOCK] loadUserData({})", wocId);

        List<Map<String, Object>> details = new ArrayList<>();
        Map<String, Object> detail1 = new HashMap<>();
        detail1.put("DETAIL_ID", 1L);
        detail1.put("DETAIL_TYPE", "SMS_NOTIFICATION");
        detail1.put("ENABLED", 1);
        details.add(detail1);

        Map<String, Object> detail2 = new HashMap<>();
        detail2.put("DETAIL_ID", 2L);
        detail2.put("DETAIL_TYPE", "BALANCE_ALERT");
        detail2.put("ENABLED", 1);
        details.add(detail2);

        Map<String, Object> userData = new HashMap<>();
        userData.put("wocId", wocId);
        userData.put("userName", "Test User");
        userData.put("login", "testuser");
        userData.put("language", "EN");
        userData.put("active", 1);
        userData.put("accept", 1);
        userData.put("details", details);

        return userData;
    }
}

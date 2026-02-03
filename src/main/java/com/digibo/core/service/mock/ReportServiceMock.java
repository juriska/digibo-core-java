package com.digibo.core.service.mock;

import com.digibo.core.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * ReportServiceMock - Mock implementation of ReportService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class ReportServiceMock implements ReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportServiceMock.class);

    private static final List<Map<String, Object>> mockUnauthorizedConditions = new ArrayList<>();
    private static final List<Map<String, Object>> mockUnauthorizedUsers = new ArrayList<>();

    static {
        // Mock unauthorized conditions
        Map<String, Object> cond1 = new HashMap<>();
        cond1.put("CONDITION_ID", 1L);
        cond1.put("CONDITION_TYPE", "SPECIAL_RATE");
        cond1.put("CUSTOMER_ID", "CUST001");
        cond1.put("CUSTOMER_NAME", "Test Customer 1");
        cond1.put("CREATED_BY", "officer1");
        cond1.put("CREATED_DATE", new Date(System.currentTimeMillis() - 3L * 24 * 60 * 60 * 1000));
        cond1.put("STATUS", "PENDING");
        mockUnauthorizedConditions.add(cond1);

        Map<String, Object> cond2 = new HashMap<>();
        cond2.put("CONDITION_ID", 2L);
        cond2.put("CONDITION_TYPE", "LIMIT_INCREASE");
        cond2.put("CUSTOMER_ID", "CUST002");
        cond2.put("CUSTOMER_NAME", "Test Customer 2");
        cond2.put("CREATED_BY", "officer2");
        cond2.put("CREATED_DATE", new Date(System.currentTimeMillis() - 1L * 24 * 60 * 60 * 1000));
        cond2.put("STATUS", "PENDING");
        mockUnauthorizedConditions.add(cond2);

        // Mock unauthorized users
        Map<String, Object> user1 = new HashMap<>();
        user1.put("USER_ID", 1001L);
        user1.put("USER_NAME", "John Doe");
        user1.put("LOGIN", "jdoe");
        user1.put("CUSTOMER_ID", "CUST001");
        user1.put("CUSTOMER_NAME", "Test Customer 1");
        user1.put("CREATED_BY", "officer1");
        user1.put("CREATED_DATE", new Date(System.currentTimeMillis() - 2L * 24 * 60 * 60 * 1000));
        user1.put("STATUS", "PENDING_AUTHORIZATION");
        mockUnauthorizedUsers.add(user1);

        Map<String, Object> user2 = new HashMap<>();
        user2.put("USER_ID", 1002L);
        user2.put("USER_NAME", "Jane Smith");
        user2.put("LOGIN", "jsmith");
        user2.put("CUSTOMER_ID", "CUST002");
        user2.put("CUSTOMER_NAME", "Test Customer 2");
        user2.put("CREATED_BY", "officer2");
        user2.put("CREATED_DATE", new Date(System.currentTimeMillis() - 5L * 24 * 60 * 60 * 1000));
        user2.put("STATUS", "PENDING_AUTHORIZATION");
        mockUnauthorizedUsers.add(user2);
    }

    @Override
    public List<Map<String, Object>> unauthorizedConditions() {
        logger.debug("[MOCK] BOReport.unauthorizedConditions() called");
        logger.debug("[MOCK] Returning {} unauthorized conditions", mockUnauthorizedConditions.size());
        return new ArrayList<>(mockUnauthorizedConditions);
    }

    @Override
    public List<Map<String, Object>> unauthorizedUsers() {
        logger.debug("[MOCK] BOReport.unauthorizedUsers() called");
        logger.debug("[MOCK] Returning {} unauthorized users", mockUnauthorizedUsers.size());
        return new ArrayList<>(mockUnauthorizedUsers);
    }
}

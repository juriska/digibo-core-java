package com.digibo.core.service.mock;

import com.digibo.core.service.InsuranceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * InsuranceServiceMock - Mock implementation of InsuranceService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class InsuranceServiceMock implements InsuranceService {

    private static final Logger logger = LoggerFactory.getLogger(InsuranceServiceMock.class);

    private final List<Map<String, Object>> mockOrders = new ArrayList<>();

    public InsuranceServiceMock() {
        initMockData();
    }

    private void initMockData() {
        Map<String, Object> order1 = new HashMap<>();
        order1.put("ID", "INS001");
        order1.put("GLB_CUST_ID", "CUST001");
        order1.put("CUST_NAME", "John Doe");
        order1.put("LOGIN", "jdoe");
        order1.put("OFFICER_ID", 100L);
        order1.put("CLASS_ID", "LIFE");
        order1.put("STATUS_ID", 1);
        order1.put("ORDER_DATE", new Date());
        order1.put("CREATOR_CHANNEL_ID", 5);
        order1.put("FROM_LOCATION", "LV");
        mockOrders.add(order1);

        Map<String, Object> order2 = new HashMap<>();
        order2.put("ID", "INS002");
        order2.put("GLB_CUST_ID", "CUST002");
        order2.put("CUST_NAME", "Jane Smith");
        order2.put("LOGIN", "jsmith");
        order2.put("OFFICER_ID", 101L);
        order2.put("CLASS_ID", "HEALTH");
        order2.put("STATUS_ID", 2);
        order2.put("ORDER_DATE", new Date());
        order2.put("CREATOR_CHANNEL_ID", 3);
        order2.put("FROM_LOCATION", "EE");
        mockOrders.add(order2);
    }

    @Override
    public List<Map<String, Object>> find(String custId, String custName, String userLogin,
                                           Long officerId, String docClass, String docId,
                                           String channels, String statuses,
                                           Date createdFrom, Date createdTill, String fromLocation) {
        logger.debug("[MOCK] find()");

        return mockOrders.stream()
                .filter(o -> custId == null || custId.equals(o.get("GLB_CUST_ID")))
                .filter(o -> custName == null || ((String) o.get("CUST_NAME")).toLowerCase().contains(custName.toLowerCase()))
                .filter(o -> userLogin == null || ((String) o.get("LOGIN")).toLowerCase().contains(userLogin.toLowerCase()))
                .filter(o -> officerId == null || officerId.equals(o.get("OFFICER_ID")))
                .filter(o -> docClass == null || docClass.equals(o.get("CLASS_ID")))
                .filter(o -> docId == null || docId.equals(o.get("ID")))
                .filter(o -> fromLocation == null || fromLocation.equals(o.get("FROM_LOCATION")))
                .toList();
    }

    @Override
    public List<Map<String, Object>> findMy() {
        logger.debug("[MOCK] findMy()");
        return new ArrayList<>(mockOrders);
    }
}

package com.digibo.core.service.mock;

import com.digibo.core.service.LifeAndPensionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * LifeAndPensionServiceMock - Mock implementation of LifeAndPensionService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class LifeAndPensionServiceMock implements LifeAndPensionService {

    private static final Logger logger = LoggerFactory.getLogger(LifeAndPensionServiceMock.class);

    private final List<Map<String, Object>> mockOrders = new ArrayList<>();

    public LifeAndPensionServiceMock() {
        initMockData();
    }

    private void initMockData() {
        Map<String, Object> order1 = new HashMap<>();
        order1.put("PID", "LP001");
        order1.put("CUSTID", "CUST001");
        order1.put("LOGIN", "jdoe");
        order1.put("STATUS", 60);
        order1.put("CLASS_ID", 30);
        order1.put("CREATED", new Date());
        order1.put("PROCESSED_BY", null);
        mockOrders.add(order1);

        Map<String, Object> order2 = new HashMap<>();
        order2.put("PID", "LP002");
        order2.put("CUSTID", "CUST002");
        order2.put("LOGIN", "jsmith");
        order2.put("STATUS", 61);
        order2.put("CLASS_ID", 31);
        order2.put("CREATED", new Date());
        order2.put("PROCESSED_BY", 1001L);
        mockOrders.add(order2);
    }

    @Override
    public List<Map<String, Object>> find(String custId, String custName, String userLogin,
                                           String docId, String statuses, String docClass,
                                           Date createdFrom, Date createdTill) {
        logger.debug("[MOCK] find()");

        return mockOrders.stream()
                .filter(o -> custId == null || custId.equals(o.get("CUSTID")))
                .filter(o -> userLogin == null || ((String) o.get("LOGIN")).toLowerCase().contains(userLogin.toLowerCase()))
                .filter(o -> docId == null || docId.equals(o.get("PID")))
                .toList();
    }

    @Override
    public List<Map<String, Object>> findMy(Long officerId) {
        logger.debug("[MOCK] findMy({})", officerId);

        if (officerId == null || officerId == 0L) {
            return mockOrders.stream()
                    .filter(o -> o.get("PROCESSED_BY") == null)
                    .toList();
        } else {
            return mockOrders.stream()
                    .filter(o -> officerId.equals(o.get("PROCESSED_BY")))
                    .toList();
        }
    }

    @Override
    public Map<String, Object> setProcessing(String orderId) {
        logger.debug("[MOCK] setProcessing({})", orderId);

        Optional<Map<String, Object>> orderOpt = mockOrders.stream()
                .filter(o -> orderId.equals(o.get("PID")))
                .findFirst();

        Map<String, Object> response = new HashMap<>();
        if (orderOpt.isPresent()) {
            Map<String, Object> order = orderOpt.get();
            order.put("PROCESSED_BY", 1001L);
            order.put("STATUS", 61);
            response.put("success", true);
            response.put("orderId", orderId);
            response.put("result", 1001);
        } else {
            response.put("success", false);
            response.put("orderId", orderId);
            response.put("result", 0);
        }
        return response;
    }
}

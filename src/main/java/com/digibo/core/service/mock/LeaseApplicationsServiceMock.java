package com.digibo.core.service.mock;

import com.digibo.core.service.LeaseApplicationsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * LeaseApplicationsServiceMock - Mock implementation of LeaseApplicationsService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class LeaseApplicationsServiceMock implements LeaseApplicationsService {

    private static final Logger logger = LoggerFactory.getLogger(LeaseApplicationsServiceMock.class);

    private final List<Map<String, Object>> mockOrders = new ArrayList<>();

    public LeaseApplicationsServiceMock() {
        initMockData();
    }

    private void initMockData() {
        Map<String, Object> order1 = new HashMap<>();
        order1.put("PID", "LA001");
        order1.put("CUST_ID", "CUST001");
        order1.put("CUST_NAME", "John Doe");
        order1.put("LOGIN", "jdoe");
        order1.put("STATUS", 5);
        order1.put("CLASS_ID", 10);
        order1.put("CREATED", new Date());
        order1.put("PROCESSEDBY", null);
        mockOrders.add(order1);

        Map<String, Object> order2 = new HashMap<>();
        order2.put("PID", "LA002");
        order2.put("CUST_ID", "CUST002");
        order2.put("CUST_NAME", "Jane Smith");
        order2.put("LOGIN", "jsmith");
        order2.put("STATUS", 3);
        order2.put("CLASS_ID", 11);
        order2.put("CREATED", new Date());
        order2.put("PROCESSEDBY", 9999L);
        mockOrders.add(order2);

        Map<String, Object> order3 = new HashMap<>();
        order3.put("PID", "LA003");
        order3.put("CUST_ID", "CUST003");
        order3.put("CUST_NAME", "Bob Wilson");
        order3.put("LOGIN", "bwilson");
        order3.put("STATUS", 13);
        order3.put("CLASS_ID", 10);
        order3.put("CREATED", new Date(System.currentTimeMillis() - 86400000));
        order3.put("PROCESSEDBY", null);
        mockOrders.add(order3);
    }

    @Override
    public List<Map<String, Object>> find(String custId, String custName, String userLogin,
                                           String docId, String statuses, String docClass,
                                           Date createdFrom, Date createdTill) {
        logger.debug("[MOCK] find()");

        return mockOrders.stream()
                .filter(o -> custId == null || custId.equals(o.get("CUST_ID")))
                .filter(o -> userLogin == null || ((String) o.get("LOGIN")).toLowerCase().contains(userLogin.toLowerCase()))
                .filter(o -> docId == null || docId.equals(o.get("PID")))
                .toList();
    }

    @Override
    public List<Map<String, Object>> findMy(Long officerId) {
        logger.debug("[MOCK] findMy({})", officerId);

        if (officerId == null || officerId == 0L) {
            // Return new/unassigned orders
            return mockOrders.stream()
                    .filter(o -> {
                        Integer status = (Integer) o.get("STATUS");
                        return status == 5 || status == 13;
                    })
                    .toList();
        } else {
            // Return orders assigned to this officer
            return mockOrders.stream()
                    .filter(o -> officerId.equals(o.get("PROCESSEDBY")) && Integer.valueOf(3).equals(o.get("STATUS")))
                    .toList();
        }
    }

    @Override
    public Map<String, Object> setProcessing(String docId, Long statusIdFrom) {
        logger.debug("[MOCK] setProcessing({}, {})", docId, statusIdFrom);

        Optional<Map<String, Object>> docOpt = mockOrders.stream()
                .filter(o -> docId.equals(o.get("PID")))
                .findFirst();

        Map<String, Object> response = new HashMap<>();
        if (docOpt.isPresent()) {
            Map<String, Object> doc = docOpt.get();
            Integer status = (Integer) doc.get("STATUS");
            if (status == 5 || status == 13 || status == 3) {
                doc.put("PROCESSEDBY", 9999L);
                doc.put("STATUS", 3);
                response.put("success", true);
                response.put("documentId", docId);
                response.put("statusIdFrom", statusIdFrom);
                response.put("officerId", 9999);
                response.put("result", 9999);
                return response;
            }
        }

        response.put("success", false);
        response.put("documentId", docId);
        response.put("statusIdFrom", statusIdFrom);
        response.put("officerId", 0);
        response.put("result", 0);
        return response;
    }
}

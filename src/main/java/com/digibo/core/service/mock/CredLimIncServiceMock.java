package com.digibo.core.service.mock;

import com.digibo.core.service.CredLimIncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * CredLimIncServiceMock - Mock implementation of CredLimIncService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class CredLimIncServiceMock implements CredLimIncService {

    private static final Logger logger = LoggerFactory.getLogger(CredLimIncServiceMock.class);

    @Override
    public List<Map<String, Object>> find(String custId, String custName, String userLogin,
                                           String docId, String statuses, String docClass,
                                           Date createdFrom, Date createdTill, Long officerId,
                                           String fromLocation) {
        logger.debug("[MOCK] CredLimIncService.find() called");

        List<Map<String, Object>> results = new ArrayList<>();

        Map<String, Object> order1 = new HashMap<>();
        order1.put("PID", 2001L);
        order1.put("STATUS", 5);
        order1.put("CLASS_ID", 200);
        order1.put("CREATED", new Date());
        order1.put("DOCNUMBER", "CLI-2024-001");
        order1.put("LOGIN", "user.cli1");
        order1.put("CUST_ID", "CUST001");
        order1.put("FROMLOCATION", "LV");
        order1.put("PROCESSEDBY", null);
        results.add(order1);

        Map<String, Object> order2 = new HashMap<>();
        order2.put("PID", 2002L);
        order2.put("STATUS", 13);
        order2.put("CLASS_ID", 200);
        order2.put("CREATED", new Date(System.currentTimeMillis() - 86400000));
        order2.put("DOCNUMBER", "CLI-2024-002");
        order2.put("LOGIN", "user.cli2");
        order2.put("CUST_ID", "CUST002");
        order2.put("FROMLOCATION", "EE");
        order2.put("PROCESSEDBY", 101);
        results.add(order2);

        // Apply filters
        if (docId != null) {
            results.removeIf(d -> !String.valueOf(d.get("PID")).equals(docId));
        }
        if (statuses != null && !statuses.isEmpty()) {
            List<Integer> statusList = Arrays.stream(statuses.split(","))
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .toList();
            results.removeIf(d -> !statusList.contains((Integer) d.get("STATUS")));
        }
        if (custId != null) {
            results.removeIf(d -> !custId.equals(d.get("CUST_ID")));
        }

        logger.debug("[MOCK] Returning {} credit limit increase orders", results.size());
        return results;
    }

    @Override
    public List<Map<String, Object>> findMy(Long officerId) {
        logger.debug("[MOCK] CredLimIncService.findMy({}) called", officerId);

        List<Map<String, Object>> results = new ArrayList<>();

        if (officerId == null || officerId == 0) {
            // Return new/unassigned orders
            Map<String, Object> order = new HashMap<>();
            order.put("PID", 2001L);
            order.put("STATUS", 5);
            order.put("CLASS_ID", 200);
            order.put("CREATED", new Date());
            order.put("DOCNUMBER", "CLI-2024-001");
            order.put("LOGIN", "user.cli1");
            order.put("PROCESSEDBY", null);
            results.add(order);
        } else {
            // Return orders assigned to this officer
            Map<String, Object> order = new HashMap<>();
            order.put("PID", 2002L);
            order.put("STATUS", 3);
            order.put("CLASS_ID", 200);
            order.put("CREATED", new Date());
            order.put("DOCNUMBER", "CLI-2024-002");
            order.put("LOGIN", "user.cli2");
            order.put("PROCESSEDBY", officerId);
            results.add(order);
        }

        logger.debug("[MOCK] Returning {} credit limit increase orders", results.size());
        return results;
    }

    @Override
    public Map<String, Object> setProcessing(String docId) {
        logger.debug("[MOCK] CredLimIncService.setProcessing({}) called", docId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("documentId", docId);
        response.put("officerId", 9999);
        response.put("result", 9999);

        return response;
    }
}

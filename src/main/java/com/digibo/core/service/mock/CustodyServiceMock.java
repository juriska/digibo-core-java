package com.digibo.core.service.mock;

import com.digibo.core.service.CustodyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * CustodyServiceMock - Mock implementation of CustodyService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class CustodyServiceMock implements CustodyService {

    private static final Logger logger = LoggerFactory.getLogger(CustodyServiceMock.class);

    @Override
    public List<Map<String, Object>> find(String custId, String custName, String userLogin,
                                           String docId, String statuses, String docClass,
                                           Date createdFrom, Date createdTill) {
        logger.debug("[MOCK] CustodyService.find() called");

        List<Map<String, Object>> results = new ArrayList<>();

        Map<String, Object> order1 = new HashMap<>();
        order1.put("ID", 4001L);
        order1.put("STATUS_ID", 1);
        order1.put("CLASS_ID", 400);
        order1.put("ORDER_DATE", new Date());
        order1.put("LOGIN", "custody.user1");
        order1.put("CUST_NAME", "Custody Customer 1");
        order1.put("GLB_CUST_ID", "CUST001");
        order1.put("OFFICER_ID", null);
        results.add(order1);

        Map<String, Object> order2 = new HashMap<>();
        order2.put("ID", 4002L);
        order2.put("STATUS_ID", 3);
        order2.put("CLASS_ID", 400);
        order2.put("ORDER_DATE", new Date(System.currentTimeMillis() - 86400000));
        order2.put("LOGIN", "custody.user2");
        order2.put("CUST_NAME", "Custody Customer 2");
        order2.put("GLB_CUST_ID", "CUST002");
        order2.put("OFFICER_ID", 101);
        results.add(order2);

        // Apply filters
        if (docId != null) {
            results.removeIf(d -> !String.valueOf(d.get("ID")).equals(docId));
        }
        if (custId != null) {
            results.removeIf(d -> !custId.equals(d.get("GLB_CUST_ID")));
        }
        if (statuses != null && !statuses.isEmpty()) {
            List<Integer> statusList = Arrays.stream(statuses.split(","))
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .toList();
            results.removeIf(d -> !statusList.contains((Integer) d.get("STATUS_ID")));
        }

        logger.debug("[MOCK] Returning {} custody orders", results.size());
        return results;
    }

    @Override
    public List<Map<String, Object>> findMy(String officerId) {
        logger.debug("[MOCK] CustodyService.findMy({}) called", officerId);

        List<Map<String, Object>> results = new ArrayList<>();

        Map<String, Object> order = new HashMap<>();
        order.put("ID", 4001L);
        order.put("STATUS_ID", 1);
        order.put("CLASS_ID", 400);
        order.put("ORDER_DATE", new Date());
        order.put("LOGIN", "custody.user1");
        order.put("CUST_NAME", "Custody Customer 1");
        order.put("OFFICER_ID", officerId != null ? Long.parseLong(officerId) : null);
        results.add(order);

        logger.debug("[MOCK] Returning {} custody orders", results.size());
        return results;
    }

    @Override
    public Map<String, Object> setProcessing(String orderId) {
        logger.debug("[MOCK] CustodyService.setProcessing({}) called", orderId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("orderId", orderId);
        response.put("result", 0);

        return response;
    }

    @Override
    public Map<String, Object> custody(String orderId) {
        logger.debug("[MOCK] CustodyService.custody({}) called", orderId);

        Map<String, Object> response = new HashMap<>();
        response.put("id", orderId);
        response.put("userName", "Mock User");
        response.put("legalId", "123456-78901");
        response.put("officerName", "Mock Officer");
        response.put("custName", "Mock Customer");
        response.put("globusNo", "MOCK123456");
        response.put("location", "LV");
        response.put("fromAccount", "LV00MOCK0000000000001 EUR");
        response.put("utPhoneNumber", "+371 20000000");
        response.put("phoneMobile", "+371 29000000");
        response.put("authName", "John");
        response.put("authSurname", "Doe");
        response.put("authLegalId", "123456-78901");
        response.put("authPassportNo", "LV1234567");
        response.put("authPassportCountry", "LV");
        response.put("authPassportInst", "PMLP");
        response.put("authPhone", "+371 20000000");
        response.put("authFax", null);
        response.put("authEmail", "john.doe@email.com");
        response.put("infoToCustomer", "Mock custody order details");
        response.put("infoToBank", "Mock bank info");
        response.put("channelId", 5);
        response.put("signTime", new Date());
        response.put("signDevType", 5);
        response.put("signDevId", "DEVICE_MOCK");
        response.put("signKey1", "KEY1_MOCK");
        response.put("signKey2", "KEY2_MOCK");
        response.put("signRSA", "RSA_SIG_MOCK");

        return response;
    }
}

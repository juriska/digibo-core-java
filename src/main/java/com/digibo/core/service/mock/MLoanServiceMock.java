package com.digibo.core.service.mock;

import com.digibo.core.service.MLoanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * MLoanServiceMock - Mock implementation of MLoanService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class MLoanServiceMock implements MLoanService {

    private static final Logger logger = LoggerFactory.getLogger(MLoanServiceMock.class);

    private final List<Map<String, Object>> mockOrders = new ArrayList<>();

    public MLoanServiceMock() {
        initMockData();
    }

    private void initMockData() {
        Map<String, Object> order1 = new HashMap<>();
        order1.put("PID", "ML001");
        order1.put("CUST_ID", "CUST001");
        order1.put("CUST_NAME", "John Doe");
        order1.put("LOGIN", "jdoe");
        order1.put("STATUS", 5);
        order1.put("CLASS_ID", 40);
        order1.put("CREATED", new Date());
        order1.put("PROCESSEDBY", null);
        order1.put("FROMLOCATION", "LV");
        mockOrders.add(order1);

        Map<String, Object> order2 = new HashMap<>();
        order2.put("PID", "ML002");
        order2.put("CUST_ID", "CUST002");
        order2.put("CUST_NAME", "Jane Smith");
        order2.put("LOGIN", "jsmith");
        order2.put("STATUS", 3);
        order2.put("CLASS_ID", 41);
        order2.put("CREATED", new Date());
        order2.put("PROCESSEDBY", 9999L);
        order2.put("FROMLOCATION", "EE");
        mockOrders.add(order2);

        Map<String, Object> order3 = new HashMap<>();
        order3.put("PID", "ML003");
        order3.put("CUST_ID", "CUST003");
        order3.put("CUST_NAME", "Bob Wilson");
        order3.put("LOGIN", "bwilson");
        order3.put("STATUS", 5);
        order3.put("CLASS_ID", 40);
        order3.put("CREATED", new Date(System.currentTimeMillis() - 86400000));
        order3.put("PROCESSEDBY", null);
        order3.put("FROMLOCATION", "LT");
        mockOrders.add(order3);
    }

    @Override
    public List<Map<String, Object>> find(String custId, String custName, String userLogin,
                                           String docId, String statuses,
                                           Date createdFrom, Date createdTill,
                                           String docClass, String fromLocation) {
        logger.debug("[MOCK] find()");

        return mockOrders.stream()
                .filter(o -> custId == null || custId.equals(o.get("CUST_ID")))
                .filter(o -> userLogin == null || ((String) o.get("LOGIN")).toLowerCase().contains(userLogin.toLowerCase()))
                .filter(o -> docId == null || docId.equals(o.get("PID")))
                .filter(o -> fromLocation == null || ((String) o.get("FROMLOCATION")).toLowerCase().contains(fromLocation.toLowerCase()))
                .toList();
    }

    @Override
    public List<Map<String, Object>> findMy(Long officerId) {
        logger.debug("[MOCK] findMy({})", officerId);

        if (officerId == null || officerId == 0L) {
            // Return new/unassigned orders
            return mockOrders.stream()
                    .filter(o -> o.get("PROCESSEDBY") == null && Integer.valueOf(5).equals(o.get("STATUS")))
                    .toList();
        } else {
            // Return orders assigned to this officer
            return mockOrders.stream()
                    .filter(o -> officerId.equals(o.get("PROCESSEDBY")) && Integer.valueOf(3).equals(o.get("STATUS")))
                    .toList();
        }
    }

    @Override
    public Map<String, Object> mloan(String docId) {
        logger.debug("[MOCK] mloan({})", docId);

        Map<String, Object> response = new HashMap<>();
        response.put("id", docId);
        response.put("userName", "Mock User (mock.user)");
        response.put("legalId", "010190-12345");
        response.put("officerName", "Mock Officer");
        response.put("custName", "Mock Customer");
        response.put("globusNo", "MOCK123456");
        response.put("location", "LV");
        response.put("fromAccount", "LV00MOCK0000000000001 EUR");
        response.put("utPhoneNumber", "+371 20000000");
        response.put("phoneMobile", "+371 29000000");
        response.put("authName", "Mock");
        response.put("authSurname", "User");
        response.put("authLegalId", "010190-12345");
        response.put("authPassportNo", "LV0000000");
        response.put("authPassportCountry", "LV");
        response.put("authPassportInst", "PMLP");
        response.put("authPhone", "+371 20000000");
        response.put("authFax", null);
        response.put("authEmail", "mock.user@email.com");
        response.put("infoToCustomer", "This is mock mortgage loan info to customer for document " + docId);
        response.put("infoToBank", "This is mock mortgage loan info to bank for document " + docId);
        response.put("channelId", 5);
        response.put("signTime", new Date());
        response.put("signDevType", 5);
        response.put("signDevId", "MOCK_DEVICE");
        response.put("signKey1", "MOCK_KEY1");
        response.put("signKey2", "MOCK_KEY2");
        response.put("signRSA", "MOCK_RSA_SIGNATURE");
        return response;
    }

    @Override
    public Map<String, Object> setProcessing(String docId) {
        logger.debug("[MOCK] setProcessing({})", docId);

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
                response.put("officerId", 9999);
                response.put("result", 9999);
                return response;
            }
        }

        response.put("success", false);
        response.put("documentId", docId);
        response.put("officerId", 0);
        response.put("result", 0);
        return response;
    }
}

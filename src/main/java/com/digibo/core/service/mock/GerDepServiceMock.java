package com.digibo.core.service.mock;

import com.digibo.core.service.GerDepService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * GerDepServiceMock - Mock implementation of GerDepService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class GerDepServiceMock implements GerDepService {

    private static final Logger logger = LoggerFactory.getLogger(GerDepServiceMock.class);

    private final List<Map<String, Object>> mockOrders = new ArrayList<>();

    public GerDepServiceMock() {
        initMockData();
    }

    private void initMockData() {
        Map<String, Object> order1 = new HashMap<>();
        order1.put("DOC_ID", "GDEP001");
        order1.put("CUST_ID", "CUST001");
        order1.put("CUST_NAME", "John Doe");
        order1.put("LOGIN", "jdoe");
        order1.put("STATUS", "NEW");
        order1.put("ORDER_DATE", new Date());
        order1.put("ID_DOC_NO", "DE123456789");
        order1.put("ACCOUNT_NO", null);
        mockOrders.add(order1);

        Map<String, Object> order2 = new HashMap<>();
        order2.put("DOC_ID", "GDEP002");
        order2.put("CUST_ID", "CUST002");
        order2.put("CUST_NAME", "Jane Smith");
        order2.put("LOGIN", "jsmith");
        order2.put("STATUS", "PENDING");
        order2.put("ORDER_DATE", new Date());
        order2.put("ID_DOC_NO", "DE987654321");
        order2.put("ACCOUNT_NO", "DE89370400440532013000");
        mockOrders.add(order2);

        Map<String, Object> order3 = new HashMap<>();
        order3.put("DOC_ID", "GDEP003");
        order3.put("CUST_ID", "CUST003");
        order3.put("CUST_NAME", "Max Mustermann");
        order3.put("LOGIN", "mmuster");
        order3.put("STATUS", "COMPLETED");
        order3.put("ORDER_DATE", new Date(System.currentTimeMillis() - 86400000));
        order3.put("ID_DOC_NO", "DE555666777");
        order3.put("ACCOUNT_NO", "DE89370400440532013001");
        mockOrders.add(order3);
    }

    @Override
    public List<Map<String, Object>> findNew() {
        logger.debug("[MOCK] findNew()");
        return mockOrders.stream()
                .filter(o -> "NEW".equals(o.get("STATUS")) || "PENDING".equals(o.get("STATUS")))
                .toList();
    }

    @Override
    public List<Map<String, Object>> findByFilter(String docId, String custId, String custName,
                                                   String idDocNo, String login, String status,
                                                   Date orderDateFrom, Date orderDateTo) {
        logger.debug("[MOCK] findByFilter()");

        return mockOrders.stream()
                .filter(o -> docId == null || docId.equals(o.get("DOC_ID")))
                .filter(o -> custId == null || custId.equals(o.get("CUST_ID")))
                .filter(o -> custName == null || ((String) o.get("CUST_NAME")).toLowerCase().contains(custName.toLowerCase()))
                .filter(o -> idDocNo == null || ((String) o.get("ID_DOC_NO")).toLowerCase().contains(idDocNo.toLowerCase()))
                .filter(o -> login == null || ((String) o.get("LOGIN")).toLowerCase().contains(login.toLowerCase()))
                .filter(o -> status == null || status.equals(o.get("STATUS")))
                .toList();
    }

    @Override
    public Map<String, Object> selectCustomer(String custId) {
        logger.debug("[MOCK] selectCustomer({})", custId);

        List<Map<String, Object>> customers = new ArrayList<>();

        if ("CUST001".equals(custId) || "CUST002".equals(custId)) {
            Map<String, Object> customer = new HashMap<>();
            customer.put("CUST_ID", custId);
            customer.put("CUST_NAME", "CUST001".equals(custId) ? "John Doe" : "Jane Smith");
            customer.put("PERSONAL_ID", "123456-12345");
            customer.put("EMAIL", "customer@example.com");
            customers.add(customer);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("rv", customers.isEmpty() ? -1 : 0);
        response.put("customers", customers);
        return response;
    }

    @Override
    public Map<String, Object> bindToCustomer(String docId, String custId) {
        logger.debug("[MOCK] bindToCustomer({}, {})", docId, custId);

        mockOrders.stream()
                .filter(o -> docId.equals(o.get("DOC_ID")))
                .findFirst()
                .ifPresent(order -> {
                    order.put("CUST_ID", custId);
                    order.put("STATUS", "BOUND");
                });

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("documentId", docId);
        response.put("customerId", custId);
        return response;
    }

    @Override
    public Map<String, Object> accountExists(String docId) {
        logger.debug("[MOCK] accountExists({})", docId);

        boolean exists = mockOrders.stream()
                .filter(o -> docId.equals(o.get("DOC_ID")))
                .findFirst()
                .map(o -> o.get("CUST_ID") != null && o.get("ACCOUNT_NO") != null)
                .orElse(false);

        Map<String, Object> response = new HashMap<>();
        response.put("documentId", docId);
        response.put("accountExists", exists ? "Y" : "N");
        response.put("exists", exists);
        return response;
    }

    @Override
    public Map<String, Object> createUser(String docId, String tanCardId) {
        logger.debug("[MOCK] createUser({}, {})", docId, tanCardId);

        mockOrders.stream()
                .filter(o -> docId.equals(o.get("DOC_ID")))
                .findFirst()
                .ifPresent(order -> {
                    order.put("TAN_CARD_ID", tanCardId);
                    order.put("STATUS", "USER_CREATED");
                });

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("documentId", docId);
        response.put("tanCardId", tanCardId);
        return response;
    }

    @Override
    public Map<String, Object> reject(String docId, String reason) {
        logger.debug("[MOCK] reject({}, {})", docId, reason);

        mockOrders.stream()
                .filter(o -> docId.equals(o.get("DOC_ID")))
                .findFirst()
                .ifPresent(order -> {
                    order.put("STATUS", "REJECTED");
                    order.put("REJECTION_REASON", reason);
                });

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("documentId", docId);
        response.put("reason", reason);
        return response;
    }
}

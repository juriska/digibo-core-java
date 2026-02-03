package com.digibo.core.service.mock;

import com.digibo.core.service.MarginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * MarginServiceMock - Mock implementation of MarginService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class MarginServiceMock implements MarginService {

    private static final Logger logger = LoggerFactory.getLogger(MarginServiceMock.class);

    private final List<Map<String, Object>> mockOrders = new ArrayList<>();

    public MarginServiceMock() {
        initMockData();
    }

    private void initMockData() {
        Map<String, Object> order1 = new HashMap<>();
        order1.put("ID", "MGN001");
        order1.put("GLB_CUST_ID", "CUST001");
        order1.put("CUST_NAME", "John Doe");
        order1.put("LOGIN", "jdoe");
        order1.put("CLASS_ID", "FOREX");
        order1.put("STATUS_ID", 1);
        order1.put("ORDER_DATE", new Date());
        order1.put("EXCHANGE_RATE", 1.08);
        order1.put("ORDER_CCY", "EUR");
        order1.put("CONTRARY_CCY", "USD");
        order1.put("GOOD_TILL", new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000));
        mockOrders.add(order1);

        Map<String, Object> order2 = new HashMap<>();
        order2.put("ID", "MGN002");
        order2.put("GLB_CUST_ID", "CUST002");
        order2.put("CUST_NAME", "Jane Smith");
        order2.put("LOGIN", "jsmith");
        order2.put("CLASS_ID", "FOREX");
        order2.put("STATUS_ID", 2);
        order2.put("ORDER_DATE", new Date());
        order2.put("EXCHANGE_RATE", 0.85);
        order2.put("ORDER_CCY", "GBP");
        order2.put("CONTRARY_CCY", "EUR");
        order2.put("GOOD_TILL", new Date(System.currentTimeMillis() + 14 * 24 * 60 * 60 * 1000));
        mockOrders.add(order2);
    }

    @Override
    public List<Map<String, Object>> find(String custId, String custName, String userLogin, String userPassword,
                                           String docClass, Double rateFrom, Double rateTill,
                                           String orderCCY, String contraryCCY,
                                           Date expiryFrom, Date expiryTill,
                                           String docId, String statuses,
                                           Date createdFrom, Date createdTill) {
        logger.debug("[MOCK] find()");

        return mockOrders.stream()
                .filter(o -> custId == null || custId.equals(o.get("GLB_CUST_ID")))
                .filter(o -> custName == null || ((String) o.get("CUST_NAME")).toLowerCase().contains(custName.toLowerCase()))
                .filter(o -> userLogin == null || ((String) o.get("LOGIN")).toLowerCase().contains(userLogin.toLowerCase()))
                .filter(o -> docClass == null || docClass.equals(o.get("CLASS_ID")))
                .filter(o -> docId == null || docId.equals(o.get("ID")))
                .filter(o -> orderCCY == null || orderCCY.equals(o.get("ORDER_CCY")))
                .filter(o -> contraryCCY == null || contraryCCY.equals(o.get("CONTRARY_CCY")))
                .toList();
    }

    @Override
    public List<Map<String, Object>> findMy(String docClass) {
        logger.debug("[MOCK] findMy({})", docClass);

        if (docClass == null) {
            return new ArrayList<>(mockOrders);
        }

        return mockOrders.stream()
                .filter(o -> docClass.equals(o.get("CLASS_ID")))
                .toList();
    }

    @Override
    public Map<String, Object> margin(String orderId) {
        logger.debug("[MOCK] margin({})", orderId);

        Map<String, Object> response = new HashMap<>();
        response.put("id", orderId);
        response.put("docNo", "MARGIN-MOCK-001");
        response.put("userName", "Mock User");
        response.put("userId", "MOCK001");
        response.put("officerName", "Mock Officer");
        response.put("custName", "Mock Customer");
        response.put("custAccount", "LV00MOCK0000000000001");
        response.put("globusNo", "MOCK123456");
        response.put("operation", "BUY");
        response.put("investVolume", "10000.00");
        response.put("identCode", "EURUSD");
        response.put("orderType", "LIMIT");
        response.put("goodTill", new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000));
        response.put("creditCCY", "USD");
        response.put("debitCCY", "EUR");
        response.put("exchangeRate", 1.08);
        response.put("valueDate", new Date());
        response.put("text", "Mock margin order");
        response.put("location", "LV");
        response.put("infoToCustomer", "Mock info to customer");
        response.put("infoToBank", "Mock info to bank");
        return response;
    }
}

package com.digibo.core.service.mock;

import com.digibo.core.service.BrokerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * BrokerServiceMock - Mock implementation of BrokerService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class BrokerServiceMock implements BrokerService {

    private static final Logger logger = LoggerFactory.getLogger(BrokerServiceMock.class);

    @Override
    public List<Map<String, Object>> find(String custId, String custName, String userLogin, String userPassword,
                                          String docClass, String operationType, Integer docCount, String currencies,
                                          Date expiryFrom, Date expiryTill,
                                          String docId, String statuses, Date createdFrom, Date createdTill) {
        logger.debug("[MOCK] find({}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {})",
                custId, custName, userLogin, userPassword, docClass, operationType, docCount, currencies,
                expiryFrom, expiryTill, docId, statuses, createdFrom, createdTill);

        List<Map<String, Object>> orders = new ArrayList<>();

        Map<String, Object> order1 = new HashMap<>();
        order1.put("DOC_ID", "BROKER001");
        order1.put("DOC_NO", "BRK-2024-001");
        order1.put("CUST_ID", custId != null ? custId : "CUST001");
        order1.put("CUST_NAME", "Test Customer 1");
        order1.put("USER_LOGIN", "user1");
        order1.put("STATUS", "PENDING");
        order1.put("DOC_CLASS", docClass != null ? docClass : "SECURITIES");
        order1.put("OPERATION_TYPE", "BUY");
        order1.put("CURRENCY", "EUR");
        order1.put("CREATED_DATE", new Date());
        orders.add(order1);

        Map<String, Object> order2 = new HashMap<>();
        order2.put("DOC_ID", "BROKER002");
        order2.put("DOC_NO", "BRK-2024-002");
        order2.put("CUST_ID", "CUST002");
        order2.put("CUST_NAME", "Test Customer 2");
        order2.put("USER_LOGIN", "user2");
        order2.put("STATUS", "PROCESSING");
        order2.put("DOC_CLASS", "SECURITIES");
        order2.put("OPERATION_TYPE", "SELL");
        order2.put("CURRENCY", "USD");
        order2.put("CREATED_DATE", new Date(System.currentTimeMillis() - 86400000));
        orders.add(order2);

        return orders;
    }

    @Override
    public List<Map<String, Object>> findMy(String docClass) {
        logger.debug("[MOCK] findMy({})", docClass);

        List<Map<String, Object>> orders = new ArrayList<>();

        Map<String, Object> order1 = new HashMap<>();
        order1.put("DOC_ID", "BROKER003");
        order1.put("DOC_NO", "BRK-2024-003");
        order1.put("CUST_ID", "CUST001");
        order1.put("CUST_NAME", "My Customer");
        order1.put("USER_LOGIN", "myuser");
        order1.put("STATUS", "PROCESSING");
        order1.put("DOC_CLASS", docClass != null ? docClass : "SECURITIES");
        order1.put("OPERATION_TYPE", "BUY");
        order1.put("CURRENCY", "EUR");
        order1.put("CREATED_DATE", new Date());
        orders.add(order1);

        return orders;
    }

    @Override
    public Map<String, Object> broker(String docId) {
        logger.debug("[MOCK] broker({})", docId);

        Map<String, Object> response = new HashMap<>();
        response.put("id", docId);
        response.put("docNo", "BRK-2024-001");
        response.put("userName", "John Doe");
        response.put("userId", "USER001");
        response.put("officerName", "Officer Smith");
        response.put("custName", "Test Customer Ltd");
        response.put("custAccount", "LV80HABA0551000000001");
        response.put("globusNo", "GL123456");
        response.put("portfolioId", "PORT001");
        response.put("operation", "BUY");
        response.put("isinCode", "LV0000101129");
        response.put("fundName", "Baltic Growth Fund");
        response.put("investVolume", "10000.00");
        response.put("identCode", "ID001");
        response.put("stockSymbol", "AAPL");
        response.put("optionSymbol", "AAPL230120C150");
        response.put("emitentName", "Apple Inc.");
        response.put("exchangeName", "NASDAQ");
        response.put("optionType", "CALL");
        response.put("optionPremium", "5.50");
        response.put("maturityDate", new Date(System.currentTimeMillis() + 90L * 24 * 60 * 60 * 1000));
        response.put("couponRate", "3.5%");
        response.put("currency", "EUR");
        response.put("orderType", "LIMIT");
        response.put("price", "150.00");
        response.put("stopPrice", "145.00");
        response.put("trailAmount", "5.00");
        response.put("goodTill", "GTC");
        response.put("text", "Buy order for Apple stock");
        response.put("location", "MAIN_OFFICE");
        response.put("infoToCustomer", "Order received and being processed");
        response.put("infoToBank", "Internal notes for broker");

        return response;
    }
}

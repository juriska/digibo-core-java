package com.digibo.core.service.mock;

import com.digibo.core.service.CapfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * CapfServiceMock - Mock implementation of CapfService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class CapfServiceMock implements CapfService {

    private static final Logger logger = LoggerFactory.getLogger(CapfServiceMock.class);

    @Override
    public List<Map<String, Object>> find(String custId, String custName, String userLogin,
                                          String docId, String statuses, String docClass,
                                          Date createdFrom, Date createdTill,
                                          String customerName, String legalId) {
        logger.debug("[MOCK] find({}, {}, {}, {}, {}, {}, {}, {}, {}, {})",
                custId, custName, userLogin, docId, statuses, docClass,
                createdFrom, createdTill, customerName, legalId);

        List<Map<String, Object>> orders = new ArrayList<>();

        Map<String, Object> order1 = new HashMap<>();
        order1.put("DOC_ID", "CAPF001");
        order1.put("CUST_ID", custId != null ? custId : "CUST001");
        order1.put("CUST_NAME", "Test Customer 1");
        order1.put("USER_LOGIN", "user1");
        order1.put("STATUS", "PENDING");
        order1.put("DOC_CLASS", "CAPF");
        order1.put("LEGAL_ID", "123456789");
        order1.put("CREATED_DATE", new Date());
        orders.add(order1);

        Map<String, Object> order2 = new HashMap<>();
        order2.put("DOC_ID", "CAPF002");
        order2.put("CUST_ID", "CUST002");
        order2.put("CUST_NAME", "Test Customer 2");
        order2.put("USER_LOGIN", "user2");
        order2.put("STATUS", "PROCESSING");
        order2.put("DOC_CLASS", "CAPF");
        order2.put("LEGAL_ID", "987654321");
        order2.put("CREATED_DATE", new Date(System.currentTimeMillis() - 86400000));
        orders.add(order2);

        return orders;
    }

    @Override
    public List<Map<String, Object>> findMy(Long officerId) {
        logger.debug("[MOCK] findMy({})", officerId);

        List<Map<String, Object>> orders = new ArrayList<>();

        Map<String, Object> order1 = new HashMap<>();
        order1.put("DOC_ID", "CAPF003");
        order1.put("CUST_ID", "CUST001");
        order1.put("CUST_NAME", "My Customer");
        order1.put("USER_LOGIN", "myuser");
        order1.put("STATUS", "NEW");
        order1.put("OFFICER_ID", officerId);
        order1.put("CREATED_DATE", new Date());
        orders.add(order1);

        return orders;
    }

    @Override
    public Map<String, Object> capforder(String orderId) {
        logger.debug("[MOCK] capforder({})", orderId);

        Map<String, Object> response = new HashMap<>();
        response.put("id", orderId);
        response.put("userName", "John Doe");
        response.put("legalId", "123456789");
        response.put("officerName", "Officer Smith");
        response.put("custName", "Test Customer Ltd");
        response.put("globusNo", "GL123456");
        response.put("location", "MAIN_OFFICE");
        response.put("fromAccount", "LV80HABA0551000000001");
        response.put("utPhoneNumber", "+371-67890123");
        response.put("phoneMobile", "+371-29876543");
        response.put("authName", "Jane");
        response.put("authSurname", "Smith");
        response.put("authLegalId", "987654321");
        response.put("authPassportNo", "AB123456");
        response.put("authPassportCountry", "LV");
        response.put("authPassportInst", "PMLP");
        response.put("authPhone", "+371-67111222");
        response.put("authFax", "+371-67111223");
        response.put("authEmail", "jane.smith@example.com");
        response.put("infoToCustomer", "CAPF order processed");
        response.put("infoToBank", "Internal notes for bank");
        response.put("channelId", 1);
        response.put("signTime", new Date());
        response.put("signDevType", 1);
        response.put("signDevId", "DEV001");
        response.put("signKey1", "KEY1_MOCK");
        response.put("signKey2", "KEY2_MOCK");
        response.put("signRSA", "RSA_MOCK_SIGNATURE");

        return response;
    }

    @Override
    public int setProcessing(String orderId) {
        logger.debug("[MOCK] setProcessing({})", orderId);
        return orderId != null && orderId.startsWith("CAPF") ? 1 : 0;
    }
}

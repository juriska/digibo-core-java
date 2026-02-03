package com.digibo.core.service.mock;

import com.digibo.core.service.AccAdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * AccAdminServiceMock - Mock implementation of AccAdminService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class AccAdminServiceMock implements AccAdminService {

    private static final Logger logger = LoggerFactory.getLogger(AccAdminServiceMock.class);

    @Override
    public List<Map<String, Object>> find(String custId, String custName, String userLogin, Long officerId,
                                          String docId, String statuses, String docClass,
                                          Date createdFrom, Date createdTill) {
        logger.debug("[MOCK] find({}, {}, {}, {}, {}, {}, {}, {}, {})",
                custId, custName, userLogin, officerId, docId, statuses, docClass, createdFrom, createdTill);

        List<Map<String, Object>> orders = new ArrayList<>();

        Map<String, Object> order1 = new HashMap<>();
        order1.put("DOC_ID", "ACCADMIN001");
        order1.put("CUST_ID", custId != null ? custId : "CUST001");
        order1.put("CUST_NAME", "Test Customer 1");
        order1.put("USER_LOGIN", "user1");
        order1.put("STATUS", "PENDING");
        order1.put("DOC_CLASS", "ACC_ADMIN");
        order1.put("CREATED_DATE", new Date());
        orders.add(order1);

        Map<String, Object> order2 = new HashMap<>();
        order2.put("DOC_ID", "ACCADMIN002");
        order2.put("CUST_ID", "CUST002");
        order2.put("CUST_NAME", "Test Customer 2");
        order2.put("USER_LOGIN", "user2");
        order2.put("STATUS", "PROCESSING");
        order2.put("DOC_CLASS", "ACC_ADMIN");
        order2.put("CREATED_DATE", new Date(System.currentTimeMillis() - 86400000));
        orders.add(order2);

        return orders;
    }

    @Override
    public List<Map<String, Object>> findMy(Long officerId) {
        logger.debug("[MOCK] findMy({})", officerId);

        List<Map<String, Object>> orders = new ArrayList<>();

        Map<String, Object> order1 = new HashMap<>();
        order1.put("DOC_ID", "ACCADMIN003");
        order1.put("CUST_ID", "CUST001");
        order1.put("CUST_NAME", "My Customer");
        order1.put("USER_LOGIN", "myuser");
        order1.put("STATUS", "PROCESSING");
        order1.put("OFFICER_ID", officerId);
        order1.put("CREATED_DATE", new Date());
        orders.add(order1);

        return orders;
    }

    @Override
    public int setProcessing(String docId) {
        logger.debug("[MOCK] setProcessing({})", docId);
        return docId != null && docId.startsWith("ACCADMIN") ? 0 : -1;
    }

    @Override
    public Map<String, Object> accadmin(String docId) {
        logger.debug("[MOCK] accadmin({})", docId);

        Map<String, Object> response = new HashMap<>();
        response.put("id", docId);
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
        response.put("infoToCustomer", "Account administration order processed");
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
}

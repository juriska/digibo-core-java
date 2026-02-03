package com.digibo.core.service.mock;

import com.digibo.core.service.CardsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * CardsServiceMock - Mock implementation of CardsService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class CardsServiceMock implements CardsService {

    private static final Logger logger = LoggerFactory.getLogger(CardsServiceMock.class);

    @Override
    public List<Map<String, Object>> find(String custId, String custName, String userLogin, Long officerId,
                                          String docClass, String fromLocation,
                                          String docId, String statuses, Date createdFrom, Date createdTill,
                                          String channels) {
        logger.debug("[MOCK] find({}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {})",
                custId, custName, userLogin, officerId, docClass, fromLocation,
                docId, statuses, createdFrom, createdTill, channels);

        List<Map<String, Object>> orders = new ArrayList<>();

        Map<String, Object> order1 = new HashMap<>();
        order1.put("DOC_ID", "CARD001");
        order1.put("CUST_ID", custId != null ? custId : "CUST001");
        order1.put("CUST_NAME", "Test Customer 1");
        order1.put("USER_LOGIN", "user1");
        order1.put("STATUS", "PENDING");
        order1.put("DOC_CLASS", docClass != null ? docClass : "CREDIT_CARD");
        order1.put("LOCATION", "MAIN");
        order1.put("PAN", "4111111111111111");
        order1.put("CREATED_DATE", new Date());
        orders.add(order1);

        Map<String, Object> order2 = new HashMap<>();
        order2.put("DOC_ID", "CARD002");
        order2.put("CUST_ID", "CUST002");
        order2.put("CUST_NAME", "Test Customer 2");
        order2.put("USER_LOGIN", "user2");
        order2.put("STATUS", "PROCESSING");
        order2.put("DOC_CLASS", "CREDIT_CARD");
        order2.put("LOCATION", "BRANCH_A");
        order2.put("PAN", "5555555555554444");
        order2.put("CREATED_DATE", new Date(System.currentTimeMillis() - 86400000));
        orders.add(order2);

        return orders;
    }

    @Override
    public List<Map<String, Object>> findMy(Long officerId, String docClass) {
        logger.debug("[MOCK] findMy({}, {})", officerId, docClass);

        List<Map<String, Object>> orders = new ArrayList<>();

        Map<String, Object> order1 = new HashMap<>();
        order1.put("DOC_ID", "CARD003");
        order1.put("CUST_ID", "CUST001");
        order1.put("CUST_NAME", "My Customer");
        order1.put("USER_LOGIN", "myuser");
        order1.put("STATUS", "PROCESSING");
        order1.put("DOC_CLASS", docClass != null ? docClass : "CREDIT_CARD");
        order1.put("OFFICER_ID", officerId);
        order1.put("PAN", "4111111111111111");
        order1.put("CREATED_DATE", new Date());
        orders.add(order1);

        return orders;
    }

    @Override
    public List<Map<String, Object>> getExtensions(String docId) {
        logger.debug("[MOCK] getExtensions({})", docId);

        List<Map<String, Object>> extensions = new ArrayList<>();

        Map<String, Object> ext1 = new HashMap<>();
        ext1.put("EXT_ID", 1L);
        ext1.put("DOC_ID", docId);
        ext1.put("EXT_TYPE", "ADDITIONAL_CARD");
        ext1.put("CARDHOLDER_NAME", "Jane Doe");
        ext1.put("STATUS", "ACTIVE");
        extensions.add(ext1);

        Map<String, Object> ext2 = new HashMap<>();
        ext2.put("EXT_ID", 2L);
        ext2.put("DOC_ID", docId);
        ext2.put("EXT_TYPE", "INSURANCE");
        ext2.put("INSURANCE_TYPE", "TRAVEL");
        ext2.put("STATUS", "ACTIVE");
        extensions.add(ext2);

        return extensions;
    }

    @Override
    public int setProcessing(String docId, Integer statusIdFrom) {
        logger.debug("[MOCK] setProcessing({}, {})", docId, statusIdFrom);
        return docId != null && docId.startsWith("CARD") ? 0 : -1;
    }

    @Override
    public Map<String, Object> card(String docId) {
        logger.debug("[MOCK] card({})", docId);

        Map<String, Object> response = new HashMap<>();
        response.put("id", docId);
        response.put("userName", "John Doe");
        response.put("userId", "USER001");
        response.put("officerName", "Officer Smith");
        response.put("custName", "Test Customer Ltd");
        response.put("custCountry", "LV");
        response.put("custAccount", "LV80HABA0551000000001");
        response.put("grpId", "GRP001");
        response.put("grpName", "Premium Customers");
        response.put("prodName", "Visa Gold");
        response.put("prodCCY", "EUR");
        response.put("pan", "4111111111111111");
        response.put("email", "john.doe@example.com");
        response.put("phone", "+371-67890123");
        response.put("mobile", "+371-29876543");
        response.put("chargesAcsdId", "ACC001");
        response.put("interestIban", "LV80HABA0551000000002");
        response.put("issueForAccount", "LV80HABA0551000000001");
        response.put("issueForCustomer", "Test Customer Ltd");
        response.put("cardStan", "STAN001");
        response.put("cardStatusFrom", "NEW");
        response.put("cardStatusTo", "ACTIVE");
        response.put("cortexStatus", 0);
        response.put("cortexDetails", "Card issued successfully");
        response.put("lostType", null);
        response.put("lostDate", null);
        response.put("ffText", "Additional notes for card");
        response.put("location", "MAIN_OFFICE");
        response.put("infoToCustomer", "Your card has been processed");
        response.put("infoToBank", "Internal notes for bank");
        response.put("signTime", new Date());
        response.put("signDevType", 1);
        response.put("signDevId", "DEV001");
        response.put("signKey1", "KEY1_MOCK");
        response.put("signKey2", "KEY2_MOCK");

        return response;
    }

    @Override
    public Map<String, Object> getLostAddr(String docId) {
        logger.debug("[MOCK] getLostAddr({})", docId);

        Map<String, Object> response = new HashMap<>();
        response.put("documentId", docId);
        response.put("lostCountry", "Spain");
        response.put("lostCity", "Barcelona");

        return response;
    }

    @Override
    public Map<String, Object> getIssueAddr(String docId) {
        logger.debug("[MOCK] getIssueAddr({})", docId);

        Map<String, Object> response = new HashMap<>();
        response.put("documentId", docId);
        response.put("receivingType", 1);
        response.put("office", "Main Branch Office");
        response.put("country", "Latvia");
        response.put("address", "Kalku iela 15, Riga, LV-1050");

        return response;
    }
}

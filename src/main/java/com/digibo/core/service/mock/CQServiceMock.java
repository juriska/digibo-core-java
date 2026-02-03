package com.digibo.core.service.mock;

import com.digibo.core.service.CQService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * CQServiceMock - Mock implementation of CQService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class CQServiceMock implements CQService {

    private static final Logger logger = LoggerFactory.getLogger(CQServiceMock.class);

    @Override
    public List<Map<String, Object>> find(String custId, String custName, String userLogin,
                                           Long officerId, String docClass, String docId,
                                           String statuses, Date createdFrom, Date createdTill) {
        logger.debug("[MOCK] CQService.find() called");

        List<Map<String, Object>> results = new ArrayList<>();

        Map<String, Object> doc1 = new HashMap<>();
        doc1.put("PID", 1001L);
        doc1.put("STATUS", 5);
        doc1.put("CLASS", 100);
        doc1.put("CREATED", new Date());
        doc1.put("DOCNUMBER", "CQ-2024-001");
        doc1.put("LOGIN", "user.cq1");
        doc1.put("ITB", 45);
        doc1.put("CUST_NAME", "Test Customer 1");
        results.add(doc1);

        Map<String, Object> doc2 = new HashMap<>();
        doc2.put("PID", 1002L);
        doc2.put("STATUS", 24);
        doc2.put("CLASS", 100);
        doc2.put("CREATED", new Date(System.currentTimeMillis() - 86400000));
        doc2.put("DOCNUMBER", "CQ-2024-002");
        doc2.put("LOGIN", "user.cq2");
        doc2.put("ITB", 30);
        doc2.put("CUST_NAME", "Test Customer 2");
        results.add(doc2);

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

        logger.debug("[MOCK] Returning {} CQ documents", results.size());
        return results;
    }

    @Override
    public List<Map<String, Object>> findMy(String docClass) {
        logger.debug("[MOCK] CQService.findMy({}) called", docClass);

        List<Map<String, Object>> results = new ArrayList<>();

        Map<String, Object> doc = new HashMap<>();
        doc.put("PID", 1001L);
        doc.put("STATUS", 5);
        doc.put("CLASS", 100);
        doc.put("CREATED", new Date());
        doc.put("DOCNUMBER", "CQ-2024-001");
        doc.put("LOGIN", "user.cq1");
        doc.put("ITB", 45);
        results.add(doc);

        logger.debug("[MOCK] Returning {} CQ documents", results.size());
        return results;
    }

    @Override
    public Map<String, Object> cq(String docId) {
        logger.debug("[MOCK] CQService.cq({}) called", docId);

        Map<String, Object> response = new HashMap<>();
        response.put("id", docId);
        response.put("docNo", "CQ-MOCK-001");
        response.put("userName", "Mock User (mock.user)");
        response.put("userId", "010190-00000");
        response.put("officerName", "Mock Officer");
        response.put("custName", "Mock Customer (1000)");
        response.put("custAccount", "LV00MOCK0000000000001 EUR");
        response.put("globusNo", "MOCK123456");
        response.put("infoToCustomer", "Mock info to customer");
        response.put("infoToBank", "Mock info to bank");
        response.put("authName", "Mock");
        response.put("authSurname", "User");
        response.put("authLegalId", "010190-00000");
        response.put("authPassportNo", "LV0000000");
        response.put("authPassportCountry", "LV");
        response.put("authPassportInst", "PMLP");
        response.put("authPhone", "+371 20000000");
        response.put("authFax", null);
        response.put("authEmail", "mock.user@example.com");
        response.put("contactPersonName", "Mock");
        response.put("contactPersonSurname", "Contact");
        response.put("contactPersonPhone", "+371 20000000");
        response.put("contactPersonEmail", "mock.contact@example.com");
        response.put("econimicActivity", "Mock Activity");
        response.put("recipients", "LV, EE");
        response.put("suppliers", "LV, EE");
        response.put("incomingPayments", "LV");
        response.put("outgoingPayments", "LV");
        response.put("financeClients", "LV");
        response.put("location", "LV");
        response.put("signTime", new Date());
        response.put("signDevType", 5);
        response.put("signDevId", "MOCK_DEVICE");
        response.put("signKey1", "MOCK_KEY1");
        response.put("signKey2", "MOCK_KEY2");

        return response;
    }

    @Override
    public List<Map<String, Object>> getExtensions(String docId) {
        logger.debug("[MOCK] CQService.getExtensions({}) called", docId);

        List<Map<String, Object>> extensions = new ArrayList<>();

        Map<String, Object> ext1 = new HashMap<>();
        ext1.put("DICTIONARY_ID", 1);
        ext1.put("ADDITIONAL_INFO", "Extension info 1");
        ext1.put("BLOCK_NUMBER", 1);
        extensions.add(ext1);

        Map<String, Object> ext2 = new HashMap<>();
        ext2.put("DICTIONARY_ID", 2);
        ext2.put("ADDITIONAL_INFO", "Extension info 2");
        ext2.put("BLOCK_NUMBER", 1);
        extensions.add(ext2);

        return extensions;
    }
}

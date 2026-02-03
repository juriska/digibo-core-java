package com.digibo.core.service.mock;

import com.digibo.core.service.CRUService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * CRUServiceMock - Mock implementation of CRUService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class CRUServiceMock implements CRUService {

    private static final Logger logger = LoggerFactory.getLogger(CRUServiceMock.class);

    @Override
    public List<Map<String, Object>> find(String custId, String docId, String statuses,
                                           Date createdFrom, Date createdTill) {
        logger.debug("[MOCK] CRUService.find() called");

        List<Map<String, Object>> results = new ArrayList<>();

        Map<String, Object> doc1 = new HashMap<>();
        doc1.put("DOCUMENT_ID", 3001L);
        doc1.put("STATUS", 1);
        doc1.put("CUSTOMER_ID", "CUST001");
        doc1.put("CREATED", new Date());
        doc1.put("DOCNUMBER", "CRU-2024-001");
        results.add(doc1);

        Map<String, Object> doc2 = new HashMap<>();
        doc2.put("DOCUMENT_ID", 3002L);
        doc2.put("STATUS", 5);
        doc2.put("CUSTOMER_ID", "CUST002");
        doc2.put("CREATED", new Date(System.currentTimeMillis() - 86400000));
        doc2.put("DOCNUMBER", "CRU-2024-002");
        results.add(doc2);

        // Apply filters
        if (docId != null) {
            results.removeIf(d -> !String.valueOf(d.get("DOCUMENT_ID")).equals(docId));
        }
        if (custId != null) {
            results.removeIf(d -> !custId.equals(d.get("CUSTOMER_ID")));
        }
        if (statuses != null && !statuses.isEmpty()) {
            List<Integer> statusList = Arrays.stream(statuses.split(","))
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .toList();
            results.removeIf(d -> !statusList.contains((Integer) d.get("STATUS")));
        }

        logger.debug("[MOCK] Returning {} CRU documents", results.size());
        return results;
    }

    @Override
    public Map<String, Object> cru(String docId) {
        logger.debug("[MOCK] CRUService.cru({}) called", docId);

        Map<String, Object> response = new HashMap<>();
        response.put("id", docId);
        response.put("docNo", "CRU-MOCK-001");
        response.put("userName", "Mock User (mock.user)");
        response.put("userId", "010190-00000");
        response.put("custName", "Mock Customer (1000)");
        response.put("globusNo", "MOCK123456");
        response.put("location", "LV");
        response.put("infoToCustomer", "Mock info to customer");
        response.put("infoToBank", "Mock info to bank");
        response.put("signTime", new Date());
        response.put("signDevType", 5);
        response.put("signDevId", "MOCK_DEVICE");
        response.put("signKey1", "MOCK_KEY1");
        response.put("signKey2", "MOCK_KEY2");

        return response;
    }
}

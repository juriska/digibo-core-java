package com.digibo.core.service.mock;

import com.digibo.core.service.CrontoDocService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * CrontoDocServiceMock - Mock implementation of CrontoDocService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class CrontoDocServiceMock implements CrontoDocService {

    private static final Logger logger = LoggerFactory.getLogger(CrontoDocServiceMock.class);

    @Override
    public List<Map<String, Object>> find(String custId, String custName, String userLogin,
                                           Long officerId, String pType, String docId,
                                           String statuses, Date createdFrom, Date createdTill) {
        logger.debug("[MOCK] CrontoDocService.find() called");

        List<Map<String, Object>> results = new ArrayList<>();

        Map<String, Object> doc1 = new HashMap<>();
        doc1.put("PID", 1001L);
        doc1.put("STATUS", 5);
        doc1.put("CLASS", 1028);
        doc1.put("CREATED", new Date());
        doc1.put("DOCNUMBER", "CRN-2024-001");
        doc1.put("LOGIN", "cronto.user1");
        doc1.put("ITB", 45);
        doc1.put("CR_AMOUNT", "0.00");
        doc1.put("DB_AMOUNT", "0.00");
        doc1.put("CR_CCY", "EUR");
        doc1.put("DB_CCY", "EUR");
        doc1.put("PROCESSEDBY", null);
        results.add(doc1);

        Map<String, Object> doc2 = new HashMap<>();
        doc2.put("PID", 1002L);
        doc2.put("STATUS", 24);
        doc2.put("CLASS", 1028);
        doc2.put("CREATED", new Date(System.currentTimeMillis() - 86400000));
        doc2.put("DOCNUMBER", "CRN-2024-002");
        doc2.put("LOGIN", "cronto.user2");
        doc2.put("ITB", 30);
        doc2.put("CR_AMOUNT", "0.00");
        doc2.put("DB_AMOUNT", "0.00");
        doc2.put("CR_CCY", "EUR");
        doc2.put("DB_CCY", "EUR");
        doc2.put("PROCESSEDBY", 101);
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

        logger.debug("[MOCK] Returning {} Cronto documents", results.size());
        return results;
    }

    @Override
    public int setProcessing(String docId) {
        logger.debug("[MOCK] CrontoDocService.setProcessing({}) called", docId);
        return 101; // Mock officer ID
    }

    @Override
    public List<Map<String, Object>> findMy(Long officerId) {
        logger.debug("[MOCK] CrontoDocService.findMy({}) called", officerId);

        List<Map<String, Object>> results = new ArrayList<>();

        Map<String, Object> doc = new HashMap<>();
        doc.put("PID", 1001L);
        doc.put("STATUS", 5);
        doc.put("CLASS", 1028);
        doc.put("CREATED", new Date());
        doc.put("DOCNUMBER", "CRN-2024-001");
        doc.put("LOGIN", "cronto.user1");
        doc.put("ITB", 45);
        doc.put("CR_AMOUNT", "0.00");
        doc.put("DB_AMOUNT", "0.00");
        doc.put("CR_CCY", "EUR");
        doc.put("DB_CCY", "EUR");
        doc.put("PROCESSEDBY", officerId != null && officerId > 0 ? officerId : null);

        if (officerId == null || officerId == 0) {
            // Return new unassigned documents
            if (doc.get("PROCESSEDBY") == null) {
                results.add(doc);
            }
        } else {
            // Return documents assigned to this officer
            doc.put("PROCESSEDBY", officerId);
            results.add(doc);
        }

        logger.debug("[MOCK] Returning {} Cronto documents", results.size());
        return results;
    }
}

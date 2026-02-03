package com.digibo.core.service.mock;

import com.digibo.core.service.DRService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * DRServiceMock - Mock implementation of DRService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class DRServiceMock implements DRService {

    private static final Logger logger = LoggerFactory.getLogger(DRServiceMock.class);

    @Override
    public List<Map<String, Object>> find(String custId, String custName, String userLogin,
                                           Long officerId, Long pClassId, String pTerm,
                                           String amountFrom, String amountTill, String currencies,
                                           String docId, String statuses,
                                           Date createdFrom, Date createdTill) {
        logger.debug("[MOCK] DRService.find() called");

        List<Map<String, Object>> results = new ArrayList<>();

        Map<String, Object> doc1 = new HashMap<>();
        doc1.put("PID", 5001L);
        doc1.put("STATUS", 1);
        doc1.put("CLASS_ID", 80);
        doc1.put("CREATED", new Date());
        doc1.put("LOGIN", "user.dr1");
        doc1.put("DB_AMOUNT", "10000.00");
        doc1.put("CCY", "EUR");
        doc1.put("TERM", "12M");
        results.add(doc1);

        Map<String, Object> doc2 = new HashMap<>();
        doc2.put("PID", 5002L);
        doc2.put("STATUS", 5);
        doc2.put("CLASS_ID", 80);
        doc2.put("CREATED", new Date(System.currentTimeMillis() - 86400000));
        doc2.put("LOGIN", "user.dr2");
        doc2.put("DB_AMOUNT", "25000.00");
        doc2.put("CCY", "EUR");
        doc2.put("TERM", "24M");
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
        if (pClassId != null) {
            results.removeIf(d -> !pClassId.equals(((Number) d.get("CLASS_ID")).longValue()));
        }
        if (currencies != null) {
            results.removeIf(d -> !currencies.equals(d.get("CCY")));
        }

        logger.debug("[MOCK] Returning {} DR documents", results.size());
        return results;
    }

    @Override
    public Map<String, Object> dr(String docId) {
        logger.debug("[MOCK] DRService.dr({}) called", docId);

        Map<String, Object> response = new HashMap<>();
        response.put("id", docId);
        response.put("classId", 80);
        response.put("globusNo", "MOCK123456");
        response.put("userName", "Mock User (mock.user)");
        response.put("userId", "010190-00000");
        response.put("officerName", "Mock Officer");
        response.put("custName", "Mock Customer (1000)");
        response.put("custAccount", "LV00MOCK0000000000001 EUR");
        response.put("rate", "3.0");
        response.put("product", "Standard Deposit");
        response.put("frequency", "Monthly");
        response.put("benName", "Mock Beneficiary");
        response.put("benIban", "LV00MOCK0000000000001 EUR");
        response.put("agreement", "Mock agreement text...");
        response.put("location", "LV");
        response.put("valueDate", new Date());
        response.put("fromContract", null);
        response.put("loyaltyBonus", "0.0");
        response.put("startAmount", "10000.00");
        response.put("startCcy", "EUR");
        response.put("currentAmount", "10000.00");
        response.put("currentCcy", "EUR");
        response.put("replenishmentAmount", "0.00");
        response.put("replenishmentCcy", "EUR");
        response.put("startDate", new Date());
        response.put("termDate", new Date(System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000));
        response.put("infoToCustomer", "Mock info to customer");
        response.put("infoToBank", "Mock info to bank");
        response.put("rejector", null);
        response.put("rejectDate", null);
        response.put("typeId", 1);

        return response;
    }
}

package com.digibo.core.service.mock;

import com.digibo.core.service.DDService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * DDServiceMock - Mock implementation of DDService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class DDServiceMock implements DDService {

    private static final Logger logger = LoggerFactory.getLogger(DDServiceMock.class);

    @Override
    public List<Map<String, Object>> find(String custId, String custName, String userLogin,
                                           Long officerId, String pType, String docId,
                                           String statuses, Date createdFrom, Date createdTill) {
        logger.debug("[MOCK] DDService.find() called");

        List<Map<String, Object>> results = new ArrayList<>();

        Map<String, Object> doc1 = new HashMap<>();
        doc1.put("PID", 601L);
        doc1.put("STATUS", 1);
        doc1.put("CLASS", 35);
        doc1.put("CREATED", new Date());
        doc1.put("DOCNUMBER", "DD-2024-001");
        doc1.put("LOGIN", "user.dd1");
        doc1.put("ITB", 80);
        doc1.put("CR_AMOUNT", "150.00");
        doc1.put("DB_AMOUNT", "150.00");
        doc1.put("CR_CCY", "EUR");
        doc1.put("DB_CCY", "EUR");
        results.add(doc1);

        Map<String, Object> doc2 = new HashMap<>();
        doc2.put("PID", 602L);
        doc2.put("STATUS", 5);
        doc2.put("CLASS", 35);
        doc2.put("CREATED", new Date(System.currentTimeMillis() - 86400000));
        doc2.put("DOCNUMBER", "DD-2024-002");
        doc2.put("LOGIN", "user.dd2");
        doc2.put("ITB", 55);
        doc2.put("CR_AMOUNT", "89.99");
        doc2.put("DB_AMOUNT", "89.99");
        doc2.put("CR_CCY", "EUR");
        doc2.put("DB_CCY", "EUR");
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

        logger.debug("[MOCK] Returning {} DD documents", results.size());
        return results;
    }

    @Override
    public Map<String, Object> dd(String ddId) {
        logger.debug("[MOCK] DDService.dd({}) called", ddId);

        Map<String, Object> response = new HashMap<>();
        response.put("id", ddId);
        response.put("userName", "Utility Customer (user.dd1)");
        response.put("userId", "UC001");
        response.put("officerName", "Officer Wilson");
        response.put("custName", "John Homeowner (80001)");
        response.put("custAccount", "LV80HABA0551000080001 EUR");
        response.put("globusNo", "GLB/DD/001");
        response.put("agreement", "Direct debit agreement for utility payments");
        response.put("contractId", "UTIL-2024-001");
        response.put("firstDate", new Date());
        response.put("lastDate", new Date(System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000));
        response.put("benName", "Electric Company");
        response.put("utPhoneNo", "+371 67000111");
        response.put("amountLimit", "300.00");
        response.put("abCode", "ELEC001");
        response.put("abName", "John");
        response.put("abSurname", "Homeowner");
        response.put("abAcnt", "LV80HABA0551000080001");
        response.put("abId", "010180-12345");
        response.put("giroNr", null);
        response.put("legalAddr", "Main Branch Office");
        response.put("contactAddr", "Riga, Brivibas 100");
        response.put("email", "john.homeowner@email.com");
        response.put("location", "LV");
        response.put("rejector", null);
        response.put("rejectDate", null);
        response.put("itc", "Direct debit authorization confirmed");
        response.put("itb", "Utility payment direct debit");

        return response;
    }
}

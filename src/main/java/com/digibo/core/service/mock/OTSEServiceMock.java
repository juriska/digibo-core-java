package com.digibo.core.service.mock;

import com.digibo.core.service.OTSEService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * OTSEServiceMock - Mock implementation of OTSEService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class OTSEServiceMock implements OTSEService {

    private static final Logger logger = LoggerFactory.getLogger(OTSEServiceMock.class);

    private static final List<Map<String, Object>> mockDocuments = new ArrayList<>();
    private static final List<Map<String, Object>> mockCustomers = new ArrayList<>();

    static {
        // Mock OTSE documents
        Map<String, Object> doc1 = new HashMap<>();
        doc1.put("ID", "OTSE001");
        doc1.put("NAME", "John Doe");
        doc1.put("LOGIN", "jdoe");
        doc1.put("PERSONAL_ID", "123456789");
        doc1.put("STATUS_ID", 6);
        doc1.put("CREATED", new Date());
        mockDocuments.add(doc1);

        Map<String, Object> doc2 = new HashMap<>();
        doc2.put("ID", "OTSE002");
        doc2.put("NAME", "Jane Smith");
        doc2.put("LOGIN", "jsmith");
        doc2.put("PERSONAL_ID", "987654321");
        doc2.put("STATUS_ID", 61);
        doc2.put("CREATED", new Date());
        mockDocuments.add(doc2);

        // Mock customers
        Map<String, Object> cust1 = new HashMap<>();
        cust1.put("ID", "CUST001");
        cust1.put("NAME", "John Doe");
        cust1.put("PERSONAL_ID", "123456789");
        cust1.put("EMAIL", "john.doe@example.com");
        mockCustomers.add(cust1);

        Map<String, Object> cust2 = new HashMap<>();
        cust2.put("ID", "CUST002");
        cust2.put("NAME", "Jane Smith");
        cust2.put("PERSONAL_ID", "987654321");
        cust2.put("EMAIL", "jane.smith@example.com");
        mockCustomers.add(cust2);
    }

    @Override
    public List<Map<String, Object>> find(String custId, String custName, String userLogin,
                                           String personalId, String docId) {
        logger.debug("[MOCK] BOOTSE.find() called");

        List<Map<String, Object>> results = new ArrayList<>(mockDocuments);

        if (custName != null && !custName.isEmpty()) {
            String nameLower = custName.toLowerCase();
            results.removeIf(d -> {
                String name = (String) d.get("NAME");
                return name == null || !name.toLowerCase().contains(nameLower);
            });
        }

        if (userLogin != null && !userLogin.isEmpty()) {
            String loginLower = userLogin.toLowerCase();
            results.removeIf(d -> {
                String login = (String) d.get("LOGIN");
                return login == null || !login.toLowerCase().contains(loginLower);
            });
        }

        if (personalId != null && !personalId.isEmpty()) {
            results.removeIf(d -> !personalId.equals(d.get("PERSONAL_ID")));
        }

        if (docId != null && !docId.isEmpty()) {
            results.removeIf(d -> !docId.equals(d.get("ID")));
        }

        logger.debug("[MOCK] Returning {} OTSE documents", results.size());
        return results;
    }

    @Override
    public List<Map<String, Object>> findNew() {
        logger.debug("[MOCK] BOOTSE.find_new() called");

        List<Map<String, Object>> results = new ArrayList<>();
        for (Map<String, Object> doc : mockDocuments) {
            Integer statusId = (Integer) doc.get("STATUS_ID");
            if (statusId != null && (statusId == 6 || statusId == 61)) {
                results.add(doc);
            }
        }

        logger.debug("[MOCK] Returning {} new OTSE documents", results.size());
        return results;
    }

    @Override
    public Map<String, Object> getCustomer(String customerId) {
        logger.debug("[MOCK] BOOTSE.get_customer({}) called", customerId);

        Map<String, Object> result = new HashMap<>();

        Map<String, Object> customer = mockCustomers.stream()
                .filter(c -> customerId.equals(c.get("ID")))
                .findFirst()
                .orElse(null);

        if (customer != null) {
            result.put("resultCode", 0);
            result.put("customer", customer);
        } else {
            result.put("resultCode", -1);
            result.put("customer", null);
        }

        return result;
    }

    @Override
    public Map<String, Object> bind(String wocId, String custId, String userId, String docId) {
        logger.debug("[MOCK] BOOTSE.bind({}, {}, {}, {}) called", wocId, custId, userId, docId);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("wocId", wocId);
        result.put("custId", custId);
        result.put("userId", userId);
        result.put("docId", docId);

        logger.debug("[MOCK] Binding successful");
        return result;
    }

    @Override
    public Map<String, Object> setWocStatus(String wocId, Integer status, Integer subStatus) {
        logger.debug("[MOCK] BOOTSE.set_woc_status({}, {}, {}) called", wocId, status, subStatus);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("wocId", wocId);
        result.put("status", status);
        result.put("subStatus", subStatus);

        logger.debug("[MOCK] Status update successful");
        return result;
    }
}

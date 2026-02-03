package com.digibo.core.service.mock;

import com.digibo.core.service.ProdKitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * ProdKitServiceMock - Mock implementation of ProdKitService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class ProdKitServiceMock implements ProdKitService {

    private static final Logger logger = LoggerFactory.getLogger(ProdKitServiceMock.class);

    private static final List<Map<String, Object>> mockProdKitOrders = new ArrayList<>();

    static {
        Map<String, Object> order1 = new HashMap<>();
        order1.put("PID", "PK001");
        order1.put("CUSTID", "CUST001");
        order1.put("CUSTNAME", "John Doe");
        order1.put("LOGIN", "jdoe");
        order1.put("STATUS", 1);
        order1.put("CLASS_ID", 100);
        order1.put("CREATED", new Date());
        order1.put("PROCESSEDBY", null);
        mockProdKitOrders.add(order1);

        Map<String, Object> order2 = new HashMap<>();
        order2.put("PID", "PK002");
        order2.put("CUSTID", "CUST002");
        order2.put("CUSTNAME", "Jane Smith");
        order2.put("LOGIN", "jsmith");
        order2.put("STATUS", 13);
        order2.put("CLASS_ID", 100);
        order2.put("CREATED", new Date());
        order2.put("PROCESSEDBY", 1001);
        mockProdKitOrders.add(order2);
    }

    @Override
    public List<Map<String, Object>> find(String custId, String custName, String userLogin,
                                           String docId, String statuses, String docClass,
                                           Date createdFrom, Date createdTill) {
        logger.debug("[MOCK] BOProdKit.find() called");

        List<Map<String, Object>> results = new ArrayList<>(mockProdKitOrders);

        if (custId != null && !custId.isEmpty()) {
            results.removeIf(d -> !custId.equals(d.get("CUSTID")));
        }

        if (custName != null && !custName.isEmpty()) {
            String nameLower = custName.toLowerCase();
            results.removeIf(d -> {
                String name = (String) d.get("CUSTNAME");
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

        if (docId != null && !docId.isEmpty()) {
            results.removeIf(d -> !docId.equals(d.get("PID")));
        }

        if (statuses != null && !statuses.isEmpty()) {
            List<Integer> statusList = Arrays.stream(statuses.split(","))
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .toList();
            results.removeIf(d -> !statusList.contains((Integer) d.get("STATUS")));
        }

        if (docClass != null && !docClass.isEmpty()) {
            List<Integer> classList = Arrays.stream(docClass.split(","))
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .toList();
            results.removeIf(d -> !classList.contains((Integer) d.get("CLASS_ID")));
        }

        logger.debug("[MOCK] Returning {} filtered custody orders", results.size());
        return results;
    }

    @Override
    public List<Map<String, Object>> findMy(Integer officerId) {
        logger.debug("[MOCK] BOProdKit.find_my({}) called", officerId);

        List<Map<String, Object>> results = new ArrayList<>();

        if (officerId == null || officerId == 0) {
            // Return new orders (no processor assigned)
            for (Map<String, Object> order : mockProdKitOrders) {
                if (order.get("PROCESSEDBY") == null) {
                    results.add(order);
                }
            }
            logger.debug("[MOCK] Returning {} new custody orders", results.size());
        } else {
            // Return orders for specific officer
            for (Map<String, Object> order : mockProdKitOrders) {
                if (officerId.equals(order.get("PROCESSEDBY"))) {
                    results.add(order);
                }
            }
            logger.debug("[MOCK] Returning {} custody orders for officer {}", results.size(), officerId);
        }

        return results;
    }

    @Override
    public Map<String, Object> setProcessing(String docId) {
        logger.debug("[MOCK] BOProdKit.set_processing({}) called", docId);

        Map<String, Object> order = mockProdKitOrders.stream()
                .filter(o -> docId.equals(o.get("PID")))
                .findFirst()
                .orElse(null);

        Map<String, Object> result = new HashMap<>();

        if (order != null) {
            int mockOfficerId = 1001;
            order.put("PROCESSEDBY", mockOfficerId);

            result.put("success", true);
            result.put("documentId", docId);
            result.put("changeOfficerId", mockOfficerId);
            result.put("result", mockOfficerId);
        } else {
            result.put("success", false);
            result.put("documentId", docId);
            result.put("changeOfficerId", 0);
            result.put("result", 0);
        }

        return result;
    }

    @Override
    public Map<String, Object> prodkit(String docId) {
        logger.debug("[MOCK] BOProdKit.prodkit({}) called", docId);

        Map<String, Object> result = new HashMap<>();
        result.put("id", docId);
        result.put("userName", "Mock User (mockuser)");
        result.put("legalId", "010190-12345");
        result.put("officerName", "Mock Officer");
        result.put("custName", "Mock Customer");
        result.put("globusNo", "GLOBUS123456");
        result.put("location", "LV");
        result.put("fromAccount", "LV00MOCK0000000000001 EUR");
        result.put("utPhoneNumber", "+371 20000000");
        result.put("phoneMobile", "+371 29000000");
        result.put("authName", "John");
        result.put("authSurname", "Doe");
        result.put("authLegalId", "010190-12345");
        result.put("authPassportNo", "LV1234567");
        result.put("authPassportCountry", "LV");
        result.put("authPassportInst", "PMLP");
        result.put("authPhone", "+371 20000000");
        result.put("authFax", null);
        result.put("authEmail", "john.doe@email.com");
        result.put("infoToCustomer", "Mock custody order information for customer");
        result.put("infoToBank", "Mock custody order information for bank. Document " + docId);
        result.put("channelId", 5);
        result.put("signTime", new Date());
        result.put("signDevType", 5);
        result.put("signDevId", "DEVICE_MOCK001");
        result.put("signKey1", "KEY1_MOCK");
        result.put("signKey2", "KEY2_MOCK");
        result.put("signRSA", "RSA_SIG_MOCK");

        return result;
    }
}

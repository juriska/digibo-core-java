package com.digibo.core.service.mock;

import com.digibo.core.service.PamoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * PamoServiceMock - Mock implementation of PamoService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class PamoServiceMock implements PamoService {

    private static final Logger logger = LoggerFactory.getLogger(PamoServiceMock.class);

    private static final List<Map<String, Object>> mockPamoOrders = new ArrayList<>();

    static {
        Map<String, Object> order1 = new HashMap<>();
        order1.put("PID", 401L);
        order1.put("CLASS", 50);
        order1.put("STATUS", 1);
        order1.put("CREATED", new Date());
        order1.put("DOCNUMBER", "PAMO-2024-001");
        order1.put("CHANNEL", 5);
        order1.put("LOGIN", "user.invest1");
        order1.put("ISINCODE", "LV0000000001");
        order1.put("OPERATION", "BUY");
        mockPamoOrders.add(order1);

        Map<String, Object> order2 = new HashMap<>();
        order2.put("PID", 402L);
        order2.put("CLASS", 50);
        order2.put("STATUS", 5);
        order2.put("CREATED", new Date());
        order2.put("DOCNUMBER", "PAMO-2024-002");
        order2.put("CHANNEL", 5);
        order2.put("LOGIN", "user.invest2");
        order2.put("ISINCODE", "LV0000000002");
        order2.put("OPERATION", "SELL");
        mockPamoOrders.add(order2);
    }

    @Override
    public List<Map<String, Object>> find(String custId, String custName, String userLogin,
                                           String docClass, String pIsin, String docId,
                                           String statuses, Date createdFrom, Date createdTill) {
        logger.debug("[MOCK] BOPAMO.find() called");

        List<Map<String, Object>> results = new ArrayList<>(mockPamoOrders);

        if (docId != null && !docId.isEmpty()) {
            results.removeIf(d -> !docId.equals(String.valueOf(d.get("PID"))));
        }

        if (pIsin != null && !pIsin.isEmpty()) {
            results.removeIf(d -> {
                String isin = (String) d.get("ISINCODE");
                return isin == null || !isin.contains(pIsin);
            });
        }

        if (statuses != null && !statuses.isEmpty()) {
            List<Integer> statusList = Arrays.stream(statuses.split(","))
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .toList();
            results.removeIf(d -> !statusList.contains((Integer) d.get("STATUS")));
        }

        logger.debug("[MOCK] Returning {} PAMO documents", results.size());
        return results;
    }

    @Override
    public List<Map<String, Object>> findMy(String docClass) {
        logger.debug("[MOCK] BOPAMO.find_my({}) called", docClass);

        List<Map<String, Object>> results = new ArrayList<>();

        Map<String, Object> order = new HashMap<>();
        order.put("PID", 401L);
        order.put("CLASS", 50);
        order.put("STATUS", 13);
        order.put("CREATED", new Date());
        order.put("DOCNUMBER", "PAMO-2024-001");
        order.put("CHANNEL", 5);
        order.put("LOGIN", "user.invest1");
        order.put("ISINCODE", "LV0000000001");
        order.put("OPERATION", "BUY");
        results.add(order);

        logger.debug("[MOCK] Returning {} user PAMO documents", results.size());
        return results;
    }

    @Override
    public Map<String, Object> pamo(String pamoId) {
        logger.debug("[MOCK] BOPAMO.pamo({}) called", pamoId);

        Map<String, Object> result = new HashMap<>();
        result.put("id", pamoId);
        result.put("docNo", "PAMO-2024-001");
        result.put("userName", "Investor Alice (user.invest1)");
        result.put("userId", "IA001");
        result.put("officerName", "Investment Officer Davis");
        result.put("custName", "Alice Peterson (60001)");
        result.put("custAccount", "LV80HABA0551000060001 EUR");
        result.put("globusNo", "GLB/PAM/001");
        result.put("portfolioId", "PORT-001");
        result.put("operation", "BUY");
        result.put("isinCode", "LV0000000001");
        result.put("fundName", "Baltic Growth Fund");
        result.put("investVolume", "100");
        result.put("creditAmnt", "10000.00 EUR");
        result.put("text", "Purchase of 100 units of Baltic Growth Fund");
        result.put("location", "LV");
        result.put("itc", "Investment order confirmed");
        result.put("itb", "Standard investment order processing");

        return result;
    }
}

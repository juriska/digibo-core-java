package com.digibo.core.service.mock;

import com.digibo.core.service.FiAccOpenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * FiAccOpenServiceMock - Mock implementation of FiAccOpenService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class FiAccOpenServiceMock implements FiAccOpenService {

    private static final Logger logger = LoggerFactory.getLogger(FiAccOpenServiceMock.class);

    @Override
    public List<Map<String, Object>> find(
            String custId,
            String custName,
            String userLogin,
            String docId,
            String statuses,
            String docClass,
            Date createdFrom,
            Date createdTill
    ) {
        logger.debug("[MOCK] find()");

        List<Map<String, Object>> results = new ArrayList<>();

        Map<String, Object> doc1 = new HashMap<>();
        doc1.put("DOC_ID", "FIAO001");
        doc1.put("CUST_ID", "CUST001");
        doc1.put("CUST_NAME", "John Doe");
        doc1.put("USER_LOGIN", "jdoe");
        doc1.put("DOC_CLASS", 321);
        doc1.put("STATUS_ID", 1);
        doc1.put("STATUS_NAME", "New");
        doc1.put("CREATED_DATE", new Date());
        doc1.put("OFFICER_ID", 1001L);
        results.add(doc1);

        Map<String, Object> doc2 = new HashMap<>();
        doc2.put("DOC_ID", "FIAO002");
        doc2.put("CUST_ID", "CUST002");
        doc2.put("CUST_NAME", "Jane Smith");
        doc2.put("USER_LOGIN", "jsmith");
        doc2.put("DOC_CLASS", 322);
        doc2.put("STATUS_ID", 2);
        doc2.put("STATUS_NAME", "In Progress");
        doc2.put("CREATED_DATE", new Date(System.currentTimeMillis() - 86400000));
        doc2.put("OFFICER_ID", 1002L);
        results.add(doc2);

        return results;
    }

    @Override
    public List<Map<String, Object>> findMy(Long officerId) {
        logger.debug("[MOCK] findMy({})", officerId);

        List<Map<String, Object>> results = new ArrayList<>();

        Map<String, Object> doc = new HashMap<>();
        doc.put("DOC_ID", "FIAO001");
        doc.put("CUST_ID", "CUST001");
        doc.put("CUST_NAME", "John Doe");
        doc.put("USER_LOGIN", "jdoe");
        doc.put("DOC_CLASS", 321);
        doc.put("STATUS_ID", 1);
        doc.put("STATUS_NAME", "New");
        doc.put("CREATED_DATE", new Date());
        doc.put("OFFICER_ID", officerId != null ? officerId : 0L);
        results.add(doc);

        return results;
    }

    @Override
    public Integer setProcessing(String docId, Integer statusIdFrom, Integer statusIdTo) {
        logger.debug("[MOCK] setProcessing({}, {}, {})", docId, statusIdFrom, statusIdTo);
        return 1001; // Return officer ID as success indicator
    }
}

package com.digibo.core.service.mock;

import com.digibo.core.service.VSAAAdvAppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * VSAAAdvAppServiceMock - Mock implementation of VSAAAdvAppService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class VSAAAdvAppServiceMock implements VSAAAdvAppService {

    private static final Logger logger = LoggerFactory.getLogger(VSAAAdvAppServiceMock.class);

    @Override
    public List<Map<String, Object>> find(String custId, String custName, String userLogin, String docClass,
                                           String legalId, String docId, String statuses,
                                           Date createdFrom, Date createdTill) {
        logger.debug("[MOCK] find({}, {}, {}, {}, {}, {}, {}, {}, {})",
                custId, custName, userLogin, docClass, legalId, docId, statuses, createdFrom, createdTill);

        List<Map<String, Object>> docs = new ArrayList<>();

        Map<String, Object> doc1 = new HashMap<>();
        doc1.put("DOC_ID", "VSAAAPP001");
        doc1.put("CUST_ID", "CUST001");
        doc1.put("CUST_NAME", "Test Customer");
        doc1.put("USER_LOGIN", "testuser");
        doc1.put("DOC_CLASS", "ADVISORY");
        doc1.put("STATUS", "ACTIVE");
        doc1.put("CREATED_DATE", new Date());
        docs.add(doc1);

        Map<String, Object> doc2 = new HashMap<>();
        doc2.put("DOC_ID", "VSAAAPP002");
        doc2.put("CUST_ID", "CUST002");
        doc2.put("CUST_NAME", "Another Customer");
        doc2.put("USER_LOGIN", "anotheruser");
        doc2.put("DOC_CLASS", "APPLICATION");
        doc2.put("STATUS", "PENDING");
        doc2.put("CREATED_DATE", new Date());
        docs.add(doc2);

        return docs;
    }

    @Override
    public Map<String, Object> advapp(String docId) {
        logger.debug("[MOCK] advapp({})", docId);

        Map<String, Object> advappDoc = new HashMap<>();
        advappDoc.put("id", docId);
        advappDoc.put("userName", "Test User");
        advappDoc.put("userId", "USER001");
        advappDoc.put("officerName", "Officer Smith");
        advappDoc.put("docNo", "DOC-2024-001");
        advappDoc.put("custName", "Test Customer");

        return advappDoc;
    }
}

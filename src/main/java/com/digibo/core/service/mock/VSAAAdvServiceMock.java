package com.digibo.core.service.mock;

import com.digibo.core.service.VSAAAdvService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * VSAAAdvServiceMock - Mock implementation of VSAAAdvService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class VSAAAdvServiceMock implements VSAAAdvService {

    private static final Logger logger = LoggerFactory.getLogger(VSAAAdvServiceMock.class);

    @Override
    public List<Map<String, Object>> find(String custId, String custName, String docId, String statuses,
                                           Date createdFrom, Date createdTill) {
        logger.debug("[MOCK] find({}, {}, {}, {}, {}, {})",
                custId, custName, docId, statuses, createdFrom, createdTill);

        List<Map<String, Object>> docs = new ArrayList<>();

        Map<String, Object> doc1 = new HashMap<>();
        doc1.put("DOC_ID", "VSAAADV001");
        doc1.put("CUST_ID", "CUST001");
        doc1.put("CUST_NAME", "Test Customer");
        doc1.put("STATUS", "ACTIVE");
        doc1.put("CREATED_DATE", new Date());
        docs.add(doc1);

        Map<String, Object> doc2 = new HashMap<>();
        doc2.put("DOC_ID", "VSAAADV002");
        doc2.put("CUST_ID", "CUST002");
        doc2.put("CUST_NAME", "Another Customer");
        doc2.put("STATUS", "CLOSED");
        doc2.put("CREATED_DATE", new Date(System.currentTimeMillis() - 7L * 24 * 60 * 60 * 1000));
        docs.add(doc2);

        return docs;
    }

    @Override
    public Map<String, Object> adv(String docId) {
        logger.debug("[MOCK] adv({})", docId);

        Map<String, Object> advDoc = new HashMap<>();
        advDoc.put("id", docId);
        advDoc.put("officerName", "Officer Smith");
        advDoc.put("createSentDate", new Date(System.currentTimeMillis() - 14L * 24 * 60 * 60 * 1000));
        advDoc.put("closeSentDate", null);

        return advDoc;
    }
}

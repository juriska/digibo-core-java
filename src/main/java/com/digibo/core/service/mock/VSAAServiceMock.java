package com.digibo.core.service.mock;

import com.digibo.core.service.VSAAService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * VSAAServiceMock - Mock implementation of VSAAService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class VSAAServiceMock implements VSAAService {

    private static final Logger logger = LoggerFactory.getLogger(VSAAServiceMock.class);

    @Override
    public List<Map<String, Object>> find(String userName, String legalId, Long officerId,
                                           String docId, String statuses,
                                           Date createdFrom, Date createdTill) {
        logger.debug("[MOCK] find({}, {}, {}, {}, {}, {}, {})",
                userName, legalId, officerId, docId, statuses, createdFrom, createdTill);

        List<Map<String, Object>> docs = new ArrayList<>();

        Map<String, Object> doc1 = new HashMap<>();
        doc1.put("DOC_ID", "VSAA001");
        doc1.put("USER_NAME", "John Doe");
        doc1.put("LEGAL_ID", "LID12345");
        doc1.put("STATUS", "ACTIVE");
        doc1.put("CREATED_DATE", new Date());
        docs.add(doc1);

        Map<String, Object> doc2 = new HashMap<>();
        doc2.put("DOC_ID", "VSAA002");
        doc2.put("USER_NAME", "Jane Smith");
        doc2.put("LEGAL_ID", "LID67890");
        doc2.put("STATUS", "PENDING");
        doc2.put("CREATED_DATE", new Date());
        docs.add(doc2);

        return docs;
    }

    @Override
    public Map<String, Object> vsaa(String docId) {
        logger.debug("[MOCK] vsaa({})", docId);

        Map<String, Object> vsaaDoc = new HashMap<>();
        vsaaDoc.put("id", docId);
        vsaaDoc.put("personalId", "PID123456789");
        vsaaDoc.put("signDate", new Date(System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000));
        vsaaDoc.put("lastUpdateDate", new Date());
        vsaaDoc.put("infoToCustomer", "VSAA agreement information for customer");
        vsaaDoc.put("state", "Active");
        vsaaDoc.put("street", "123 Main Street");
        vsaaDoc.put("email", "john.doe@example.com");
        vsaaDoc.put("postalCode", "LV-1010");
        vsaaDoc.put("phone", "+371-12345678");
        vsaaDoc.put("receivingType", "ELECTRONIC");
        vsaaDoc.put("officer", "Officer Smith");
        vsaaDoc.put("location", "LV");

        return vsaaDoc;
    }
}

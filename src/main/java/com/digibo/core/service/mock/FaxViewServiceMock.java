package com.digibo.core.service.mock;

import com.digibo.core.service.FaxViewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * FaxViewServiceMock - Mock implementation of FaxViewService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class FaxViewServiceMock implements FaxViewService {

    private static final Logger logger = LoggerFactory.getLogger(FaxViewServiceMock.class);

    @Override
    public List<Map<String, Object>> findMyDocuments(String classes) {
        logger.debug("[MOCK] findMyDocuments({})", classes);

        List<Map<String, Object>> results = new ArrayList<>();

        Map<String, Object> doc1 = new HashMap<>();
        doc1.put("ID", "FAX001");
        doc1.put("RECV_TIME", System.currentTimeMillis() / 1000);
        doc1.put("DOC_ID", "DOC001");
        doc1.put("OFFICER_ID", 1001L);
        doc1.put("CUST_ID", "CUST001");
        doc1.put("ACC", "ACC123456");
        doc1.put("AMNT", new BigDecimal("1500.00"));
        doc1.put("CCY", "USD");
        doc1.put("DOC_CLASS", 1);
        doc1.put("STATUS", 2);
        doc1.put("NOTE", "Test note");
        doc1.put("PARTNER", "Test Partner");
        doc1.put("SUBJECT", "Transfer Request");
        results.add(doc1);

        return results;
    }

    @Override
    public Map<String, Object> setLock(String id, Integer doc) {
        logger.debug("[MOCK] setLock({}, {})", id, doc);

        Map<String, Object> result = new HashMap<>();
        result.put("lockStatus", 0); // Success
        result.put("id", id);
        result.put("officerName", null);
        result.put("officerPhone", null);
        return result;
    }

    @Override
    public List<Map<String, Object>> loadHistory(String id) {
        logger.debug("[MOCK] loadHistory({})", id);

        List<Map<String, Object>> results = new ArrayList<>();

        Map<String, Object> history1 = new HashMap<>();
        history1.put("CHANGE_DATE", new Date());
        history1.put("CHANGE_OFFICER", "John Doe");
        history1.put("DOC_CLASS", 1);
        history1.put("CUST_ID", "CUST001");
        history1.put("ACC", "ACC123456");
        history1.put("AMNT", new BigDecimal("1500.00"));
        history1.put("CCY", "USD");
        history1.put("OFFICER_ID", 1001L);
        history1.put("PARTNER", "Test Partner");
        history1.put("SUBJ", "Transfer Request");
        history1.put("STATUS", 2);
        history1.put("NOTE", "Initial processing");
        results.add(history1);

        Map<String, Object> history2 = new HashMap<>();
        history2.put("CHANGE_DATE", new Date(System.currentTimeMillis() - 86400000));
        history2.put("CHANGE_OFFICER", "Jane Smith");
        history2.put("DOC_CLASS", 1);
        history2.put("CUST_ID", "CUST001");
        history2.put("ACC", "ACC123456");
        history2.put("AMNT", new BigDecimal("1500.00"));
        history2.put("CCY", "USD");
        history2.put("OFFICER_ID", 1002L);
        history2.put("PARTNER", "Test Partner");
        history2.put("SUBJ", "Transfer Request");
        history2.put("STATUS", 1);
        history2.put("NOTE", "Created");
        results.add(history2);

        return results;
    }

    @Override
    public List<Map<String, Object>> loadActual(String id) {
        logger.debug("[MOCK] loadActual({})", id);

        List<Map<String, Object>> results = new ArrayList<>();

        Map<String, Object> actual = new HashMap<>();
        actual.put("CHANGE_DATE", new Date());
        actual.put("CHANGE_OFFICER", "John Doe");
        actual.put("DOC_CLASS", 1);
        actual.put("CUST_ID", "CUST001");
        actual.put("ACC", "ACC123456");
        actual.put("AMNT", new BigDecimal("1500.00"));
        actual.put("CCY", "USD");
        actual.put("OFFICER_ID", 1001L);
        actual.put("PARTNER", "Test Partner");
        actual.put("SUBJ", "Transfer Request");
        actual.put("STATUS", 2);
        actual.put("NOTE", "In processing");
        results.add(actual);

        return results;
    }

    @Override
    public Long lastOfficer(Long custId, String fromAccount, Integer classId, String officers) {
        logger.debug("[MOCK] lastOfficer({}, {}, {}, {})", custId, fromAccount, classId, officers);
        return 1001L;
    }

    @Override
    public Long nextFaxId() {
        logger.debug("[MOCK] nextFaxId()");
        return 1003L;
    }

    @Override
    public Map<String, Object> nextDocumentId(Long docId, String classes) {
        logger.debug("[MOCK] nextDocumentId({}, {})", docId, classes);

        Map<String, Object> result = new HashMap<>();
        result.put("faxId", 1001L);
        result.put("nextDocId", docId != null ? docId + 1 : 1L);
        return result;
    }

    @Override
    public Map<String, Object> loadFax(String id, String docId) {
        logger.debug("[MOCK] loadFax({}, {})", id, docId);

        List<Map<String, Object>> documents = new ArrayList<>();
        Map<String, Object> doc = new HashMap<>();
        doc.put("DOC_ID", "DOC001");
        doc.put("DOC_CLASS", 1);
        doc.put("STATUS", 2);
        doc.put("CUST_ID", "CUST001");
        doc.put("OFFICER_ID", 1001L);
        documents.add(doc);

        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("docId", docId != null ? docId : "DOC001");
        result.put("fromFax", "+1-555-1234");
        result.put("fromCSid", "CSID001");
        result.put("recvTime", System.currentTimeMillis() / 1000);
        result.put("recvStatus", 1);
        result.put("faxStatus", 2);
        result.put("fTif", new byte[0]); // Empty blob for mock
        result.put("documents", documents);
        return result;
    }

    @Override
    public Map<String, Object> init(String id) {
        logger.debug("[MOCK] init({})", id);

        Map<String, Object> result = new HashMap<>();
        result.put("classId", 1);
        result.put("statusId", 2);
        return result;
    }
}

package com.digibo.core.service.mock;

import com.digibo.core.service.SmsDocService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * SmsDocServiceMock - Mock implementation of SmsDocService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class SmsDocServiceMock implements SmsDocService {

    private static final Logger logger = LoggerFactory.getLogger(SmsDocServiceMock.class);

    @Override
    public List<Map<String, Object>> find(String custId, String custName, String userLogin, Long officerId,
                                           String pType, String mobile, String docId, String statuses,
                                           Date createdFrom, Date createdTill) {
        logger.debug("[MOCK] find({}, {}, {}, {}, {}, {}, {}, {}, {}, {})",
                custId, custName, userLogin, officerId, pType, mobile, docId, statuses, createdFrom, createdTill);

        List<Map<String, Object>> docs = new ArrayList<>();

        Map<String, Object> doc1 = new HashMap<>();
        doc1.put("DOC_ID", "SMSDOC001");
        doc1.put("CUST_ID", "CUST001");
        doc1.put("CUST_NAME", "Test Customer");
        doc1.put("MOBILE", "+1234567890");
        doc1.put("STATUS", "ACTIVE");
        doc1.put("CREATED_DATE", new Date());
        docs.add(doc1);

        Map<String, Object> doc2 = new HashMap<>();
        doc2.put("DOC_ID", "SMSDOC002");
        doc2.put("CUST_ID", "CUST002");
        doc2.put("CUST_NAME", "Another Customer");
        doc2.put("MOBILE", "+0987654321");
        doc2.put("STATUS", "PENDING");
        doc2.put("CREATED_DATE", new Date());
        docs.add(doc2);

        return docs;
    }

    @Override
    public Map<String, Object> sms(String docId) {
        logger.debug("[MOCK] sms({})", docId);

        Map<String, Object> smsDoc = new HashMap<>();
        smsDoc.put("id", docId);
        smsDoc.put("userName", "Test User");
        smsDoc.put("userId", "USER001");
        smsDoc.put("officerName", "Officer Smith");
        smsDoc.put("custName", "Test Customer");
        smsDoc.put("custAccount", "ACC12345");
        smsDoc.put("agreement", "SMS Service Agreement v1.0");
        smsDoc.put("contractId", "CONTRACT001");
        smsDoc.put("mobileOperator", "Operator A");
        smsDoc.put("contactLanguage", "EN");
        smsDoc.put("location", "LV");
        smsDoc.put("itc", "Information for customer");
        smsDoc.put("smsTime", "09:00-21:00");

        return smsDoc;
    }

    @Override
    public int alreadyExists(String phone) {
        logger.debug("[MOCK] alreadyExists({})", phone);
        return "+1234567890".equals(phone) ? 1 : 0;
    }

    @Override
    public void updateDocument(String docId, String reason, Integer newStatus, Long messageId) {
        logger.debug("[MOCK] updateDocument({}, {}, {}, {})", docId, reason, newStatus, messageId);
        // Mock implementation - no action needed
    }
}

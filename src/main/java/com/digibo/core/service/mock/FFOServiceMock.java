package com.digibo.core.service.mock;

import com.digibo.core.service.FFOService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * FFOServiceMock - Mock implementation of FFOService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class FFOServiceMock implements FFOService {

    private static final Logger logger = LoggerFactory.getLogger(FFOServiceMock.class);

    @Override
    public List<Map<String, Object>> findMy() {
        logger.debug("[MOCK] findMy()");

        List<Map<String, Object>> results = new ArrayList<>();

        Map<String, Object> doc1 = new HashMap<>();
        doc1.put("ID", "FFO001");
        doc1.put("CLASS_ID", 100);
        doc1.put("STATUS_ID", 1);
        doc1.put("ORDER_DATE", new Date());
        doc1.put("DOCUMENT_NUMBER", "DOC-2024-001");
        doc1.put("CREATOR_CHANNEL_ID", 1);
        doc1.put("LOGIN", "jdoe");
        doc1.put("FF_SUBJECT", "Account Opening Request");
        doc1.put("WOC_ID", "WOC001");
        doc1.put("GLB_CUST_ID", "GLB001");
        doc1.put("SECTOR", 1);
        doc1.put("SEGMENT", "RETAIL");
        doc1.put("ISDOCUMENTATTACHED", 1);
        doc1.put("CATEGORY_ID", 10);
        doc1.put("SUBCATEGORY_ID", 101);
        doc1.put("CATEGORY_NAME", "Account Services");
        doc1.put("SUBCATEGORY_NAME", "New Account");
        doc1.put("ASSIGNEE", 1001);
        doc1.put("DOCUMENT_ATTACHED", "Y");
        results.add(doc1);

        Map<String, Object> doc2 = new HashMap<>();
        doc2.put("ID", "FFO002");
        doc2.put("CLASS_ID", 100);
        doc2.put("STATUS_ID", 2);
        doc2.put("ORDER_DATE", new Date(System.currentTimeMillis() - 86400000));
        doc2.put("DOCUMENT_NUMBER", "DOC-2024-002");
        doc2.put("CREATOR_CHANNEL_ID", 2);
        doc2.put("LOGIN", "jsmith");
        doc2.put("FF_SUBJECT", "Card Request");
        doc2.put("WOC_ID", "WOC002");
        doc2.put("GLB_CUST_ID", "GLB002");
        doc2.put("SECTOR", 2);
        doc2.put("SEGMENT", "CORPORATE");
        doc2.put("ISDOCUMENTATTACHED", 0);
        doc2.put("CATEGORY_ID", 20);
        doc2.put("SUBCATEGORY_ID", 201);
        doc2.put("CATEGORY_NAME", "Card Services");
        doc2.put("SUBCATEGORY_NAME", "New Card");
        doc2.put("ASSIGNEE", 1002);
        doc2.put("DOCUMENT_ATTACHED", "N");
        results.add(doc2);

        return results;
    }

    @Override
    public Map<String, Object> getById(String documentId) {
        logger.debug("[MOCK] getById({})", documentId);

        return findMy().stream()
                .filter(d -> documentId.equals(String.valueOf(d.get("ID"))))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Map<String, Object>> getCategories() {
        logger.debug("[MOCK] getCategories()");

        List<Map<String, Object>> results = new ArrayList<>();

        Map<String, Object> cat1 = new HashMap<>();
        cat1.put("CATEGORY_ID", 10);
        cat1.put("CATEGORY_NAME", "Account Services");
        cat1.put("SUBCATEGORY_ID", 101);
        cat1.put("SUBCATEGORY_NAME", "New Account");
        results.add(cat1);

        Map<String, Object> cat2 = new HashMap<>();
        cat2.put("CATEGORY_ID", 10);
        cat2.put("CATEGORY_NAME", "Account Services");
        cat2.put("SUBCATEGORY_ID", 102);
        cat2.put("SUBCATEGORY_NAME", "Account Modification");
        results.add(cat2);

        Map<String, Object> cat3 = new HashMap<>();
        cat3.put("CATEGORY_ID", 20);
        cat3.put("CATEGORY_NAME", "Card Services");
        cat3.put("SUBCATEGORY_ID", 201);
        cat3.put("SUBCATEGORY_NAME", "New Card");
        results.add(cat3);

        return results;
    }

    @Override
    public Map<String, Object> categorize(Long docId, Long categoryId, Long subCategoryId, Long assignee) {
        logger.debug("[MOCK] categorize({}, {}, {}, {})", docId, categoryId, subCategoryId, assignee);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("documentId", docId);
        result.put("categoryId", categoryId);
        result.put("subCategoryId", subCategoryId);
        result.put("assignee", assignee);
        result.put("result", 0);
        return result;
    }

    @Override
    public List<Map<String, Object>> find(
            String custId,
            String custName,
            String userLogin,
            Long officerId,
            String docClass,
            String subject,
            String text,
            String docId,
            String channels,
            String statuses,
            Date createdFrom,
            Date createdTill,
            Long assignee,
            Long categoryId,
            Long subcategoryId
    ) {
        logger.debug("[MOCK] find()");
        return findMy(); // Return same mock data
    }

    @Override
    public Map<String, Object> ffo(String docId) {
        logger.debug("[MOCK] ffo({})", docId);

        Map<String, Object> result = new HashMap<>();
        result.put("id", docId);
        result.put("userName", "John Doe");
        result.put("userId", "USR001");
        result.put("officerName", "Officer Smith");
        result.put("goldManager", "Manager Johnson");
        result.put("custName", "Test Customer");
        result.put("custAccount", "ACC123456");
        result.put("globusNo", "GLB001");
        result.put("location", "Main Branch");
        result.put("fText", "Free form text content for the order request.");
        result.put("infoToCustomer", "Please visit your branch for verification.");
        result.put("infoToBank", "Customer identity verified.");
        result.put("signTime", new Date());
        result.put("signRSA", "RSA_SIGNATURE_12345");
        result.put("sector", 1);
        result.put("segment", "RETAIL");
        return result;
    }

    @Override
    public Map<String, Object> setProcessing(String docId, String reason, Integer newStatus, Long messageId) {
        logger.debug("[MOCK] setProcessing({}, {}, {}, {})", docId, reason, newStatus, messageId);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("documentId", docId);
        result.put("newStatus", newStatus);
        result.put("result", 0);
        return result;
    }
}

package com.digibo.core.service.mock;

import com.digibo.core.service.FaxDocFindService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * FaxDocFindServiceMock - Mock implementation of FaxDocFindService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class FaxDocFindServiceMock implements FaxDocFindService {

    private static final Logger logger = LoggerFactory.getLogger(FaxDocFindServiceMock.class);

    @Override
    public List<Map<String, Object>> find(
            String faxId,
            String fromFax,
            String fromCSid,
            String docId,
            String custId,
            String fromAccount,
            String amountFrom,
            String amountTo,
            String docCcy,
            Long officerId,
            Integer docClass,
            String classes,
            String statuses,
            String partner,
            String subj,
            Long recvTimeFrom,
            Long recvTimeTo
    ) {
        logger.debug("[MOCK] find()");

        List<Map<String, Object>> results = new ArrayList<>();

        Map<String, Object> doc1 = new HashMap<>();
        doc1.put("FAX_ID", "FAX001");
        doc1.put("DOC_ID", "DOC001");
        doc1.put("FROM_FAX", "+1-555-1234");
        doc1.put("FROM_CSID", "CSID001");
        doc1.put("CUST_ID", "CUST001");
        doc1.put("FROM_ACCOUNT", "ACC123456");
        doc1.put("AMOUNT", new BigDecimal("1500.00"));
        doc1.put("CCY", "USD");
        doc1.put("PARTNER", "Test Partner");
        doc1.put("SUBJECT", "Transfer Request");
        doc1.put("DOC_CLASS", 1);
        doc1.put("STATUS", 2);
        doc1.put("RECV_TIME", System.currentTimeMillis() / 1000);
        doc1.put("OFFICER_ID", 1001L);
        results.add(doc1);

        Map<String, Object> doc2 = new HashMap<>();
        doc2.put("FAX_ID", "FAX002");
        doc2.put("DOC_ID", "DOC002");
        doc2.put("FROM_FAX", "+1-555-5678");
        doc2.put("FROM_CSID", "CSID002");
        doc2.put("CUST_ID", "CUST002");
        doc2.put("FROM_ACCOUNT", "ACC789012");
        doc2.put("AMOUNT", new BigDecimal("2500.00"));
        doc2.put("CCY", "EUR");
        doc2.put("PARTNER", "Another Partner");
        doc2.put("SUBJECT", "Payment Order");
        doc2.put("DOC_CLASS", 2);
        doc2.put("STATUS", 1);
        doc2.put("RECV_TIME", System.currentTimeMillis() / 1000 - 3600);
        doc2.put("OFFICER_ID", 1002L);
        results.add(doc2);

        return results;
    }
}

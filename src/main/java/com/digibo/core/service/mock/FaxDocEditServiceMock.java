package com.digibo.core.service.mock;

import com.digibo.core.service.FaxDocEditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * FaxDocEditServiceMock - Mock implementation of FaxDocEditService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class FaxDocEditServiceMock implements FaxDocEditService {

    private static final Logger logger = LoggerFactory.getLogger(FaxDocEditServiceMock.class);

    @Override
    public Map<String, Object> saveDocument(
            String docId,
            Long officerId,
            String custId,
            String fromAccount,
            BigDecimal amnt,
            String ccy,
            String partner,
            String note,
            String subj,
            Integer docStatus
    ) {
        logger.debug("[MOCK] saveDocument({})", docId);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("documentId", docId);
        result.put("message", "Document saved successfully (mock)");
        return result;
    }
}

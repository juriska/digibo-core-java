package com.digibo.core.service.mock;

import com.digibo.core.service.FaxEditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * FaxEditServiceMock - Mock implementation of FaxEditService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class FaxEditServiceMock implements FaxEditService {

    private static final Logger logger = LoggerFactory.getLogger(FaxEditServiceMock.class);

    @Override
    public Map<String, Object> addDocument(
            String faxId,
            Integer docClass,
            Long officerId,
            String custId,
            String fromAccount,
            BigDecimal amnt,
            String ccy,
            String partner,
            String note,
            String subj,
            Integer docStatus,
            byte[] dTif
    ) {
        logger.debug("[MOCK] addDocument({})", faxId);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("faxId", faxId);
        result.put("message", "Document added successfully (mock)");
        return result;
    }
}

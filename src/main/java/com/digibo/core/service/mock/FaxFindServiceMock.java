package com.digibo.core.service.mock;

import com.digibo.core.service.FaxFindService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * FaxFindServiceMock - Mock implementation of FaxFindService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class FaxFindServiceMock implements FaxFindService {

    private static final Logger logger = LoggerFactory.getLogger(FaxFindServiceMock.class);

    @Override
    public List<Map<String, Object>> find(
            String faxId,
            String fromFax,
            String fromCSid,
            Integer faxStatus,
            Long recvTimeFrom,
            Long recvTimeTo
    ) {
        logger.debug("[MOCK] find()");

        List<Map<String, Object>> results = new ArrayList<>();

        Map<String, Object> fax1 = new HashMap<>();
        fax1.put("FAX_ID", "FAX001");
        fax1.put("FROM_FAX", "+1-555-1234");
        fax1.put("FROM_CSID", "CSID001");
        fax1.put("RECV_TIME", System.currentTimeMillis() / 1000);
        fax1.put("RECV_STATUS", 1);
        fax1.put("FAX_STATUS", 2);
        results.add(fax1);

        Map<String, Object> fax2 = new HashMap<>();
        fax2.put("FAX_ID", "FAX002");
        fax2.put("FROM_FAX", "+1-555-5678");
        fax2.put("FROM_CSID", "CSID002");
        fax2.put("RECV_TIME", System.currentTimeMillis() / 1000 - 3600);
        fax2.put("RECV_STATUS", 1);
        fax2.put("FAX_STATUS", 1);
        results.add(fax2);

        return results;
    }

    @Override
    public List<Map<String, Object>> findNew() {
        logger.debug("[MOCK] findNew()");

        List<Map<String, Object>> results = new ArrayList<>();

        Map<String, Object> fax = new HashMap<>();
        fax.put("FAX_ID", "FAX003");
        fax.put("FROM_FAX", "+1-555-9999");
        fax.put("FROM_CSID", "CSID003");
        fax.put("RECV_TIME", System.currentTimeMillis() / 1000);
        fax.put("RECV_STATUS", 1);
        fax.put("FAX_STATUS", 1);
        results.add(fax);

        return results;
    }
}

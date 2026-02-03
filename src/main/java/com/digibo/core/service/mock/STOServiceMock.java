package com.digibo.core.service.mock;

import com.digibo.core.service.STOService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * STOServiceMock - Mock implementation of STOService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class STOServiceMock implements STOService {

    private static final Logger logger = LoggerFactory.getLogger(STOServiceMock.class);

    @Override
    public List<Map<String, Object>> find(String custId, String custName, String userLogin, Long officerId,
                                           String pType, String docId, String statuses,
                                           Date createdFrom, Date createdTill) {
        logger.debug("[MOCK] find({}, {}, {}, {}, {}, {}, {}, {}, {})",
                custId, custName, userLogin, officerId, pType, docId, statuses, createdFrom, createdTill);

        List<Map<String, Object>> stoList = new ArrayList<>();

        Map<String, Object> sto1 = new HashMap<>();
        sto1.put("DOC_ID", "STO001");
        sto1.put("CUST_ID", "CUST001");
        sto1.put("CUST_NAME", "Test Customer");
        sto1.put("TYPE", "MONTHLY");
        sto1.put("STATUS", "ACTIVE");
        sto1.put("AMOUNT", 500.00);
        sto1.put("CREATED_DATE", new Date());
        stoList.add(sto1);

        Map<String, Object> sto2 = new HashMap<>();
        sto2.put("DOC_ID", "STO002");
        sto2.put("CUST_ID", "CUST002");
        sto2.put("CUST_NAME", "Another Customer");
        sto2.put("TYPE", "WEEKLY");
        sto2.put("STATUS", "PENDING");
        sto2.put("AMOUNT", 100.00);
        sto2.put("CREATED_DATE", new Date());
        stoList.add(sto2);

        return stoList;
    }

    @Override
    public Map<String, Object> sto(String stoId) {
        logger.debug("[MOCK] sto({})", stoId);

        Map<String, Object> stoDoc = new HashMap<>();
        stoDoc.put("id", stoId);
        stoDoc.put("globusNo", "GLB123456");
        stoDoc.put("userName", "Test User");
        stoDoc.put("userId", "USER001");
        stoDoc.put("officerName", "Officer Smith");
        stoDoc.put("custName", "Test Customer");
        stoDoc.put("custAccount", "ACC123456789");
        stoDoc.put("benAcnt", "BEN987654321");
        stoDoc.put("benType", "INDIVIDUAL");
        stoDoc.put("benName", "John Beneficiary");
        stoDoc.put("benId", "BEN001");
        stoDoc.put("benRes", "US");
        stoDoc.put("benBankName", "Beneficiary Bank");
        stoDoc.put("benBankBranch", "Main Branch");
        stoDoc.put("benBankSwift", "BENBUS33");
        stoDoc.put("benBankOtherCode", null);
        stoDoc.put("details", "Monthly utility payment");
        stoDoc.put("contractId", "CONTRACT001");
        stoDoc.put("agreement", "Standing Order Agreement v1.0");
        stoDoc.put("firstDate", new Date(System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000));
        stoDoc.put("lastDate", new Date(System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000));
        stoDoc.put("nextDate", new Date(System.currentTimeMillis() + 7L * 24 * 60 * 60 * 1000));
        stoDoc.put("frequency", "MONTHLY");
        stoDoc.put("balMin", "100.00");
        stoDoc.put("balMax", "10000.00");
        stoDoc.put("crMin", "50.00");
        stoDoc.put("revolving", "N");
        stoDoc.put("location", "LV");
        stoDoc.put("rejector", null);
        stoDoc.put("rejectDate", null);
        stoDoc.put("abonentCode", "UTIL123");
        stoDoc.put("itc", "Information for customer");
        stoDoc.put("itb", "Information for bank");

        return stoDoc;
    }
}

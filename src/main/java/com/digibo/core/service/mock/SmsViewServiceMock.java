package com.digibo.core.service.mock;

import com.digibo.core.service.SmsViewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * SmsViewServiceMock - Mock implementation of SmsViewService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class SmsViewServiceMock implements SmsViewService {

    private static final Logger logger = LoggerFactory.getLogger(SmsViewServiceMock.class);

    @Override
    public List<Map<String, Object>> getTypes() {
        logger.debug("[MOCK] getTypes()");

        List<Map<String, Object>> types = new ArrayList<>();

        Map<String, Object> type1 = new HashMap<>();
        type1.put("TYPE_ID", 1L);
        type1.put("TYPE_NAME", "NOTIFICATION");
        type1.put("DESCRIPTION", "Account notification");
        types.add(type1);

        Map<String, Object> type2 = new HashMap<>();
        type2.put("TYPE_ID", 2L);
        type2.put("TYPE_NAME", "ALERT");
        type2.put("DESCRIPTION", "Balance alert");
        types.add(type2);

        Map<String, Object> type3 = new HashMap<>();
        type3.put("TYPE_ID", 3L);
        type3.put("TYPE_NAME", "OTP");
        type3.put("DESCRIPTION", "One-time password");
        types.add(type3);

        return types;
    }

    @Override
    public List<Map<String, Object>> find(String custId, String custName, String userLogin, Long officerId,
                                           String pType, String mobile, String text, String statuses,
                                           Date createdFrom, Date createdTill) {
        logger.debug("[MOCK] find({}, {}, {}, {}, {}, {}, {}, {}, {}, {})",
                custId, custName, userLogin, officerId, pType, mobile, text, statuses, createdFrom, createdTill);

        List<Map<String, Object>> messages = new ArrayList<>();

        Map<String, Object> msg1 = new HashMap<>();
        msg1.put("MESSAGE_ID", "MSG001");
        msg1.put("CUST_ID", "CUST001");
        msg1.put("MOBILE", "+1234567890");
        msg1.put("TEXT", "Your balance is $1000");
        msg1.put("STATUS", "SENT");
        msg1.put("CREATED_DATE", new Date());
        messages.add(msg1);

        Map<String, Object> msg2 = new HashMap<>();
        msg2.put("MESSAGE_ID", "MSG002");
        msg2.put("CUST_ID", "CUST001");
        msg2.put("MOBILE", "+1234567890");
        msg2.put("TEXT", "Your OTP is 123456");
        msg2.put("STATUS", "DELIVERED");
        msg2.put("CREATED_DATE", new Date());
        messages.add(msg2);

        return messages;
    }

    @Override
    public Map<String, Object> sms(String messageId) {
        logger.debug("[MOCK] sms({})", messageId);

        Map<String, Object> smsMessage = new HashMap<>();
        smsMessage.put("id", messageId);
        smsMessage.put("typeName", "NOTIFICATION");
        smsMessage.put("priority", "NORMAL");
        smsMessage.put("changeDate", new Date());
        smsMessage.put("chargeDate", new Date());
        smsMessage.put("text", "Your account balance is $1,000.00");
        smsMessage.put("srcAddr", "BANK");
        smsMessage.put("srcProvider", "Provider A");
        smsMessage.put("destAddr", "+1234567890");
        smsMessage.put("destProvider", "Provider B");
        smsMessage.put("wocId", "WOC001");
        smsMessage.put("userId", "USER001");
        smsMessage.put("userName", "Test User");
        smsMessage.put("login", "testuser");
        smsMessage.put("stmtId", "STMT001");
        smsMessage.put("batchId", "BATCH001");
        smsMessage.put("errorType", null);
        smsMessage.put("errorText", null);

        return smsMessage;
    }
}

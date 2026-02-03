package com.digibo.core.service.mock;

import com.digibo.core.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * MessageServiceMock - Mock implementation of MessageService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class MessageServiceMock implements MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageServiceMock.class);

    private final List<Map<String, Object>> mockMessages = new ArrayList<>();

    public MessageServiceMock() {
        initMockData();
    }

    private void initMockData() {
        Map<String, Object> msg1 = new HashMap<>();
        msg1.put("MSGID", 1001L);
        msg1.put("WOCID", 5001L);
        msg1.put("CHANNEL", "WEB");
        msg1.put("POSTDATE", new Date());
        msg1.put("LOGIN", "jdoe");
        msg1.put("MESSAGE", "Question about my account balance");
        msg1.put("OFFICERID", null);
        msg1.put("STATUS", 1);
        msg1.put("CLASSID", 10L);
        msg1.put("SECTOR", "RETAIL");
        msg1.put("SEGMENT", "PREMIUM");
        msg1.put("IS_EMPLOYEE", "N");
        msg1.put("TYPE", "Q");
        mockMessages.add(msg1);

        Map<String, Object> msg2 = new HashMap<>();
        msg2.put("MSGID", 1002L);
        msg2.put("WOCID", 5002L);
        msg2.put("CHANNEL", "MOBILE");
        msg2.put("POSTDATE", new Date(System.currentTimeMillis() - 3600000));
        msg2.put("LOGIN", "jsmith");
        msg2.put("MESSAGE", "Request for card replacement");
        msg2.put("OFFICERID", 100L);
        msg2.put("STATUS", 2);
        msg2.put("CLASSID", 20L);
        msg2.put("SECTOR", "RETAIL");
        msg2.put("SEGMENT", "STANDARD");
        msg2.put("IS_EMPLOYEE", "N");
        msg2.put("TYPE", "R");
        mockMessages.add(msg2);

        Map<String, Object> msg3 = new HashMap<>();
        msg3.put("MSGID", 1003L);
        msg3.put("WOCID", 5003L);
        msg3.put("CHANNEL", "WEB");
        msg3.put("POSTDATE", new Date(System.currentTimeMillis() - 7200000));
        msg3.put("LOGIN", "bwilson");
        msg3.put("MESSAGE", "Complaint about service fee");
        msg3.put("OFFICERID", null);
        msg3.put("STATUS", 1);
        msg3.put("CLASSID", 30L);
        msg3.put("SECTOR", "BUSINESS");
        msg3.put("SEGMENT", "SME");
        msg3.put("IS_EMPLOYEE", "N");
        msg3.put("TYPE", "C");
        mockMessages.add(msg3);
    }

    @Override
    public List<Map<String, Object>> findMessages(String userId, String userName, String login,
                                                   Long officerId, String msgId, String message,
                                                   String type, String custId, String custName,
                                                   String statuses, Long classId,
                                                   Date dateFrom, Date dateTill, String channelId) {
        logger.debug("[MOCK] findMessages()");

        return mockMessages.stream()
                .filter(m -> msgId == null || msgId.equals(String.valueOf(m.get("MSGID"))))
                .filter(m -> login == null || ((String) m.get("LOGIN")).toLowerCase().contains(login.toLowerCase()))
                .filter(m -> officerId == null || officerId.equals(m.get("OFFICERID")))
                .filter(m -> message == null || ((String) m.get("MESSAGE")).toLowerCase().contains(message.toLowerCase()))
                .filter(m -> type == null || type.equalsIgnoreCase((String) m.get("TYPE")))
                .filter(m -> classId == null || classId.equals(m.get("CLASSID")))
                .filter(m -> channelId == null || channelId.equals(m.get("CHANNEL")))
                .toList();
    }

    @Override
    public List<Map<String, Object>> findCurrent(String classes) {
        logger.debug("[MOCK] findCurrent({})", classes);

        return mockMessages.stream()
                .filter(m -> {
                    Integer status = (Integer) m.get("STATUS");
                    return status >= 1 && status <= 3;
                })
                .filter(m -> {
                    if (classes == null) return true;
                    Long classId = (Long) m.get("CLASSID");
                    return Arrays.stream(classes.split(","))
                            .map(String::trim)
                            .map(Long::parseLong)
                            .anyMatch(c -> c.equals(classId));
                })
                .toList();
    }

    @Override
    public Map<String, Object> loadUserData(Long wocId, String msgId) {
        logger.debug("[MOCK] loadUserData({}, {})", wocId, msgId);

        List<Map<String, Object>> customers = new ArrayList<>();
        Map<String, Object> cust1 = new HashMap<>();
        cust1.put("NAME", "Acme Corporation");
        cust1.put("ID", 5001L);
        customers.add(cust1);

        Map<String, Object> cust2 = new HashMap<>();
        cust2.put("NAME", "Smith Industries");
        cust2.put("ID", 5002L);
        customers.add(cust2);

        Map<String, Object> response = new HashMap<>();
        response.put("userName", "John Doe");
        response.put("login", "john.doe");
        response.put("fromCust", msgId != null ? "Acme Corporation (5001)" : null);
        response.put("custId", msgId != null ? 5001L : null);
        response.put("customers", customers);
        return response;
    }

    @Override
    public List<Map<String, Object>> loadCommunication(Long wocId) {
        logger.debug("[MOCK] loadCommunication({})", wocId);

        List<Map<String, Object>> communication = new ArrayList<>();

        Map<String, Object> comm1 = new HashMap<>();
        comm1.put("MSGID", 2001L);
        comm1.put("WOCID", wocId);
        comm1.put("POSTDATE", new Date(System.currentTimeMillis() - 86400000));
        comm1.put("MESSAGE", "Initial inquiry");
        comm1.put("TYPE", "Q");
        comm1.put("DIRECTION", "IN");
        communication.add(comm1);

        Map<String, Object> comm2 = new HashMap<>();
        comm2.put("MSGID", 2002L);
        comm2.put("WOCID", wocId);
        comm2.put("POSTDATE", new Date(System.currentTimeMillis() - 43200000));
        comm2.put("MESSAGE", "Response from bank");
        comm2.put("TYPE", "A");
        comm2.put("DIRECTION", "OUT");
        communication.add(comm2);

        return communication;
    }

    @Override
    public Map<String, Object> setLock(String lockName, String id) {
        logger.debug("[MOCK] setLock({}, {})", lockName, id);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("lockStatus", 0);
        response.put("id", id);
        response.put("officerName", null);
        response.put("officerPhone", null);
        response.put("message", "Lock acquired");
        return response;
    }

    @Override
    public Map<String, Object> setSeen(Long id) {
        logger.debug("[MOCK] setSeen({})", id);

        mockMessages.stream()
                .filter(m -> id.equals(m.get("MSGID")))
                .findFirst()
                .ifPresent(m -> m.put("STATUS", 2));

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("messageId", id);
        response.put("message", "Message marked as seen");
        return response;
    }

    @Override
    public Map<String, Object> answer(Long id, Long wocId, Integer status, Long classId, String message) {
        logger.debug("[MOCK] answer({}, {}, {}, {})", id, wocId, status, classId);

        mockMessages.stream()
                .filter(m -> id.equals(m.get("MSGID")))
                .findFirst()
                .ifPresent(m -> m.put("STATUS", status));

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("messageId", id);
        response.put("message", "Answer sent successfully");
        return response;
    }

    @Override
    public Map<String, Object> forward(Long id, Long classId) {
        logger.debug("[MOCK] forward({}, {})", id, classId);

        mockMessages.stream()
                .filter(m -> id.equals(m.get("MSGID")))
                .findFirst()
                .ifPresent(m -> {
                    m.put("CLASSID", classId);
                    m.put("STATUS", 1);
                });

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("messageId", id);
        response.put("newClassId", classId);
        response.put("message", "Message forwarded successfully");
        return response;
    }
}

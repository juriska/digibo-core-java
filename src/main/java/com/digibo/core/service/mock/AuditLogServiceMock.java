package com.digibo.core.service.mock;

import com.digibo.core.service.AuditLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * AuditLogServiceMock - Mock implementation of AuditLogService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class AuditLogServiceMock implements AuditLogService {

    private static final Logger logger = LoggerFactory.getLogger(AuditLogServiceMock.class);

    @Override
    public List<Map<String, Object>> find(Date dfrom, Date dto, String events, String pObject,
                                          String pOriginator, String pChannels, Integer pResultSetSize) {
        logger.debug("[MOCK] find({}, {}, {}, {}, {}, {}, {})",
                dfrom, dto, events, pObject, pOriginator, pChannels, pResultSetSize);

        List<Map<String, Object>> entries = new ArrayList<>();

        Map<String, Object> entry1 = new HashMap<>();
        entry1.put("LOG_ID", 1001L);
        entry1.put("EVENT_ID", 100);
        entry1.put("EVENT_NAME", "USER_LOGIN");
        entry1.put("EVENT_TIME", new Date());
        entry1.put("OBJECT_ID", pObject != null ? pObject : "USER001");
        entry1.put("ORIGINATOR", pOriginator != null ? pOriginator : "system");
        entry1.put("CHANNEL_ID", 1);
        entry1.put("SESSION_ID", "SESSION001");
        entry1.put("IP_ADDRESS", "192.168.1.100");
        entry1.put("DETAILS", "User logged in successfully");
        entries.add(entry1);

        Map<String, Object> entry2 = new HashMap<>();
        entry2.put("LOG_ID", 1002L);
        entry2.put("EVENT_ID", 101);
        entry2.put("EVENT_NAME", "PASSWORD_CHANGE");
        entry2.put("EVENT_TIME", new Date(System.currentTimeMillis() - 3600000));
        entry2.put("OBJECT_ID", "USER001");
        entry2.put("ORIGINATOR", "user");
        entry2.put("CHANNEL_ID", 1);
        entry2.put("SESSION_ID", "SESSION001");
        entry2.put("IP_ADDRESS", "192.168.1.100");
        entry2.put("DETAILS", "Password changed");
        entries.add(entry2);

        Map<String, Object> entry3 = new HashMap<>();
        entry3.put("LOG_ID", 1003L);
        entry3.put("EVENT_ID", 200);
        entry3.put("EVENT_NAME", "DOCUMENT_CREATE");
        entry3.put("EVENT_TIME", new Date(System.currentTimeMillis() - 7200000));
        entry3.put("OBJECT_ID", "DOC001");
        entry3.put("ORIGINATOR", "user");
        entry3.put("CHANNEL_ID", 2);
        entry3.put("SESSION_ID", "SESSION002");
        entry3.put("IP_ADDRESS", "192.168.1.101");
        entry3.put("DETAILS", "New document created");
        entries.add(entry3);

        // Apply result set size limit
        if (pResultSetSize != null && pResultSetSize > 0 && entries.size() > pResultSetSize) {
            return entries.subList(0, pResultSetSize);
        }

        return entries;
    }

    @Override
    public List<Map<String, Object>> findSession(String pSession) {
        logger.debug("[MOCK] findSession({})", pSession);

        List<Map<String, Object>> entries = new ArrayList<>();

        Map<String, Object> entry1 = new HashMap<>();
        entry1.put("LOG_ID", 2001L);
        entry1.put("EVENT_ID", 100);
        entry1.put("EVENT_NAME", "SESSION_START");
        entry1.put("EVENT_TIME", new Date(System.currentTimeMillis() - 7200000));
        entry1.put("SESSION_ID", pSession);
        entry1.put("IP_ADDRESS", "192.168.1.100");
        entry1.put("USER_AGENT", "Mozilla/5.0");
        entries.add(entry1);

        Map<String, Object> entry2 = new HashMap<>();
        entry2.put("LOG_ID", 2002L);
        entry2.put("EVENT_ID", 101);
        entry2.put("EVENT_NAME", "PAGE_VIEW");
        entry2.put("EVENT_TIME", new Date(System.currentTimeMillis() - 3600000));
        entry2.put("SESSION_ID", pSession);
        entry2.put("IP_ADDRESS", "192.168.1.100");
        entry2.put("PAGE", "/dashboard");
        entries.add(entry2);

        Map<String, Object> entry3 = new HashMap<>();
        entry3.put("LOG_ID", 2003L);
        entry3.put("EVENT_ID", 102);
        entry3.put("EVENT_NAME", "ACTION");
        entry3.put("EVENT_TIME", new Date());
        entry3.put("SESSION_ID", pSession);
        entry3.put("IP_ADDRESS", "192.168.1.100");
        entry3.put("ACTION_TYPE", "SUBMIT");
        entries.add(entry3);

        return entries;
    }

    @Override
    public List<Map<String, Object>> getTree() {
        logger.debug("[MOCK] getTree()");

        List<Map<String, Object>> tree = new ArrayList<>();

        Map<String, Object> node1 = new HashMap<>();
        node1.put("NODE_ID", 1L);
        node1.put("NODE_NAME", "Authentication Events");
        node1.put("PARENT_ID", null);
        node1.put("LEVEL", 0);
        node1.put("EVENT_COUNT", 150);
        tree.add(node1);

        Map<String, Object> node2 = new HashMap<>();
        node2.put("NODE_ID", 2L);
        node2.put("NODE_NAME", "Login Events");
        node2.put("PARENT_ID", 1L);
        node2.put("LEVEL", 1);
        node2.put("EVENT_COUNT", 100);
        tree.add(node2);

        Map<String, Object> node3 = new HashMap<>();
        node3.put("NODE_ID", 3L);
        node3.put("NODE_NAME", "Logout Events");
        node3.put("PARENT_ID", 1L);
        node3.put("LEVEL", 1);
        node3.put("EVENT_COUNT", 50);
        tree.add(node3);

        Map<String, Object> node4 = new HashMap<>();
        node4.put("NODE_ID", 4L);
        node4.put("NODE_NAME", "Document Events");
        node4.put("PARENT_ID", null);
        node4.put("LEVEL", 0);
        node4.put("EVENT_COUNT", 200);
        tree.add(node4);

        Map<String, Object> node5 = new HashMap<>();
        node5.put("NODE_ID", 5L);
        node5.put("NODE_NAME", "Create Events");
        node5.put("PARENT_ID", 4L);
        node5.put("LEVEL", 1);
        node5.put("EVENT_COUNT", 80);
        tree.add(node5);

        Map<String, Object> node6 = new HashMap<>();
        node6.put("NODE_ID", 6L);
        node6.put("NODE_NAME", "Update Events");
        node6.put("PARENT_ID", 4L);
        node6.put("LEVEL", 1);
        node6.put("EVENT_COUNT", 120);
        tree.add(node6);

        return tree;
    }
}

package com.digibo.core.service.mock;

import com.digibo.core.service.OfficerSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Profile("mock")
public class OfficerSessionServiceMock implements OfficerSessionService {

    private static final Logger logger = LoggerFactory.getLogger(OfficerSessionServiceMock.class);

    private final Set<String> activeSessions = ConcurrentHashMap.newKeySet();

    @Override
    public String createSession(String username, String password, String ipAddress) {
        String sessionId = UUID.randomUUID().toString();
        activeSessions.add(sessionId);
        logger.debug("Mock: Created officer session {} for user {}", sessionId, username);
        return sessionId;
    }

    @Override
    public void closeSession(String sessionId) {
        if (sessionId != null) {
            activeSessions.remove(sessionId);
            logger.debug("Mock: Closed officer session {}", sessionId);
        }
    }
}

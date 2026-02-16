package com.digibo.core.service.impl;

import com.digibo.core.service.OfficerSessionService;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Profile("!mock")
public class OfficerSessionServiceImpl implements OfficerSessionService {

    private static final Logger logger = LoggerFactory.getLogger(OfficerSessionServiceImpl.class);

    private final String jdbcUrl;
    private final ConcurrentHashMap<String, Connection> activeSessions = new ConcurrentHashMap<>();

    public OfficerSessionServiceImpl(@Value("${spring.datasource.url}") String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    @Override
    public String createSession(String username, String password, String ipAddress) {
        String sessionId = UUID.randomUUID().toString();

        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            try (CallableStatement cs = connection.prepareCall("{call BO_AUTH.REGISTER_SESSION(?, ?)}")) {
                cs.setString(1, sessionId);
                cs.setString(2, ipAddress);
                cs.execute();
            }

            activeSessions.put(sessionId, connection);
            logger.info("Created officer session {} for user {}", sessionId, username);
            return sessionId;

        } catch (SQLException e) {
            logger.error("Failed to create officer session for user {}: {}", username, e.getMessage());
            throw new RuntimeException("Failed to create officer session", e);
        }
    }

    @Override
    public void closeSession(String sessionId) {
        if (sessionId == null) {
            return;
        }

        Connection connection = activeSessions.remove(sessionId);
        if (connection == null) {
            logger.warn("No active connection found for session {}", sessionId);
            return;
        }

        try {
            try (CallableStatement cs = connection.prepareCall("{call BO_AUTH.REMOVE_SESSION(?)}")) {
                cs.setString(1, sessionId);
                cs.execute();
            }
            logger.info("Removed officer session {}", sessionId);
        } catch (SQLException e) {
            logger.error("Failed to remove officer session {}: {}", sessionId, e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error("Failed to close connection for session {}: {}", sessionId, e.getMessage());
            }
        }
    }

    @PreDestroy
    public void cleanup() {
        logger.info("Cleaning up {} active officer sessions", activeSessions.size());
        for (Map.Entry<String, Connection> entry : activeSessions.entrySet()) {
            try {
                Connection connection = entry.getValue();
                if (!connection.isClosed()) {
                    try (CallableStatement cs = connection.prepareCall("{call BO_AUTH.REMOVE_SESSION(?)}")) {
                        cs.setString(1, entry.getKey());
                        cs.execute();
                    }
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error("Error cleaning up session {}: {}", entry.getKey(), e.getMessage());
            }
        }
        activeSessions.clear();
    }
}

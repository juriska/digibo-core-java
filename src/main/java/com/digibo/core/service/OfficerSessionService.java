package com.digibo.core.service;

/**
 * Service for managing officer sessions in the officers_online table.
 * Creates dedicated Oracle connections with officer credentials and registers sessions
 * that a 3rd party system uses to verify officer permissions.
 */
public interface OfficerSessionService {

    /**
     * Create a new officer session by opening a dedicated Oracle connection
     * with the officer's credentials and registering the session.
     *
     * @param username Officer's Oracle username
     * @param password Officer's Oracle password
     * @param ipAddress Client IP address
     * @return Session ID (UUID) that identifies this session
     */
    String createSession(String username, String password, String ipAddress);

    /**
     * Close an officer session by removing it from officers_online
     * and closing the dedicated Oracle connection.
     *
     * @param sessionId The session ID to close
     */
    void closeSession(String sessionId);
}

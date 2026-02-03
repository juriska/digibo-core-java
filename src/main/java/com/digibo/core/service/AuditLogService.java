package com.digibo.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * AuditLogService - Service interface for BOAuditLog Oracle package
 * Handles audit log queries and event tracking
 */
public interface AuditLogService {

    /**
     * Find audit log entries by filters
     * @param dfrom Start date
     * @param dto End date
     * @param events Comma-separated event IDs
     * @param pObject Object identifier
     * @param pOriginator Originator identifier
     * @param pChannels Comma-separated channel IDs
     * @param pResultSetSize Maximum number of results
     * @return List of audit log entries
     */
    List<Map<String, Object>> find(Date dfrom, Date dto, String events, String pObject,
                                   String pOriginator, String pChannels, Integer pResultSetSize);

    /**
     * Find audit log entries by session ID
     * @param pSession Session ID
     * @return List of audit log entries for the session
     */
    List<Map<String, Object>> findSession(String pSession);

    /**
     * Get audit log tree structure
     * @return List of tree nodes
     */
    List<Map<String, Object>> getTree();
}

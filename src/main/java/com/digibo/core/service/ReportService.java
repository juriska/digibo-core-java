package com.digibo.core.service;

import java.util.List;
import java.util.Map;

/**
 * ReportService - Service interface for BOReport Oracle package
 * Handles reporting operations
 */
public interface ReportService {

    /**
     * Get unauthorized conditions report
     * @return List of unauthorized conditions
     */
    List<Map<String, Object>> unauthorizedConditions();

    /**
     * Get unauthorized users report
     * @return List of unauthorized users
     */
    List<Map<String, Object>> unauthorizedUsers();
}

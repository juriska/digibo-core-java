package com.digibo.core.service;

import java.util.List;
import java.util.Map;

/**
 * SMSService - Service interface for BOSMS Oracle package
 * Handles SMS user data operations
 */
public interface SMSService {

    /**
     * Load user data for SMS
     * @param wocId WOC ID
     * @return User data with cursor results and OUT parameters
     */
    Map<String, Object> loadUserData(Long wocId);
}

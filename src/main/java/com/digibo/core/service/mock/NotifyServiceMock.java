package com.digibo.core.service.mock;

import com.digibo.core.service.NotifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * NotifyServiceMock - Mock implementation of NotifyService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class NotifyServiceMock implements NotifyService {

    private static final Logger logger = LoggerFactory.getLogger(NotifyServiceMock.class);

    @Override
    public Map<String, Object> notifyRatesBoard() {
        logger.debug("[MOCK] BONotify.notifyRatesBoard() called");
        logger.debug("[MOCK] Simulating rates board notification");

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Rates board notified successfully (MOCK)");
        return result;
    }

    @Override
    public Map<String, Object> notifyFfo() {
        logger.debug("[MOCK] BONotify.notifyFfo() called");
        logger.debug("[MOCK] Simulating FFO notification");

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "FFO system notified successfully (MOCK)");
        return result;
    }

    @Override
    public Map<String, Object> notifyInvestment() {
        logger.debug("[MOCK] BONotify.notifyInvestment() called");
        logger.debug("[MOCK] Simulating investment notification");

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Investment system notified successfully (MOCK)");
        return result;
    }

    @Override
    public Map<String, Object> notifyMortgageLoans() {
        logger.debug("[MOCK] BONotify.notifyMortgageLoans() called");
        logger.debug("[MOCK] Simulating mortgage loans notification");

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Mortgage loans system notified successfully (MOCK)");
        return result;
    }

    @Override
    public Map<String, Object> updateBoPermissions() {
        logger.debug("[MOCK] BONotify.updateBoPermissions() called");
        logger.debug("[MOCK] Simulating BO permissions update");

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "BO permissions updated successfully (MOCK)");
        return result;
    }
}

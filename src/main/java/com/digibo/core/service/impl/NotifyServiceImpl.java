package com.digibo.core.service.impl;

import com.digibo.core.service.NotifyService;
import com.digibo.core.service.base.BaseService;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * NotifyServiceImpl - Real implementation of NotifyService
 * Calls BONotify Oracle package procedures
 */
@Service
@Profile("!mock")
public class NotifyServiceImpl extends BaseService implements NotifyService {

    public NotifyServiceImpl() {
        super("BONotify");
    }

    @Override
    public Map<String, Object> notifyRatesBoard() {
        logger.debug("Calling BONotify.notifyRatesBoard()");

        List<SqlParameter> params = List.of();
        Map<String, Object> inputParams = Map.of();

        executeVoidProcedure("notifyRatesBoard", params, inputParams);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Rates board notified successfully");
        return result;
    }

    @Override
    public Map<String, Object> notifyFfo() {
        logger.debug("Calling BONotify.notifyFfo()");

        List<SqlParameter> params = List.of();
        Map<String, Object> inputParams = Map.of();

        executeVoidProcedure("notifyFfo", params, inputParams);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "FFO system notified successfully");
        return result;
    }

    @Override
    public Map<String, Object> notifyInvestment() {
        logger.debug("Calling BONotify.notifyInvestment()");

        List<SqlParameter> params = List.of();
        Map<String, Object> inputParams = Map.of();

        executeVoidProcedure("notifyInvestment", params, inputParams);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Investment system notified successfully");
        return result;
    }

    @Override
    public Map<String, Object> notifyMortgageLoans() {
        logger.debug("Calling BONotify.notifyMortgageLoans()");

        List<SqlParameter> params = List.of();
        Map<String, Object> inputParams = Map.of();

        executeVoidProcedure("notifyMortgageLoans", params, inputParams);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Mortgage loans system notified successfully");
        return result;
    }

    @Override
    public Map<String, Object> updateBoPermissions() {
        logger.debug("Calling BONotify.updateBoPermissions()");

        List<SqlParameter> params = List.of();
        Map<String, Object> inputParams = Map.of();

        executeVoidProcedure("updateBoPermissions", params, inputParams);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "BO permissions updated successfully");
        return result;
    }
}

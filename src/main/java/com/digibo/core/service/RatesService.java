package com.digibo.core.service;

import java.util.List;
import java.util.Map;

/**
 * RatesService - Service interface for BOrates Oracle package
 * Handles currency exchange rates operations
 */
public interface RatesService {

    /**
     * Load currency rates with filters
     * @param filter Filter criteria
     * @param dao DAO parameter
     * @return List of currency rates
     */
    List<Map<String, Object>> loadCurrencyRates(String filter, String dao);
}

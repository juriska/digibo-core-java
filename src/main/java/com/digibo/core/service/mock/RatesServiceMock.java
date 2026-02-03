package com.digibo.core.service.mock;

import com.digibo.core.service.RatesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * RatesServiceMock - Mock implementation of RatesService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class RatesServiceMock implements RatesService {

    private static final Logger logger = LoggerFactory.getLogger(RatesServiceMock.class);

    private static final List<Map<String, Object>> mockCurrencyRates = new ArrayList<>();

    static {
        mockCurrencyRates.add(createRate("EUR", "BUY", new BigDecimal("1.0000"), "BANK"));
        mockCurrencyRates.add(createRate("EUR", "SELL", new BigDecimal("1.0000"), "BANK"));
        mockCurrencyRates.add(createRate("USD", "BUY", new BigDecimal("0.9200"), "BANK"));
        mockCurrencyRates.add(createRate("USD", "SELL", new BigDecimal("0.9400"), "BANK"));
        mockCurrencyRates.add(createRate("GBP", "BUY", new BigDecimal("1.1500"), "BANK"));
        mockCurrencyRates.add(createRate("GBP", "SELL", new BigDecimal("1.1700"), "BANK"));
        mockCurrencyRates.add(createRate("CHF", "BUY", new BigDecimal("1.0400"), "BANK"));
        mockCurrencyRates.add(createRate("CHF", "SELL", new BigDecimal("1.0600"), "BANK"));
        mockCurrencyRates.add(createRate("JPY", "BUY", new BigDecimal("0.0062"), "BANK"));
        mockCurrencyRates.add(createRate("JPY", "SELL", new BigDecimal("0.0064"), "BANK"));
    }

    private static Map<String, Object> createRate(String currency, String rateType, BigDecimal rate, String source) {
        Map<String, Object> rateMap = new HashMap<>();
        rateMap.put("CURRENCY", currency);
        rateMap.put("RATE_TYPE", rateType);
        rateMap.put("RATE", rate);
        rateMap.put("SOURCE", source);
        rateMap.put("VALID_FROM", new Date());
        rateMap.put("VALID_TILL", new Date(System.currentTimeMillis() + 24L * 60 * 60 * 1000));
        return rateMap;
    }

    @Override
    public List<Map<String, Object>> loadCurrencyRates(String filter, String dao) {
        logger.debug("[MOCK] BOrates.loadCurrencyRates({}, {}) called", filter, dao);

        List<Map<String, Object>> results = new ArrayList<>(mockCurrencyRates);

        // Apply filter if provided
        if (filter != null && !filter.isEmpty()) {
            String filterLower = filter.toLowerCase();
            results.removeIf(rate -> {
                String currency = (String) rate.get("CURRENCY");
                String rateType = (String) rate.get("RATE_TYPE");
                boolean currencyMatch = currency != null && currency.toLowerCase().contains(filterLower);
                boolean rateTypeMatch = rateType != null && rateType.toLowerCase().contains(filterLower);
                return !currencyMatch && !rateTypeMatch;
            });
        }

        // Apply DAO filter if provided
        if (dao != null && !dao.isEmpty()) {
            String daoLower = dao.toLowerCase();
            results.removeIf(rate -> {
                String source = (String) rate.get("SOURCE");
                return source == null || !source.toLowerCase().contains(daoLower);
            });
        }

        logger.debug("[MOCK] Returning {} currency rates", results.size());
        return results;
    }
}

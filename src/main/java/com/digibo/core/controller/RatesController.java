package com.digibo.core.controller;

import com.digibo.core.service.RatesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * RatesController - REST controller for currency rates operations
 * Maps to /api/rates endpoints
 */
@RestController
@RequestMapping("/api/rates")
public class RatesController {

    private final RatesService ratesService;

    public RatesController(RatesService ratesService) {
        this.ratesService = ratesService;
    }

    /**
     * GET /api/rates
     * Load currency rates using BOrates.loadCurrencyRates()
     *
     * Query params:
     * - filter: Filter criteria (optional)
     * - dao: DAO parameter (optional)
     */
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getCurrencyRates(
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String dao) {
        List<Map<String, Object>> result = ratesService.loadCurrencyRates(filter, dao);
        return ResponseEntity.ok(result);
    }
}

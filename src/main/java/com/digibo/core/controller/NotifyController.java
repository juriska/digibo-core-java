package com.digibo.core.controller;

import com.digibo.core.service.NotifyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * NotifyController - REST controller for notification operations
 * Maps to /api/notify endpoints
 */
@RestController
@RequestMapping("/api/notify")
public class NotifyController {

    private final NotifyService notifyService;

    public NotifyController(NotifyService notifyService) {
        this.notifyService = notifyService;
    }

    /**
     * POST /api/notify/rates-board
     * Notify rates board using BONotify.notifyRatesBoard()
     */
    @PostMapping("/rates-board")
    public ResponseEntity<Map<String, Object>> notifyRatesBoard() {
        Map<String, Object> result = notifyService.notifyRatesBoard();
        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/notify/ffo
     * Notify FFO system using BONotify.notifyFfo()
     */
    @PostMapping("/ffo")
    public ResponseEntity<Map<String, Object>> notifyFfo() {
        Map<String, Object> result = notifyService.notifyFfo();
        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/notify/investment
     * Notify investment system using BONotify.notifyInvestment()
     */
    @PostMapping("/investment")
    public ResponseEntity<Map<String, Object>> notifyInvestment() {
        Map<String, Object> result = notifyService.notifyInvestment();
        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/notify/mortgage-loans
     * Notify mortgage loans system using BONotify.notifyMortgageLoans()
     */
    @PostMapping("/mortgage-loans")
    public ResponseEntity<Map<String, Object>> notifyMortgageLoans() {
        Map<String, Object> result = notifyService.notifyMortgageLoans();
        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/notify/permissions
     * Update BO permissions using BONotify.updateBoPermissions()
     */
    @PostMapping("/permissions")
    public ResponseEntity<Map<String, Object>> updateBoPermissions() {
        Map<String, Object> result = notifyService.updateBoPermissions();
        return ResponseEntity.ok(result);
    }
}

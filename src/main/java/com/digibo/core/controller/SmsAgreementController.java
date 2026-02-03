package com.digibo.core.controller;

import com.digibo.core.exception.ValidationException;
import com.digibo.core.service.SmsAgreementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * SmsAgreementController - REST controller for SMS agreement operations
 * Maps to /api/smsagreement endpoints
 */
@RestController
@RequestMapping("/api/smsagreement")
public class SmsAgreementController {

    private final SmsAgreementService smsAgreementService;

    public SmsAgreementController(SmsAgreementService smsAgreementService) {
        this.smsAgreementService = smsAgreementService;
    }

    /**
     * GET /api/smsagreement/operators
     * Get list of SMS operators
     */
    @GetMapping("/operators")
    public ResponseEntity<List<Map<String, Object>>> getOperators() {
        List<Map<String, Object>> result = smsAgreementService.getOperators();
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/smsagreement/accounts
     * Get accounts for customer
     * Query params: custId, location
     */
    @GetMapping("/accounts")
    public ResponseEntity<List<Map<String, Object>>> getAccounts(
            @RequestParam String custId,
            @RequestParam String location) {
        List<Map<String, Object>> result = smsAgreementService.getAccounts(custId, location);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/smsagreement/logins
     * Get logins for user
     * Query params: userId, custId, location (all optional)
     */
    @GetMapping("/logins")
    public ResponseEntity<List<Map<String, Object>>> getLogins(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long custId,
            @RequestParam(required = false) String location) {
        List<Map<String, Object>> result = smsAgreementService.getLogins(userId, custId, location);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/smsagreement/rights/level1
     * Load rights level 1
     * Query params: wocId, custId, location
     */
    @GetMapping("/rights/level1")
    public ResponseEntity<List<Map<String, Object>>> getRightsLevel1(
            @RequestParam Long wocId,
            @RequestParam Long custId,
            @RequestParam String location) {
        List<Map<String, Object>> result = smsAgreementService.loadRights1(wocId, custId, location);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/smsagreement/rights/level2
     * Load rights level 2
     * Query params: wocId, custId, location
     */
    @GetMapping("/rights/level2")
    public ResponseEntity<List<Map<String, Object>>> getRightsLevel2(
            @RequestParam Long wocId,
            @RequestParam Long custId,
            @RequestParam String location) {
        List<Map<String, Object>> result = smsAgreementService.loadRights2(wocId, custId, location);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/smsagreement/rights/cards
     * Load card rights
     * Query params: wocId, custId, location
     */
    @GetMapping("/rights/cards")
    public ResponseEntity<List<Map<String, Object>>> getCardRights(
            @RequestParam Long wocId,
            @RequestParam Long custId,
            @RequestParam String location) {
        List<Map<String, Object>> result = smsAgreementService.loadCardRights(wocId, custId, location);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/smsagreement/login/check/:login
     * Check if login is valid
     */
    @GetMapping("/login/check/{login}")
    public ResponseEntity<Map<String, Object>> checkLogin(@PathVariable String login) {
        int result = smsAgreementService.checkLogin(login);
        return ResponseEntity.ok(Map.of(
                "login", login,
                "valid", result == 0,
                "resultCode", result
        ));
    }

    /**
     * GET /api/smsagreement/login/count/:login
     * Get login count
     */
    @GetMapping("/login/count/{login}")
    public ResponseEntity<Map<String, Object>> getLoginCount(@PathVariable String login) {
        int count = smsAgreementService.getLoginCount(login);
        return ResponseEntity.ok(Map.of(
                "login", login,
                "count", count
        ));
    }

    /**
     * GET /api/smsagreement/login/exists
     * Check if login exists for customer
     * Query params: wocId, custId, location, login
     */
    @GetMapping("/login/exists")
    public ResponseEntity<Map<String, Object>> loginExists(
            @RequestParam Long wocId,
            @RequestParam Long custId,
            @RequestParam String location,
            @RequestParam String login) {
        int result = smsAgreementService.loginForCustomerExists(wocId, custId, location, login);
        return ResponseEntity.ok(Map.of(
                "wocId", wocId,
                "custId", custId,
                "location", location,
                "login", login,
                "exists", result == 1
        ));
    }

    /**
     * GET /api/smsagreement/channels/:wocId
     * Load channel information
     */
    @GetMapping("/channels/{wocId}")
    public ResponseEntity<Map<String, Object>> getChannel(@PathVariable String wocId) {
        Map<String, Object> result = smsAgreementService.loadChannel(wocId);
        return ResponseEntity.ok(result);
    }
}

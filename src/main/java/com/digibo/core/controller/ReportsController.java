package com.digibo.core.controller;

import com.digibo.core.service.ReportsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * ReportsController - REST controller for reports operations
 * Maps to /api/reports endpoints
 *
 * Authorization: Uses Oracle grants via @PreAuthorize.
 * User must have EXECUTE grant on BOREPORT package procedures.
 */
@RestController
@RequestMapping("/api/reports")
public class ReportsController {

    private final ReportsService reportsService;

    public ReportsController(ReportsService reportsService) {
        this.reportsService = reportsService;
    }

    /**
     * GET /api/reports/unauthorized-conditions
     * Get unauthorized conditions report using BOReport.unauthorizedConditions()
     *
     * Requires: EXECUTE on BOREPORT.UNAUTHORIZED_CONDITIONS
     */
    @GetMapping("/unauthorized-conditions")
    @PreAuthorize("hasPermission(null, 'BOREPORT.UNAUTHORIZED_CONDITIONS')")
    public ResponseEntity<List<Map<String, Object>>> getUnauthorizedConditions() {
        List<Map<String, Object>> result = reportsService.unauthorizedConditions();
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/reports/unauthorized-users
     * Get unauthorized users report using BOReport.unauthorizedUsers()
     *
     * Requires: EXECUTE on BOREPORT.UNAUTHORIZED_USERS
     */
    @GetMapping("/unauthorized-users")
    @PreAuthorize("hasPermission(null, 'BOREPORT.UNAUTHORIZED_USERS')")
    public ResponseEntity<List<Map<String, Object>>> getUnauthorizedUsers() {
        List<Map<String, Object>> result = reportsService.unauthorizedUsers();
        return ResponseEntity.ok(result);
    }
}

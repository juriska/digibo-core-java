package com.digibo.core.controller;

import com.digibo.core.dto.request.NewLicenseRequest;
import com.digibo.core.exception.ValidationException;
import com.digibo.core.service.DFLicenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * DFLicenceController - REST controller for DF licence operations
 * Maps to /api/dflicence endpoints
 */
@RestController
@RequestMapping("/api/dflicence")
public class DFLicenceController {

    private final DFLicenceService dfLicenceService;

    public DFLicenceController(DFLicenceService dfLicenceService) {
        this.dfLicenceService = dfLicenceService;
    }

    /**
     * GET /api/dflicence
     * Get available licenses using BODFLicence.get_licences()
     *
     * @param count Maximum number of licenses to return (default: 10)
     * @return List of available licenses
     */
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getLicences(
            @RequestParam(required = false, defaultValue = "10") Integer count) {

        List<Map<String, Object>> result = dfLicenceService.getLicences(count);
        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/dflicence
     * Create a new license using BODFLicence.new_license()
     *
     * @param request Request body containing the license ID
     * @return Created license information
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createLicense(@RequestBody NewLicenseRequest request) {
        if (request.getId() == null || request.getId().isBlank()) {
            throw new ValidationException("Missing required field: id");
        }

        Map<String, Object> result = dfLicenceService.newLicense(request.getId());
        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/dflicence/{id}/print
     * Mark a license as printed using BODFLicence.print_licence()
     *
     * @param id License ID
     * @return Print operation result
     */
    @PostMapping("/{id}/print")
    public ResponseEntity<Map<String, Object>> printLicence(@PathVariable String id) {
        Map<String, Object> result = dfLicenceService.printLicence(id);
        return ResponseEntity.ok(result);
    }
}

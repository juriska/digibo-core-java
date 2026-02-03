package com.digibo.core.controller;

import com.digibo.core.exception.ResourceNotFoundException;
import com.digibo.core.exception.ValidationException;
import com.digibo.core.service.SysAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * SysAdminController - REST controller for system administration operations
 * Maps to /api/sysadmin endpoints
 */
@RestController
@RequestMapping("/api/sysadmin")
public class SysAdminController {

    private final SysAdminService sysAdminService;

    public SysAdminController(SysAdminService sysAdminService) {
        this.sysAdminService = sysAdminService;
    }

    /**
     * GET /api/sysadmin/replacers
     * Get list of officers with replacement status
     */
    @GetMapping("/replacers")
    public ResponseEntity<List<Map<String, Object>>> getReplacers() {
        List<Map<String, Object>> result = sysAdminService.getReplacers();
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/sysadmin/officers
     * Get officers filtered by login and/or name
     * Query params: login, name (optional)
     */
    @GetMapping("/officers")
    public ResponseEntity<List<Map<String, Object>>> getOfficers(
            @RequestParam(required = false) String login,
            @RequestParam(required = false) String name) {
        List<Map<String, Object>> result = sysAdminService.getOfficers(login, name);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/sysadmin/officers/:id
     * Load officer details with history
     */
    @GetMapping("/officers/{id}")
    public ResponseEntity<Map<String, Object>> getOfficer(@PathVariable Long id) {
        Map<String, Object> result = sysAdminService.loadOfficer(id);

        if (result.get("id") == null) {
            throw new ResourceNotFoundException("Officer", id);
        }

        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/sysadmin/officers/:id/depts
     * Get department list available for officer
     */
    @GetMapping("/officers/{id}/depts")
    public ResponseEntity<List<Map<String, Object>>> getOfficerDepts(@PathVariable Long id) {
        List<Map<String, Object>> result = sysAdminService.getDeptList(id);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/sysadmin/officers/:id/replaces
     * Get list of officers that this officer replaces
     */
    @GetMapping("/officers/{id}/replaces")
    public ResponseEntity<List<Map<String, Object>>> getOfficerReplaces(@PathVariable Long id) {
        List<Map<String, Object>> result = sysAdminService.officerReplaces(id);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/sysadmin/logged
     * Get currently logged officers
     */
    @GetMapping("/logged")
    public ResponseEntity<List<Map<String, Object>>> getLogged() {
        List<Map<String, Object>> result = sysAdminService.getLogged();
        return ResponseEntity.ok(result);
    }

    /**
     * PUT /api/sysadmin/officers/:id
     * Update officer information
     * Body: {
     *   name, login, repId, personalId, deptId, phone, mobile,
     *   email, parentDeptId, defForCountry, skypeName, isLdapUser
     * }
     */
    @PutMapping("/officers/{id}")
    public ResponseEntity<Map<String, Object>> updateOfficer(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {

        String name = (String) body.get("name");
        String login = (String) body.get("login");

        if (name == null || login == null) {
            throw new ValidationException("Missing required fields: name, login");
        }

        Map<String, Object> officerData = Map.ofEntries(
                Map.entry("id", id),
                Map.entry("name", name),
                Map.entry("login", login),
                Map.entry("repId", body.get("repId")),
                Map.entry("personalId", body.get("personalId")),
                Map.entry("deptId", body.get("deptId")),
                Map.entry("phone", body.get("phone")),
                Map.entry("mobile", body.get("mobile")),
                Map.entry("email", body.get("email")),
                Map.entry("parentDeptId", body.get("parentDeptId")),
                Map.entry("defForCountry", body.get("defForCountry")),
                Map.entry("skypeName", body.get("skypeName")),
                Map.entry("isLdapUser", body.get("isLdapUser") != null ? body.get("isLdapUser") : 0)
        );

        sysAdminService.updateOfficer(officerData);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Officer updated successfully",
                "officerId", id
        ));
    }

    /**
     * POST /api/sysadmin/officers/:id/replace
     * Set replacement for officer
     * Body: { repId }
     */
    @PostMapping("/officers/{id}/replace")
    public ResponseEntity<Map<String, Object>> replaceOfficer(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {

        Long repId = body.get("repId") != null ? ((Number) body.get("repId")).longValue() : null;

        sysAdminService.replaceOfficer(id, repId);

        String message = repId != null
                ? String.format("Officer %d is now replaced by officer %d", id, repId)
                : String.format("Replacement for officer %d has been removed", id);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", message,
                "officerId", id,
                "replacedBy", repId != null ? repId : ""
        ));
    }

    /**
     * PUT /api/sysadmin/officers/:id/roles
     * Update officer roles
     * Body: { login, roles }
     */
    @PutMapping("/officers/{id}/roles")
    public ResponseEntity<Map<String, Object>> updateOfficerRoles(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {

        String login = (String) body.get("login");
        String roles = (String) body.get("roles");

        if (login == null || roles == null) {
            throw new ValidationException("Missing required fields: login, roles");
        }

        sysAdminService.updateRoles(id, login, roles);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Officer roles updated successfully",
                "officerId", id,
                "login", login,
                "roles", roles
        ));
    }
}

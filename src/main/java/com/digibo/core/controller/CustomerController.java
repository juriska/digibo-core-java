package com.digibo.core.controller;

import com.digibo.core.dto.request.CheckLoginRequest;
import com.digibo.core.dto.response.ChannelResponse;
import com.digibo.core.dto.response.UserResponse;
import com.digibo.core.exception.ResourceNotFoundException;
import com.digibo.core.exception.ValidationException;
import com.digibo.core.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * CustomerController - REST controller for customer operations
 * Maps to /api/customers endpoints
 *
 * All endpoints require authentication and specific permissions.
 */
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * GET /api/customers/exists/:id
     * Check if customer exists
     */
    @GetMapping("/exists/{id}")
    @PreAuthorize("hasPermission(null, 'BO_CUSTOMER.EXISTS')")
    public ResponseEntity<Map<String, Object>> customerExists(@PathVariable String id) {
        int exists = customerService.customerExists(id);
        return ResponseEntity.ok(Map.of(
                "exists", exists == 1,
                "customerId", id
        ));
    }

    /**
     * GET /api/customers/:id/channels
     * Get user channels
     */
    @GetMapping("/{id}/channels")
    @PreAuthorize("hasPermission(null, 'BO_CUSTOMER.LOAD_USER_CHANNELS')")
    public ResponseEntity<List<Map<String, Object>>> getUserChannels(@PathVariable String id) {
        List<Map<String, Object>> channels = customerService.loadUserChannels(id);
        return ResponseEntity.ok(channels);
    }

    /**
     * GET /api/customers/:id/info
     * Get user info
     */
    @GetMapping("/{id}/info")
    @PreAuthorize("hasPermission(null, 'BO_CUSTOMER.LOAD_USER_INFO')")
    public ResponseEntity<List<Map<String, Object>>> getUserInfo(@PathVariable Long id) {
        List<Map<String, Object>> info = customerService.loadUserInfo(id);
        return ResponseEntity.ok(info);
    }

    /**
     * GET /api/customers/:id/history
     * Get user history
     */
    @GetMapping("/{id}/history")
    @PreAuthorize("hasPermission(null, 'BO_CUSTOMER.LOAD_USER_HISTORY')")
    public ResponseEntity<List<Map<String, Object>>> getUserHistory(@PathVariable Long id) {
        List<Map<String, Object>> history = customerService.loadUserHistory(id);
        return ResponseEntity.ok(history);
    }

    /**
     * GET /api/customers/:custId/tree
     * Get customer tree
     */
    @GetMapping("/{custId}/tree")
    @PreAuthorize("hasPermission(null, 'BO_CUSTOMER.LOAD_CUSTOMER_TREE')")
    public ResponseEntity<List<Map<String, Object>>> getCustomerTree(
            @PathVariable String custId,
            @RequestParam(required = false) String location) {
        List<Map<String, Object>> tree = customerService.loadCustomerTree(custId, location);
        return ResponseEntity.ok(tree);
    }

    /**
     * GET /api/customers/:custId/licenses
     * Get licenses for customer
     */
    @GetMapping("/{custId}/licenses")
    @PreAuthorize("hasPermission(null, 'BO_CUSTOMER.LOAD_LICENSES')")
    public ResponseEntity<List<Map<String, Object>>> getLicenses(@PathVariable String custId) {
        List<Map<String, Object>> licenses = customerService.loadLicenses(custId);
        return ResponseEntity.ok(licenses);
    }

    /**
     * GET /api/customers/license/:id/check
     * Check if license is valid
     */
    @GetMapping("/license/{id}/check")
    @PreAuthorize("hasPermission(null, 'BO_CUSTOMER.CHECK_LICENSE')")
    public ResponseEntity<Map<String, Object>> checkLicense(@PathVariable String id) {
        int valid = customerService.checkLicense(id);
        return ResponseEntity.ok(Map.of(
                "valid", valid == 1,
                "licenseId", id
        ));
    }

    /**
     * POST /api/customers/login/check
     * Check login validity
     */
    @PostMapping("/login/check")
    @PreAuthorize("hasPermission(null, 'BO_CUSTOMER.CHECK_LOGIN')")
    public ResponseEntity<Map<String, Object>> checkLogin(@RequestBody CheckLoginRequest request) {
        if (request.getUserId() == null || request.getLogin() == null ||
                request.getLicense() == null || request.getChannelId() == null) {
            throw new ValidationException("Missing required fields: userId, login, license, channelId");
        }

        int result = customerService.checkLogin(
                request.getUserId(),
                request.getLogin(),
                request.getLicense(),
                request.getChannelId()
        );

        Map<Integer, String> statusMessages = Map.of(
                0, "Login check successful",
                1, "User not found",
                2, "Invalid channel for user",
                3, "Invalid license"
        );

        return ResponseEntity.ok(Map.of(
                "success", result == 0,
                "resultCode", result,
                "message", statusMessages.getOrDefault(result, "Unknown error"),
                "userId", request.getUserId(),
                "login", request.getLogin(),
                "license", request.getLicense(),
                "channelId", request.getChannelId()
        ));
    }

    /**
     * GET /api/customers/search
     * Search users by criteria
     */
    @GetMapping("/search")
    @PreAuthorize("hasPermission(null, 'BO_CUSTOMER.FIND')")
    public ResponseEntity<List<Map<String, Object>>> searchUsers(
            @RequestParam(required = false) String custId,
            @RequestParam(required = false) Long channel,
            @RequestParam(required = false) String license,
            @RequestParam(required = false) String location) {
        List<Map<String, Object>> users = customerService.loadUsers(custId, channel, license, location);
        return ResponseEntity.ok(users);
    }

    /**
     * GET /api/customers/:id
     * Get user details by ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'BO_CUSTOMER.LOAD_USER')")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        UserResponse user = customerService.loadUser(id);
        if (user == null || user.getName() == null) {
            throw new ResourceNotFoundException("User", id);
        }
        return ResponseEntity.ok(user);
    }

    /**
     * GET /api/customers/channel/:wocId/:custId
     * Get channel information
     */
    @GetMapping("/channel/{wocId}/{custId}")
    @PreAuthorize("hasPermission(null, 'BO_CUSTOMER.LOAD_CHANNEL')")
    public ResponseEntity<ChannelResponse> getChannel(
            @PathVariable String wocId,
            @PathVariable String custId) {
        ChannelResponse channel = customerService.loadChannel(wocId, custId);
        return ResponseEntity.ok(channel);
    }
}

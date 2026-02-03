package com.digibo.core.service.mock;

import com.digibo.core.service.AuthPermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Mock implementation of AuthPermissionService for testing without Oracle.
 * Provides predefined permissions for test users.
 */
@Service
@Profile("mock")
public class AuthPermissionServiceMock implements AuthPermissionService {

    private static final Logger logger = LoggerFactory.getLogger(AuthPermissionServiceMock.class);

    // Predefined mock permissions for different user roles
    private static final Map<String, Set<String>> MOCK_PERMISSIONS = new HashMap<>();

    static {
        // Admin user - has all permissions
        Set<String> adminPerms = new HashSet<>();
        adminPerms.add("BOCUSTOMER.FIND");
        adminPerms.add("BOCUSTOMER.GET_BY_ID");
        adminPerms.add("BOCUSTOMER.CREATE");
        adminPerms.add("BOCUSTOMER.UPDATE");
        adminPerms.add("BOCUSTOMER.DELETE");
        adminPerms.add("BOPAYMENT.FIND");
        adminPerms.add("BOPAYMENT.CREATE");
        adminPerms.add("BOPAYMENT.APPROVE");
        adminPerms.add("BOREPORT.UNAUTHORIZED_CONDITIONS");
        adminPerms.add("BOREPORT.UNAUTHORIZED_USERS");
        adminPerms.add("BONOTE.FIND_NOTES");
        adminPerms.add("BONOTE.LOAD_NOTE");
        adminPerms.add("BONOTE.SET_NOTE");
        adminPerms.add("BOSYSADMIN.GET_OFFICERS");
        adminPerms.add("BOSYSADMIN.UPDATE_OFFICER");
        adminPerms.add("BOAUDITLOG.FIND");
        adminPerms.add("BOAUDITLOG.GET_TREE");
        // Add more as needed
        MOCK_PERMISSIONS.put("ADMIN", adminPerms);

        // Regular operator - limited permissions
        Set<String> operatorPerms = new HashSet<>();
        operatorPerms.add("BOCUSTOMER.FIND");
        operatorPerms.add("BOCUSTOMER.GET_BY_ID");
        operatorPerms.add("BOPAYMENT.FIND");
        operatorPerms.add("BONOTE.FIND_NOTES");
        operatorPerms.add("BONOTE.LOAD_NOTE");
        MOCK_PERMISSIONS.put("OPERATOR", operatorPerms);

        // Read-only user
        Set<String> viewerPerms = new HashSet<>();
        viewerPerms.add("BOCUSTOMER.FIND");
        viewerPerms.add("BOCUSTOMER.GET_BY_ID");
        viewerPerms.add("BOREPORT.UNAUTHORIZED_CONDITIONS");
        viewerPerms.add("BOREPORT.UNAUTHORIZED_USERS");
        MOCK_PERMISSIONS.put("VIEWER", viewerPerms);

        // Default test user gets operator permissions
        MOCK_PERMISSIONS.put("TESTUSER", operatorPerms);
        MOCK_PERMISSIONS.put("DEFAULT", operatorPerms);
    }

    @Override
    public Set<String> getUserPermissions(String username) {
        logger.debug("Mock: Getting permissions for user: {}", username);

        String upperUsername = username.toUpperCase();

        // Check if user has specific permissions defined
        if (MOCK_PERMISSIONS.containsKey(upperUsername)) {
            return new HashSet<>(MOCK_PERMISSIONS.get(upperUsername));
        }

        // Return default permissions for unknown users
        logger.debug("Mock: User {} not found, returning default permissions", username);
        return new HashSet<>(MOCK_PERMISSIONS.get("DEFAULT"));
    }

    @Override
    public boolean hasPermission(String username, String packageName, String procedureName) {
        Set<String> permissions = getUserPermissions(username);
        String permissionKey = packageName.toUpperCase() + "." + procedureName.toUpperCase();
        boolean hasPermission = permissions.contains(permissionKey);

        logger.debug("Mock: User {} permission check for {}: {}",
                username, permissionKey, hasPermission);

        return hasPermission;
    }

    /**
     * Add mock permissions for a test user (useful in tests)
     */
    public static void addMockUser(String username, Set<String> permissions) {
        MOCK_PERMISSIONS.put(username.toUpperCase(), permissions);
    }

    /**
     * Remove mock user
     */
    public static void removeMockUser(String username) {
        MOCK_PERMISSIONS.remove(username.toUpperCase());
    }
}

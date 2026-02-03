package com.digibo.core.service;

import java.util.Set;

/**
 * Service interface for fetching user permissions from Oracle.
 * Permissions are based on Oracle grants for PL/SQL procedure execution.
 */
public interface AuthPermissionService {

    /**
     * Get all permissions for a user.
     * Returns a set of permission strings in format "PACKAGE_NAME.PROCEDURE_NAME"
     * e.g., ["BOCUSTOMER.FIND", "BOCUSTOMER.GET_BY_ID", "BOPAYMENT.CREATE"]
     *
     * @param username The Oracle/LDAP username
     * @return Set of permission strings
     */
    Set<String> getUserPermissions(String username);

    /**
     * Check if user has a specific permission.
     *
     * @param username The Oracle/LDAP username
     * @param packageName The PL/SQL package name (e.g., "BOCUSTOMER")
     * @param procedureName The procedure name (e.g., "FIND")
     * @return true if user has permission
     */
    boolean hasPermission(String username, String packageName, String procedureName);

    /**
     * Check if user has permission using combined format.
     *
     * @param username The Oracle/LDAP username
     * @param permission Permission string in format "PACKAGE.PROCEDURE"
     * @return true if user has permission
     */
    default boolean hasPermission(String username, String permission) {
        String[] parts = permission.split("\\.");
        if (parts.length != 2) {
            return false;
        }
        return hasPermission(username, parts[0], parts[1]);
    }
}

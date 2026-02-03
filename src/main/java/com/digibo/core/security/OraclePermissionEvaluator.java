package com.digibo.core.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Set;

/**
 * Custom PermissionEvaluator that checks Oracle PL/SQL permissions from JWT.
 *
 * Usage in controllers:
 * <pre>
 * @PreAuthorize("hasPermission(null, 'BOCUSTOMER.FIND')")
 * public List<Customer> findCustomers(...) { }
 *
 * @PreAuthorize("hasPermission(#customerId, 'BOCUSTOMER.GET_BY_ID')")
 * public Customer getCustomer(@PathVariable Long customerId) { }
 * </pre>
 */
@Component
public class OraclePermissionEvaluator implements PermissionEvaluator {

    private static final Logger logger = LoggerFactory.getLogger(OraclePermissionEvaluator.class);

    private final JwtTokenProvider jwtTokenProvider;

    public OraclePermissionEvaluator(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Evaluate permission with target object.
     *
     * @param authentication Current authentication
     * @param targetDomainObject The target object (can be null)
     * @param permission The permission to check (e.g., "BOCUSTOMER.FIND")
     * @return true if user has the permission
     */
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication == null || permission == null) {
            logger.debug("Permission denied: authentication or permission is null");
            return false;
        }

        String permissionStr = permission.toString().toUpperCase();

        // Get permissions from the authentication principal
        if (authentication.getPrincipal() instanceof UserPrincipal userPrincipal) {
            Set<String> permissions = userPrincipal.getPermissions();
            boolean hasPermission = permissions.contains(permissionStr);

            logger.debug("Permission check for user '{}': {} -> {}",
                    userPrincipal.getUsername(), permissionStr, hasPermission);

            return hasPermission;
        }

        logger.debug("Permission denied: principal is not UserPrincipal");
        return false;
    }

    /**
     * Evaluate permission with target ID and type.
     *
     * @param authentication Current authentication
     * @param targetId The target object ID
     * @param targetType The target type (e.g., "BOCUSTOMER")
     * @param permission The permission/operation (e.g., "FIND")
     * @return true if user has the permission
     */
    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId,
                                  String targetType, Object permission) {
        if (authentication == null || targetType == null || permission == null) {
            logger.debug("Permission denied: required parameters are null");
            return false;
        }

        // Combine targetType and permission to form full permission string
        String fullPermission = targetType.toUpperCase() + "." + permission.toString().toUpperCase();

        return hasPermission(authentication, targetId, fullPermission);
    }
}

package com.digibo.core.service.impl;

import com.digibo.core.service.AuthPermissionService;
import com.digibo.core.service.base.BaseService;
import oracle.jdbc.OracleTypes;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of AuthPermissionService that fetches permissions from Oracle.
 * Calls BO_AUTH.GET_USER_PERMISSIONS to retrieve the list of allowed operations.
 */
@Service
@Profile("!mock")
public class AuthPermissionServiceImpl extends BaseService implements AuthPermissionService {

    // Simple cache to avoid repeated DB calls during a session
    // In production, consider using Spring Cache or Redis
    private final Map<String, CachedPermissions> permissionCache = new ConcurrentHashMap<>();
    private static final long CACHE_TTL_MS = 5 * 60 * 1000; // 5 minutes

    public AuthPermissionServiceImpl() {
        super("BO_AUTH");
    }

    @Override
    public Set<String> getUserPermissions(String username) {
        // Check cache first
        CachedPermissions cached = permissionCache.get(username.toUpperCase());
        if (cached != null && !cached.isExpired()) {
            logger.debug("Returning cached permissions for user: {}", username);
            return cached.permissions;
        }

        logger.debug("Fetching permissions from Oracle for user: {}", username);

        List<SqlParameter> params = List.of(
                inParam("P_USERNAME", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_USERNAME", username.toUpperCase());

        List<Map<String, Object>> results = executeCursorProcedure(
                "GET_USER_PERMISSIONS",
                params,
                inputParams,
                "P_CURSOR",
                (rs, rowNum) -> {
                    Map<String, Object> row = new java.util.HashMap<>();
                    row.put("PACKAGE_NAME", rs.getString("PACKAGE_NAME"));
                    row.put("PROCEDURE_NAME", rs.getString("PROCEDURE_NAME"));
                    return row;
                }
        );

        Set<String> permissions = new HashSet<>();
        for (Map<String, Object> row : results) {
            String packageName = (String) row.get("PACKAGE_NAME");
            String procedureName = (String) row.get("PROCEDURE_NAME");
            if (packageName != null && procedureName != null) {
                permissions.add(packageName.toUpperCase() + "." + procedureName.toUpperCase());
            }
        }

        // Cache the permissions
        permissionCache.put(username.toUpperCase(), new CachedPermissions(permissions));
        logger.debug("Cached {} permissions for user: {}", permissions.size(), username);

        return permissions;
    }

    @Override
    public boolean hasPermission(String username, String packageName, String procedureName) {
        Set<String> permissions = getUserPermissions(username);
        String permissionKey = packageName.toUpperCase() + "." + procedureName.toUpperCase();
        return permissions.contains(permissionKey);
    }

    /**
     * Clear cached permissions for a user (e.g., on logout or permission change)
     */
    public void clearCache(String username) {
        permissionCache.remove(username.toUpperCase());
    }

    /**
     * Clear all cached permissions
     */
    public void clearAllCache() {
        permissionCache.clear();
    }

    private static class CachedPermissions {
        final Set<String> permissions;
        final long timestamp;

        CachedPermissions(Set<String> permissions) {
            this.permissions = permissions;
            this.timestamp = System.currentTimeMillis();
        }

        boolean isExpired() {
            return System.currentTimeMillis() - timestamp > CACHE_TTL_MS;
        }
    }
}

package com.digibo.core.service;

import java.util.List;
import java.util.Map;

/**
 * SysAdminService - Service interface for BOSysAdmin Oracle package
 * Handles system administration operations for officers and replacements
 */
public interface SysAdminService {

    /**
     * Get list of replacers
     * @return Array of officers with their replacement status
     */
    List<Map<String, Object>> getReplacers();

    /**
     * Get officers filtered by login and name
     * @param login Officer login (partial match)
     * @param name Officer name (partial match)
     * @return Array of officers
     */
    List<Map<String, Object>> getOfficers(String login, String name);

    /**
     * Get department list for officer
     * @param officerId Officer ID
     * @return Array of available departments
     */
    List<Map<String, Object>> getDeptList(Long officerId);

    /**
     * Get list of officers that this officer replaces
     * @param officerId Officer ID
     * @return Array of replaced officers
     */
    List<Map<String, Object>> officerReplaces(Long officerId);

    /**
     * Get logged officers
     * @return Array of currently logged officers
     */
    List<Map<String, Object>> getLogged();

    /**
     * Load officer details with history
     * @param officerId Officer ID
     * @return Officer details with history
     */
    Map<String, Object> loadOfficer(Long officerId);

    /**
     * Update officer information
     * @param officerData Officer data to update
     */
    void updateOfficer(Map<String, Object> officerData);

    /**
     * Replace officer
     * @param officerId Officer ID to be replaced
     * @param repId Replacement officer ID
     */
    void replaceOfficer(Long officerId, Long repId);

    /**
     * Update officer roles
     * @param officerId Officer ID
     * @param login Officer login
     * @param roles Comma-separated roles
     */
    void updateRoles(Long officerId, String login, String roles);
}

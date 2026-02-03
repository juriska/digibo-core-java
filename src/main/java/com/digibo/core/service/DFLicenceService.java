package com.digibo.core.service;

import java.util.List;
import java.util.Map;

/**
 * DFLicenceService - Service interface for BODFLicence Oracle package
 * Handles license management operations
 */
public interface DFLicenceService {

    /**
     * Get licenses using the get_licences function
     * Returns cursor with available licenses
     * @param pCount Maximum number of licenses to return
     * @return List of license records
     */
    List<Map<String, Object>> getLicences(Integer pCount);

    /**
     * Create a new license using the new_license procedure
     * Inserts a new license record with status 'G' (Generated)
     * @param pId License ID
     * @return Result map with success, licenseId, and message
     */
    Map<String, Object> newLicense(String pId);

    /**
     * Mark a license as printed using the print_licence procedure
     * Updates license status from 'G' to 'P' (Printed)
     * @param pId License ID
     * @return Result map with success, licenseId, and message
     */
    Map<String, Object> printLicence(String pId);
}

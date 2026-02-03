package com.digibo.core.service.mock;

import com.digibo.core.service.DFLicenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * DFLicenceServiceMock - Mock implementation of DFLicenceService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class DFLicenceServiceMock implements DFLicenceService {

    private static final Logger logger = LoggerFactory.getLogger(DFLicenceServiceMock.class);

    private final List<Map<String, Object>> mockLicences = new ArrayList<>();

    public DFLicenceServiceMock() {
        // Initialize mock licenses
        Map<String, Object> lic1 = new HashMap<>();
        lic1.put("ID", "LIC-001");
        lic1.put("STATUS", "G");
        lic1.put("CHANGE_DATE", new Date());
        lic1.put("CHANGE_OFFICER_ID", 0);
        mockLicences.add(lic1);

        Map<String, Object> lic2 = new HashMap<>();
        lic2.put("ID", "LIC-002");
        lic2.put("STATUS", "G");
        lic2.put("CHANGE_DATE", new Date());
        lic2.put("CHANGE_OFFICER_ID", 0);
        mockLicences.add(lic2);

        Map<String, Object> lic3 = new HashMap<>();
        lic3.put("ID", "LIC-003");
        lic3.put("STATUS", "P");
        lic3.put("CHANGE_DATE", new Date(System.currentTimeMillis() - 86400000));
        lic3.put("CHANGE_OFFICER_ID", 100);
        mockLicences.add(lic3);
    }

    @Override
    public List<Map<String, Object>> getLicences(Integer pCount) {
        logger.debug("[MOCK] DFLicenceService.getLicences({}) called", pCount);

        int count = pCount != null ? pCount : 10;

        List<Map<String, Object>> availableLicences = mockLicences.stream()
                .filter(l -> "G".equals(l.get("STATUS")))
                .limit(count)
                .toList();

        logger.debug("[MOCK] Returning {} licenses", availableLicences.size());
        return new ArrayList<>(availableLicences);
    }

    @Override
    public Map<String, Object> newLicense(String pId) {
        logger.debug("[MOCK] DFLicenceService.newLicense({}) called", pId);

        Map<String, Object> response = new HashMap<>();

        // Check if license already exists
        boolean exists = mockLicences.stream()
                .anyMatch(l -> pId.equals(l.get("ID")));

        if (exists) {
            response.put("success", false);
            response.put("licenseId", pId);
            response.put("message", "License already exists (mock)");
        } else {
            // Add new license
            Map<String, Object> newLic = new HashMap<>();
            newLic.put("ID", pId);
            newLic.put("STATUS", "G");
            newLic.put("CHANGE_DATE", new Date());
            newLic.put("CHANGE_OFFICER_ID", 0);
            mockLicences.add(newLic);

            response.put("success", true);
            response.put("licenseId", pId);
            response.put("message", "License created successfully (mock)");
        }

        return response;
    }

    @Override
    public Map<String, Object> printLicence(String pId) {
        logger.debug("[MOCK] DFLicenceService.printLicence({}) called", pId);

        Map<String, Object> response = new HashMap<>();

        Optional<Map<String, Object>> licenceOpt = mockLicences.stream()
                .filter(l -> pId.equals(l.get("ID")))
                .findFirst();

        if (licenceOpt.isEmpty()) {
            response.put("success", false);
            response.put("licenseId", pId);
            response.put("message", "License not found (mock)");
        } else {
            Map<String, Object> licence = licenceOpt.get();
            licence.put("STATUS", "P");
            licence.put("CHANGE_DATE", new Date());
            licence.put("CHANGE_OFFICER_ID", 100);

            response.put("success", true);
            response.put("licenseId", pId);
            response.put("message", "License marked as printed successfully (mock)");
        }

        return response;
    }
}

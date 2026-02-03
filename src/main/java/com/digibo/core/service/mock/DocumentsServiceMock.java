package com.digibo.core.service.mock;

import com.digibo.core.service.DocumentsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * DocumentsServiceMock - Mock implementation of DocumentsService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class DocumentsServiceMock implements DocumentsService {

    private static final Logger logger = LoggerFactory.getLogger(DocumentsServiceMock.class);

    @Override
    public List<Map<String, Object>> getHistory(String documentId) {
        logger.debug("[MOCK] DocumentsService.getHistory({}) called", documentId);

        List<Map<String, Object>> history = new ArrayList<>();

        Map<String, Object> entry1 = new HashMap<>();
        entry1.put("ID", 1L);
        entry1.put("EVENT_TYPE", "CREATE");
        entry1.put("TIMESTAMP", new Date(System.currentTimeMillis() - 86400000));
        entry1.put("OFFICER_NAME", "Officer Smith");
        entry1.put("DETAILS", "Document created");
        history.add(entry1);

        Map<String, Object> entry2 = new HashMap<>();
        entry2.put("ID", 2L);
        entry2.put("EVENT_TYPE", "STATUS_CHANGE");
        entry2.put("TIMESTAMP", new Date());
        entry2.put("OFFICER_NAME", "Officer Johnson");
        entry2.put("DETAILS", "Status changed to Processing");
        history.add(entry2);

        return history;
    }

    @Override
    public List<Map<String, Object>> getMessageHistory(String documentId) {
        logger.debug("[MOCK] DocumentsService.getMessageHistory({}) called", documentId);

        List<Map<String, Object>> history = new ArrayList<>();

        Map<String, Object> entry1 = new HashMap<>();
        entry1.put("ID", 1L);
        entry1.put("EVENT_TYPE_ID", 100);
        entry1.put("TIMESTAMP", new Date());
        entry1.put("STATUS", 1);
        entry1.put("DETAILS", "Message sent");
        history.add(entry1);

        return history;
    }

    @Override
    public Map<String, Object> setLock(String documentId) {
        logger.debug("[MOCK] DocumentsService.setLock({}) called", documentId);

        Map<String, Object> response = new HashMap<>();

        // Simulate: 20% chance document is locked by another user
        boolean isLocked = new Random().nextDouble() < 0.2;

        if (isLocked) {
            response.put("lockAcquired", false);
            response.put("status", 3);
            Map<String, Object> lockedBy = new HashMap<>();
            lockedBy.put("name", "Officer Jane Smith");
            lockedBy.put("phone", "+371 67 123456");
            response.put("lockedBy", lockedBy);
        } else {
            response.put("lockAcquired", true);
            response.put("status", 1);
            response.put("lockedBy", null);
        }

        return response;
    }

    @Override
    public Map<String, Object> setManualStatus(String documentId, String reason,
                                                Integer newStatus, Integer messageId) {
        logger.debug("[MOCK] DocumentsService.setManualStatus({}, {}, {}, {}) called",
                documentId, reason, newStatus, messageId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("documentId", documentId);
        response.put("newStatus", newStatus);

        return response;
    }

    @Override
    public Map<String, Object> setManualStatusWithRef(String documentId, String reason,
                                                       Integer newStatus, Integer messageId,
                                                       String bankReference) {
        logger.debug("[MOCK] DocumentsService.setManualStatusWithRef({}, {}, {}, {}, {}) called",
                documentId, reason, newStatus, messageId, bankReference);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("documentId", documentId);
        response.put("newStatus", newStatus);
        response.put("bankReference", bankReference);

        return response;
    }

    @Override
    public Map<String, Object> getSignOwner(String certId, Date signDate) {
        logger.debug("[MOCK] DocumentsService.getSignOwner({}, {}) called", certId, signDate);

        Map<String, Object> response = new HashMap<>();

        // Mock signature owners based on cert ID
        if ("CERT_ABC123".equals(certId)) {
            response.put("userName", "John Doe");
            response.put("legalId", "123456-12345");
        } else if ("CERT_XYZ789".equals(certId)) {
            response.put("userName", "Jane Smith");
            response.put("legalId", "654321-54321");
        } else {
            response.put("userName", "Test User");
            response.put("legalId", "111111-11111");
        }

        response.put("certificateId", certId);
        response.put("signatureDate", signDate);

        return response;
    }

    @Override
    public List<Map<String, Object>> getAddresses(String documentId) {
        logger.debug("[MOCK] DocumentsService.getAddresses({}) called", documentId);

        List<Map<String, Object>> addresses = new ArrayList<>();

        Map<String, Object> addr1 = new HashMap<>();
        addr1.put("TYPE_ID", 1);
        addr1.put("RECEIVING_TYPE", "MAIL");
        addr1.put("BANK_OFFICE_NAME", "Main Branch");
        addr1.put("ADDR", "123 Main Street, City");
        addresses.add(addr1);

        return addresses;
    }

    @Override
    public List<Map<String, Object>> getExtensions(String documentId) {
        logger.debug("[MOCK] DocumentsService.getExtensions({}) called", documentId);

        List<Map<String, Object>> extensions = new ArrayList<>();

        Map<String, Object> ext1 = new HashMap<>();
        ext1.put("DICTIONARY_ID", 1);
        ext1.put("ADDITIONAL_INFO", "Extension data 1");
        ext1.put("BLOCK_NUMBER", 1);
        extensions.add(ext1);

        return extensions;
    }

    @Override
    public List<Map<String, Object>> getIBSignatures(String documentId) {
        logger.debug("[MOCK] DocumentsService.getIBSignatures({}) called", documentId);

        List<Map<String, Object>> signatures = new ArrayList<>();

        Map<String, Object> sig1 = new HashMap<>();
        sig1.put("NAME", "John Doe");
        sig1.put("SIGNATURE_ACTION", "APPROVE");
        sig1.put("SIGNATURE_LEVEL", 1);
        sig1.put("SIGNATURE_DATE", new Date());
        sig1.put("SIGNATURE_CDEVICE_TYPE_ID", 5);
        sig1.put("SIGNATURE_CDEVICE_SERIAL", "DEV001");
        sig1.put("DOCUMENT_BATCH_ID", null);
        signatures.add(sig1);

        return signatures;
    }

    @Override
    public Map<String, Object> setManualProcessing(String documentId) {
        logger.debug("[MOCK] DocumentsService.setManualProcessing({}) called", documentId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("documentId", documentId);
        response.put("manualProcessingEnabled", true);

        return response;
    }

    @Override
    public Map<String, Object> getChangeOfficerId(String documentId) {
        logger.debug("[MOCK] DocumentsService.getChangeOfficerId({}) called", documentId);

        Map<String, Object> response = new HashMap<>();
        response.put("documentId", documentId);
        response.put("changeOfficerId", 7890);

        return response;
    }

    @Override
    public Map<String, Object> getById(Integer documentId) {
        logger.debug("[MOCK] DocumentsService.getById({}) called", documentId);

        Map<String, Object> response = new HashMap<>();
        response.put("id", documentId);
        response.put("status", 1);
        response.put("officerId", 100);
        response.put("infoToCustomer", "Mock document info");
        response.put("found", true);

        return response;
    }
}

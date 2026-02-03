package com.digibo.core.controller;

import com.digibo.core.dto.request.SetManualStatusRequest;
import com.digibo.core.dto.request.SetManualStatusWithRefRequest;
import com.digibo.core.dto.request.SignOwnerRequest;
import com.digibo.core.exception.ResourceNotFoundException;
import com.digibo.core.exception.ValidationException;
import com.digibo.core.service.DocumentsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * DocumentsController - REST controller for document operations
 * Maps to /api/documents endpoints
 */
@RestController
@RequestMapping("/api/documents")
public class DocumentsController {

    private final DocumentsService documentsService;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public DocumentsController(DocumentsService documentsService) {
        this.documentsService = documentsService;
    }

    /**
     * GET /api/documents/{id}
     * Get basic document information by ID
     *
     * @param id Document ID
     * @return Document information
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getDocument(@PathVariable String id) {
        Integer documentId = parseInteger(id, "id");
        Map<String, Object> result = documentsService.getById(documentId);

        if (result == null || !Boolean.TRUE.equals(result.get("found"))) {
            throw new ResourceNotFoundException("Document", id);
        }

        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/documents/{id}/history
     * Get document audit history
     *
     * @param id Document ID
     * @return Document history records
     */
    @GetMapping("/{id}/history")
    public ResponseEntity<List<Map<String, Object>>> getDocumentHistory(@PathVariable String id) {
        List<Map<String, Object>> result = documentsService.getHistory(id);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/documents/{id}/messages
     * Get message-related history
     *
     * @param id Document ID
     * @return Message history records
     */
    @GetMapping("/{id}/messages")
    public ResponseEntity<List<Map<String, Object>>> getMessageHistory(@PathVariable String id) {
        List<Map<String, Object>> result = documentsService.getMessageHistory(id);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/documents/{id}/addresses
     * Get document addresses
     *
     * @param id Document ID
     * @return Document addresses
     */
    @GetMapping("/{id}/addresses")
    public ResponseEntity<List<Map<String, Object>>> getDocumentAddresses(@PathVariable String id) {
        List<Map<String, Object>> result = documentsService.getAddresses(id);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/documents/{id}/extensions
     * Get document extensions
     *
     * @param id Document ID
     * @return Document extensions
     */
    @GetMapping("/{id}/extensions")
    public ResponseEntity<List<Map<String, Object>>> getDocumentExtensions(@PathVariable String id) {
        List<Map<String, Object>> result = documentsService.getExtensions(id);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/documents/{id}/signatures
     * Get Internet Banking signatures
     *
     * @param id Document ID
     * @return Document signatures
     */
    @GetMapping("/{id}/signatures")
    public ResponseEntity<List<Map<String, Object>>> getDocumentSignatures(@PathVariable String id) {
        List<Map<String, Object>> result = documentsService.getIBSignatures(id);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/documents/{id}/change-officer
     * Get change officer ID for a document
     *
     * @param id Document ID
     * @return Change officer information
     */
    @GetMapping("/{id}/change-officer")
    public ResponseEntity<Map<String, Object>> getChangeOfficer(@PathVariable String id) {
        Map<String, Object> result = documentsService.getChangeOfficerId(id);
        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/documents/{id}/lock
     * Lock document for editing
     *
     * @param id Document ID
     * @return Lock operation result
     */
    @PostMapping("/{id}/lock")
    public ResponseEntity<Map<String, Object>> lockDocument(@PathVariable String id) {
        Map<String, Object> result = documentsService.setLock(id);

        boolean lockAcquired = Boolean.TRUE.equals(result.get("lockAcquired"));
        result.put("message", lockAcquired ? "Document locked successfully" : "Document is locked by another user");

        if (!lockAcquired) {
            return ResponseEntity.status(HttpStatus.LOCKED).body(result);
        }

        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/documents/{id}/status
     * Set document status manually
     *
     * @param id Document ID
     * @param request Request body containing reason, newStatus, and messageId
     * @return Status update result
     */
    @PostMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> setDocumentStatus(
            @PathVariable String id,
            @RequestBody SetManualStatusRequest request) {

        if (request.getReason() == null || request.getNewStatus() == null || request.getMessageId() == null) {
            throw new ValidationException("Missing required fields: reason, newStatus, messageId");
        }

        Map<String, Object> result = documentsService.setManualStatus(
                id,
                request.getReason(),
                parseInteger(request.getNewStatus(), "newStatus"),
                parseInteger(request.getMessageId(), "messageId")
        );

        result.put("message", "Document status updated successfully");
        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/documents/{id}/status-with-ref
     * Set document status manually with bank reference
     *
     * @param id Document ID
     * @param request Request body containing reason, newStatus, messageId, and bankReference
     * @return Status update result
     */
    @PostMapping("/{id}/status-with-ref")
    public ResponseEntity<Map<String, Object>> setDocumentStatusWithRef(
            @PathVariable String id,
            @RequestBody SetManualStatusWithRefRequest request) {

        if (request.getReason() == null || request.getNewStatus() == null ||
                request.getMessageId() == null || request.getBankReference() == null) {
            throw new ValidationException("Missing required fields: reason, newStatus, messageId, bankReference");
        }

        Map<String, Object> result = documentsService.setManualStatusWithRef(
                id,
                request.getReason(),
                parseInteger(request.getNewStatus(), "newStatus"),
                parseInteger(request.getMessageId(), "messageId"),
                request.getBankReference()
        );

        result.put("message", "Document status updated successfully with bank reference");
        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/documents/{id}/manual-processing
     * Enable manual processing for a document
     *
     * @param id Document ID
     * @return Manual processing result
     */
    @PostMapping("/{id}/manual-processing")
    public ResponseEntity<Map<String, Object>> enableManualProcessing(@PathVariable String id) {
        Map<String, Object> result = documentsService.setManualProcessing(id);
        result.put("message", "Manual processing enabled");
        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/documents/sign-owner
     * Get signature owner information
     *
     * @param request Request body containing certificateId and signatureDate
     * @return Sign owner information
     */
    @PostMapping("/sign-owner")
    public ResponseEntity<Map<String, Object>> getSignOwner(@RequestBody SignOwnerRequest request) {
        if (request.getCertificateId() == null || request.getSignatureDate() == null) {
            throw new ValidationException("Missing required fields: certificateId, signatureDate");
        }

        Date signDate = parseDate(request.getSignatureDate(), "signatureDate");

        Map<String, Object> result = documentsService.getSignOwner(
                request.getCertificateId(),
                signDate
        );
        return ResponseEntity.ok(result);
    }

    /**
     * Parse integer from string
     */
    private Integer parseInteger(String value, String paramName) {
        if (value == null || value.isBlank()) {
            throw new ValidationException("Missing required parameter: " + paramName);
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new ValidationException("Invalid " + paramName + ": must be a valid integer");
        }
    }

    /**
     * Parse date string in ISO format (yyyy-MM-dd or yyyy-MM-ddTHH:mm:ss)
     */
    private Date parseDate(String dateString, String paramName) {
        if (dateString == null || dateString.isBlank()) {
            return null;
        }
        try {
            // Try full ISO datetime format first
            if (dateString.contains("T")) {
                synchronized (DATETIME_FORMAT) {
                    return DATETIME_FORMAT.parse(dateString);
                }
            }
            // Try date-only format
            synchronized (DATE_FORMAT) {
                return DATE_FORMAT.parse(dateString);
            }
        } catch (ParseException e) {
            throw new ValidationException(
                    "Invalid " + paramName + " date format. Use ISO format (YYYY-MM-DD or YYYY-MM-DDTHH:mm:ss)");
        }
    }
}

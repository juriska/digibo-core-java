package com.digibo.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * DocumentsService - Service interface for BODocuments Oracle package
 * Handles document operations including history, locking, status changes
 */
public interface DocumentsService {

    /**
     * Get document audit history
     * @param documentId Document ID
     * @return List of audit log entries
     */
    List<Map<String, Object>> getHistory(String documentId);

    /**
     * Get message-related history for a document
     * @param documentId Document/Message ID
     * @return List of message history entries
     */
    List<Map<String, Object>> getMessageHistory(String documentId);

    /**
     * Lock document for editing
     * @param documentId Document ID
     * @return Map with lockAcquired, status, and lockedBy info
     */
    Map<String, Object> setLock(String documentId);

    /**
     * Set document status manually
     * @param documentId Document ID
     * @param reason Reason for status change
     * @param newStatus New status ID
     * @param messageId Message ID for audit
     * @return Map with success indicator
     */
    Map<String, Object> setManualStatus(String documentId, String reason,
                                         Integer newStatus, Integer messageId);

    /**
     * Set document status manually with bank reference
     * @param documentId Document ID
     * @param reason Reason for status change
     * @param newStatus New status ID
     * @param messageId Message ID for audit
     * @param bankReference Bank reference number
     * @return Map with success indicator
     */
    Map<String, Object> setManualStatusWithRef(String documentId, String reason,
                                                Integer newStatus, Integer messageId,
                                                String bankReference);

    /**
     * Get signature owner information
     * @param certId Certificate ID
     * @param signDate Signature date
     * @return Map with userName, legalId, certificateId, signatureDate
     */
    Map<String, Object> getSignOwner(String certId, Date signDate);

    /**
     * Get document addresses
     * @param documentId Document ID
     * @return List of addresses
     */
    List<Map<String, Object>> getAddresses(String documentId);

    /**
     * Get document extensions
     * @param documentId Document ID
     * @return List of extensions
     */
    List<Map<String, Object>> getExtensions(String documentId);

    /**
     * Get Internet Banking signatures for a document
     * @param documentId Document ID
     * @return List of signatures
     */
    List<Map<String, Object>> getIBSignatures(String documentId);

    /**
     * Enable manual processing for a document
     * @param documentId Document ID
     * @return Map with success, documentId, manualProcessingEnabled
     */
    Map<String, Object> setManualProcessing(String documentId);

    /**
     * Get change officer ID for a document
     * @param documentId Document ID
     * @return Map with documentId and changeOfficerId
     */
    Map<String, Object> getChangeOfficerId(String documentId);

    /**
     * Get basic document information by ID
     * @param documentId Document ID
     * @return Map with id, status, officerId, infoToCustomer, found
     */
    Map<String, Object> getById(Integer documentId);
}

package com.digibo.core.service;

import java.util.List;
import java.util.Map;

/**
 * FaxViewService - Service interface for BOFaxView Oracle package
 * Handles fax viewing, locking, and navigation operations
 */
public interface FaxViewService {

    /**
     * Find user's assigned fax documents
     *
     * @param classes Comma-separated class IDs
     * @return List of assigned fax documents with id, recvTime, docId, officerId, custId, acc, amnt, ccy, docClass, status, note, partner, subject
     */
    List<Map<String, Object>> findMyDocuments(String classes);

    /**
     * Set lock on fax or fax document
     *
     * @param id Fax ID or document ID
     * @param doc 0 for fax, 1 for document
     * @return Lock result with lockStatus (0=Success, 1=LockedBy, 2=Locked, 3=Error), id, officerName, officerPhone
     */
    Map<String, Object> setLock(String id, Integer doc);

    /**
     * Load history of fax document changes
     *
     * @param id Document ID
     * @return List of historical changes with changeDate, changeOfficer, docClass, custId, acc, amnt, ccy, officerId, partner, subj, status, note
     */
    List<Map<String, Object>> loadHistory(String id);

    /**
     * Load actual (current) state of fax document
     *
     * @param id Document ID
     * @return List with current document state
     */
    List<Map<String, Object>> loadActual(String id);

    /**
     * Get last officer who processed document for customer
     *
     * @param custId Customer ID
     * @param fromAccount Account number
     * @param classId Document class ID (0 = any)
     * @param officers Comma-separated officer IDs
     * @return Officer ID or null
     */
    Long lastOfficer(Long custId, String fromAccount, Integer classId, String officers);

    /**
     * Get next new fax ID
     *
     * @return Next fax ID or null if none
     */
    Long nextFaxId();

    /**
     * Get next document ID for user's documents
     *
     * @param docId Current document ID (to find next after this)
     * @param classes Comma-separated class IDs
     * @return Map with faxId and nextDocId
     */
    Map<String, Object> nextDocumentId(Long docId, String classes);

    /**
     * Load fax with all its details and documents
     *
     * @param id Fax ID
     * @param docId Optional document ID filter
     * @return Fax details including id, docId, fromFax, fromCSid, recvTime, recvStatus, faxStatus, fTif, documents list
     */
    Map<String, Object> loadFax(String id, String docId);

    /**
     * Initialize/get basic info for fax document
     *
     * @param id Document ID
     * @return Map with classId and statusId
     */
    Map<String, Object> init(String id);
}

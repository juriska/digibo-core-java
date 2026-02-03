package com.digibo.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * FFOService - Service interface for BOFFO Oracle package
 * Handles Free-Form Orders and related operations
 */
public interface FFOService {

    /**
     * Get user's FFO documents using find_my function
     *
     * @return List of FFO documents with ID, CLASS_ID, STATUS_ID, ORDER_DATE, DOCUMENT_NUMBER,
     *         CREATOR_CHANNEL_ID, LOGIN, FF_SUBJECT, WOC_ID, GLB_CUST_ID, SECTOR, SEGMENT,
     *         ISDOCUMENTATTACHED, CATEGORY_ID, SUBCATEGORY_ID, CATEGORY_NAME, SUBCATEGORY_NAME,
     *         ASSIGNEE, DOCUMENT_ATTACHED
     */
    List<Map<String, Object>> findMy();

    /**
     * Get FFO document by ID
     *
     * @param documentId Document ID
     * @return FFO document details or null if not found
     */
    Map<String, Object> getById(String documentId);

    /**
     * Get FFO categories
     *
     * @return List of categories
     */
    List<Map<String, Object>> getCategories();

    /**
     * Categorize FFO document
     *
     * @param docId Document ID
     * @param categoryId Category ID
     * @param subCategoryId Subcategory ID
     * @param assignee Assignee officer ID
     * @return Result map with success, documentId, categoryId, subCategoryId, assignee, result code
     */
    Map<String, Object> categorize(Long docId, Long categoryId, Long subCategoryId, Long assignee);

    /**
     * Find FFO documents by filters
     *
     * @param custId Customer ID
     * @param custName Customer name
     * @param userLogin User login
     * @param officerId Officer ID
     * @param docClass Document class
     * @param subject Subject
     * @param text Text content
     * @param docId Document ID
     * @param channels Comma-separated channel IDs
     * @param statuses Comma-separated status IDs
     * @param createdFrom Created date from
     * @param createdTill Created date till
     * @param assignee Assignee officer ID
     * @param categoryId Category ID
     * @param subcategoryId Subcategory ID
     * @return List of matching FFO documents
     */
    List<Map<String, Object>> find(
            String custId,
            String custName,
            String userLogin,
            Long officerId,
            String docClass,
            String subject,
            String text,
            String docId,
            String channels,
            String statuses,
            Date createdFrom,
            Date createdTill,
            Long assignee,
            Long categoryId,
            Long subcategoryId
    );

    /**
     * Get detailed FFO document information
     *
     * @param docId Document ID
     * @return Detailed FFO document info with id, userName, userId, officerName, goldManager,
     *         custName, custAccount, globusNo, location, fText, infoToCustomer, infoToBank,
     *         signTime, signRSA, sector, segment
     */
    Map<String, Object> ffo(String docId);

    /**
     * Set processing status for FFO document
     *
     * @param docId Document ID
     * @param reason Reason for status change
     * @param newStatus New status ID
     * @param messageId Message ID
     * @return Result map with success, documentId, newStatus, result code
     */
    Map<String, Object> setProcessing(String docId, String reason, Integer newStatus, Long messageId);
}

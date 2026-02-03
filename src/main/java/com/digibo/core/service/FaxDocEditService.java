package com.digibo.core.service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * FaxDocEditService - Service interface for BOFaxDocEdit Oracle package
 * Handles fax document editing operations
 */
public interface FaxDocEditService {

    /**
     * Save fax document using the save_document procedure
     * Updates fax document with new values and creates history record
     *
     * @param docId Document ID
     * @param officerId Officer ID
     * @param custId Customer ID
     * @param fromAccount From account number
     * @param amnt Amount
     * @param ccy Currency code
     * @param partner Partner name
     * @param note Note
     * @param subj Subject
     * @param docStatus Document status ID
     * @return Result map with success indicator, documentId, and message
     */
    Map<String, Object> saveDocument(
            String docId,
            Long officerId,
            String custId,
            String fromAccount,
            BigDecimal amnt,
            String ccy,
            String partner,
            String note,
            String subj,
            Integer docStatus
    );
}

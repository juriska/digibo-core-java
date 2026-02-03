package com.digibo.core.service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * FaxEditService - Service interface for BOFaxEdit Oracle package
 * Handles fax document creation and editing operations
 */
public interface FaxEditService {

    /**
     * Add a new document from fax
     *
     * @param faxId Fax ID
     * @param docClass Document class ID
     * @param officerId Officer ID
     * @param custId Customer ID
     * @param fromAccount From account number
     * @param amnt Amount
     * @param ccy Currency code
     * @param partner Partner name
     * @param note Note text
     * @param subj Subject
     * @param docStatus Document status
     * @param dTif Document TIF image (BLOB as byte array)
     * @return Result map with success indicator, faxId, and message
     */
    Map<String, Object> addDocument(
            String faxId,
            Integer docClass,
            Long officerId,
            String custId,
            String fromAccount,
            BigDecimal amnt,
            String ccy,
            String partner,
            String note,
            String subj,
            Integer docStatus,
            byte[] dTif
    );
}

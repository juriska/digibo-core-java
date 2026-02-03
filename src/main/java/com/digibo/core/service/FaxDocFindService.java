package com.digibo.core.service;

import java.util.List;
import java.util.Map;

/**
 * FaxDocFindService - Service interface for BOFaxDocFind Oracle package
 * Handles fax document search operations
 */
public interface FaxDocFindService {

    /**
     * Find fax documents using the find function with multiple filter parameters
     * Returns list of fax document data
     *
     * @param faxId Fax ID
     * @param fromFax From fax number
     * @param fromCSid From CSID
     * @param docId Document ID
     * @param custId Customer ID
     * @param fromAccount From account number
     * @param amountFrom Amount from
     * @param amountTo Amount to
     * @param docCcy Document currency
     * @param officerId Officer ID
     * @param docClass Document class
     * @param classes Comma-separated classes
     * @param statuses Comma-separated statuses
     * @param partner Partner name
     * @param subj Subject
     * @param recvTimeFrom Receive time from (Unix timestamp)
     * @param recvTimeTo Receive time to (Unix timestamp)
     * @return List of fax documents
     */
    List<Map<String, Object>> find(
            String faxId,
            String fromFax,
            String fromCSid,
            String docId,
            String custId,
            String fromAccount,
            String amountFrom,
            String amountTo,
            String docCcy,
            Long officerId,
            Integer docClass,
            String classes,
            String statuses,
            String partner,
            String subj,
            Long recvTimeFrom,
            Long recvTimeTo
    );
}

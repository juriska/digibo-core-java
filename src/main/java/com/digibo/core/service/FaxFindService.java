package com.digibo.core.service;

import java.util.List;
import java.util.Map;

/**
 * FaxFindService - Service interface for BOFaxFind Oracle package
 * Handles fax search and retrieval operations
 */
public interface FaxFindService {

    /**
     * Find fax documents by various filters
     *
     * @param faxId Fax ID
     * @param fromFax From fax number
     * @param fromCSid From CSID
     * @param faxStatus Fax status (0 = all)
     * @param recvTimeFrom Receive time from (timestamp)
     * @param recvTimeTo Receive time to (timestamp)
     * @return List of fax documents with faxId, fromFax, fromCSid, recvTime, recvStatus, faxStatus
     */
    List<Map<String, Object>> find(
            String faxId,
            String fromFax,
            String fromCSid,
            Integer faxStatus,
            Long recvTimeFrom,
            Long recvTimeTo
    );

    /**
     * Find new fax documents (status = 1)
     *
     * @return List of new fax documents with faxId, fromFax, fromCSid, recvTime, recvStatus, faxStatus
     */
    List<Map<String, Object>> findNew();
}

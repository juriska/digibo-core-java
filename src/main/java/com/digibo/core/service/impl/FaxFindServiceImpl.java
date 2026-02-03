package com.digibo.core.service.impl;

import com.digibo.core.service.FaxFindService;
import com.digibo.core.service.base.BaseService;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FaxFindServiceImpl - Real implementation of FaxFindService
 * Calls BOFaxFind Oracle package procedures
 */
@Service
@Profile("!mock")
public class FaxFindServiceImpl extends BaseService implements FaxFindService {

    public FaxFindServiceImpl() {
        super("BOFaxFind");
    }

    @Override
    public List<Map<String, Object>> find(
            String faxId,
            String fromFax,
            String fromCSid,
            Integer faxStatus,
            Long recvTimeFrom,
            Long recvTimeTo
    ) {
        logger.debug("Calling BOFaxFind.find() with filters");

        List<SqlParameter> params = List.of(
                inParam("P_FAX_ID", Types.VARCHAR),
                inParam("P_FROM_FAX", Types.VARCHAR),
                inParam("P_FROM_CSID", Types.VARCHAR),
                inParam("P_FAX_STATUS", Types.NUMERIC),
                inParam("P_RECV_TIME_FROM", Types.NUMERIC),
                inParam("P_RECV_TIME_TO", Types.NUMERIC)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_FAX_ID", faxId);
        inputParams.put("P_FROM_FAX", fromFax);
        inputParams.put("P_FROM_CSID", fromCSid);
        inputParams.put("P_FAX_STATUS", faxStatus != null ? faxStatus : 0);
        inputParams.put("P_RECV_TIME_FROM", recvTimeFrom != null ? recvTimeFrom : 0L);
        inputParams.put("P_RECV_TIME_TO", recvTimeTo != null ? recvTimeTo : 0L);

        List<Map<String, Object>> rows = executeCursorProcedure("find", params, inputParams, "P_CURSOR",
                (rs, rowNum) -> {
                    Map<String, Object> row = new HashMap<>();
                    int colCount = rs.getMetaData().getColumnCount();
                    for (int i = 1; i <= colCount; i++) {
                        String colName = rs.getMetaData().getColumnName(i);
                        row.put(colName, rs.getObject(i));
                    }
                    return row;
                });

        logger.debug("find returned {} rows", rows.size());
        return rows;
    }

    @Override
    public List<Map<String, Object>> findNew() {
        logger.debug("Calling BOFaxFind.find_new()");

        List<SqlParameter> params = List.of();
        Map<String, Object> inputParams = Map.of();

        List<Map<String, Object>> rows = executeCursorProcedure("find_new", params, inputParams, "P_CURSOR",
                (rs, rowNum) -> {
                    Map<String, Object> row = new HashMap<>();
                    int colCount = rs.getMetaData().getColumnCount();
                    for (int i = 1; i <= colCount; i++) {
                        String colName = rs.getMetaData().getColumnName(i);
                        row.put(colName, rs.getObject(i));
                    }
                    return row;
                });

        logger.debug("find_new returned {} rows", rows.size());
        return rows;
    }
}

package com.digibo.core.service.impl;

import com.digibo.core.service.FaxDocFindService;
import com.digibo.core.service.base.BaseService;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FaxDocFindServiceImpl - Real implementation of FaxDocFindService
 * Calls BOFaxDocFind Oracle package procedures
 */
@Service
@Profile("!mock")
public class FaxDocFindServiceImpl extends BaseService implements FaxDocFindService {

    public FaxDocFindServiceImpl() {
        super("BOFaxDocFind");
    }

    @Override
    public List<Map<String, Object>> find(
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
    ) {
        logger.debug("Calling BOFaxDocFind.find()");

        List<SqlParameter> params = List.of(
                inParam("P_FAX_ID", Types.VARCHAR),
                inParam("P_FROM_FAX", Types.VARCHAR),
                inParam("P_FROM_CSID", Types.VARCHAR),
                inParam("P_DOC_ID", Types.VARCHAR),
                inParam("P_CUST_ID", Types.VARCHAR),
                inParam("P_FROM_ACCOUNT", Types.VARCHAR),
                inParam("P_AMOUNT_FROM", Types.VARCHAR),
                inParam("P_AMOUNT_TO", Types.VARCHAR),
                inParam("P_DOC_CCY", Types.VARCHAR),
                inParam("P_OFFICER_ID", Types.NUMERIC),
                inParam("P_DOC_CLASS", Types.NUMERIC),
                inParam("P_CLASSES", Types.VARCHAR),
                inParam("P_STATUSES", Types.VARCHAR),
                inParam("P_PARTNER", Types.VARCHAR),
                inParam("P_SUBJ", Types.VARCHAR),
                inParam("P_RECV_TIME_FROM", Types.NUMERIC),
                inParam("P_RECV_TIME_TO", Types.NUMERIC)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_FAX_ID", faxId);
        inputParams.put("P_FROM_FAX", fromFax);
        inputParams.put("P_FROM_CSID", fromCSid);
        inputParams.put("P_DOC_ID", docId);
        inputParams.put("P_CUST_ID", custId);
        inputParams.put("P_FROM_ACCOUNT", fromAccount);
        inputParams.put("P_AMOUNT_FROM", amountFrom);
        inputParams.put("P_AMOUNT_TO", amountTo);
        inputParams.put("P_DOC_CCY", docCcy);
        inputParams.put("P_OFFICER_ID", officerId != null ? officerId : 0L);
        inputParams.put("P_DOC_CLASS", docClass != null ? docClass : 0);
        inputParams.put("P_CLASSES", classes);
        inputParams.put("P_STATUSES", statuses);
        inputParams.put("P_PARTNER", partner);
        inputParams.put("P_SUBJ", subj);
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
}

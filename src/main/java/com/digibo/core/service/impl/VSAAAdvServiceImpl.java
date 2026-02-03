package com.digibo.core.service.impl;

import com.digibo.core.service.VSAAAdvService;
import com.digibo.core.service.base.BaseService;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * VSAAAdvServiceImpl - Real implementation of VSAAAdvService
 * Calls BOVsaaAdv Oracle package procedures
 */
@Service
@Profile("!mock")
public class VSAAAdvServiceImpl extends BaseService implements VSAAAdvService {

    public VSAAAdvServiceImpl() {
        super("BOVsaaAdv");
    }

    @Override
    public List<Map<String, Object>> find(String custId, String custName, String docId, String statuses,
                                           Date createdFrom, Date createdTill) {
        logger.debug("Calling BOVsaaAdv.find({}, {}, {}, {}, {}, {})",
                custId, custName, docId, statuses, createdFrom, createdTill);

        List<SqlParameter> params = List.of(
                inParam("P_CUST_ID", Types.VARCHAR),
                inParam("P_CUST_NAME", Types.VARCHAR),
                inParam("P_DOC_ID", Types.VARCHAR),
                inParam("P_STATUSES", Types.VARCHAR),
                inParam("P_CREATED_FROM", Types.DATE),
                inParam("P_CREATED_TILL", Types.DATE)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_CUST_ID", custId);
        inputParams.put("P_CUST_NAME", custName);
        inputParams.put("P_DOC_ID", docId);
        inputParams.put("P_STATUSES", statuses);
        inputParams.put("P_CREATED_FROM", createdFrom);
        inputParams.put("P_CREATED_TILL", createdTill);

        return executeCursorProcedure("find", params, inputParams, "P_CURSOR",
                (rs, rowNum) -> {
                    Map<String, Object> row = new HashMap<>();
                    int colCount = rs.getMetaData().getColumnCount();
                    for (int i = 1; i <= colCount; i++) {
                        String colName = rs.getMetaData().getColumnName(i);
                        row.put(colName, rs.getObject(i));
                    }
                    return row;
                });
    }

    @Override
    public Map<String, Object> adv(String docId) {
        logger.debug("Calling BOVsaaAdv.adv({})", docId);

        List<SqlParameter> inParams = List.of(
                inParam("P_ID", Types.VARCHAR)
        );

        List<SqlOutParameter> outParams = List.of(
                outParam("P_OFFICER_NAME", Types.VARCHAR),
                outParam("P_CREATE_SENT_DATE", Types.DATE),
                outParam("P_CLOSE_SENT_DATE", Types.DATE)
        );

        Map<String, Object> inputParams = Map.of("P_ID", docId);

        Map<String, Object> result = executeProcedureWithOutputs("adv", inParams, outParams, inputParams);

        Map<String, Object> advDoc = new HashMap<>();
        advDoc.put("id", docId);
        advDoc.put("officerName", result.get("P_OFFICER_NAME"));
        advDoc.put("createSentDate", result.get("P_CREATE_SENT_DATE"));
        advDoc.put("closeSentDate", result.get("P_CLOSE_SENT_DATE"));

        return advDoc;
    }
}

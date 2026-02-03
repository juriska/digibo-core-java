package com.digibo.core.service.impl;

import com.digibo.core.service.VSAAAdvAppService;
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
 * VSAAAdvAppServiceImpl - Real implementation of VSAAAdvAppService
 * Calls BOVsaaAdvApp Oracle package procedures
 */
@Service
@Profile("!mock")
public class VSAAAdvAppServiceImpl extends BaseService implements VSAAAdvAppService {

    public VSAAAdvAppServiceImpl() {
        super("BOVsaaAdvApp");
    }

    @Override
    public List<Map<String, Object>> find(String custId, String custName, String userLogin, String docClass,
                                           String legalId, String docId, String statuses,
                                           Date createdFrom, Date createdTill) {
        logger.debug("Calling BOVsaaAdvApp.find({}, {}, {}, {}, {}, {}, {}, {}, {})",
                custId, custName, userLogin, docClass, legalId, docId, statuses, createdFrom, createdTill);

        List<SqlParameter> params = List.of(
                inParam("P_CUST_ID", Types.VARCHAR),
                inParam("P_CUST_NAME", Types.VARCHAR),
                inParam("P_USER_LOGIN", Types.VARCHAR),
                inParam("P_DOC_CLASS", Types.VARCHAR),
                inParam("P_LEGAL_ID", Types.VARCHAR),
                inParam("P_DOC_ID", Types.VARCHAR),
                inParam("P_STATUSES", Types.VARCHAR),
                inParam("P_CREATED_FROM", Types.DATE),
                inParam("P_CREATED_TILL", Types.DATE)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_CUST_ID", custId);
        inputParams.put("P_CUST_NAME", custName);
        inputParams.put("P_USER_LOGIN", userLogin);
        inputParams.put("P_DOC_CLASS", docClass);
        inputParams.put("P_LEGAL_ID", legalId);
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
    public Map<String, Object> advapp(String docId) {
        logger.debug("Calling BOVsaaAdvApp.advapp({})", docId);

        List<SqlParameter> inParams = List.of(
                inParam("P_ID", Types.VARCHAR)
        );

        List<SqlOutParameter> outParams = List.of(
                outParam("P_USER_NAME", Types.VARCHAR),
                outParam("P_USER_ID", Types.VARCHAR),
                outParam("P_OFFICER_NAME", Types.VARCHAR),
                outParam("P_DOC_NO", Types.VARCHAR),
                outParam("P_CUST_NAME", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_ID", docId);

        Map<String, Object> result = executeProcedureWithOutputs("advapp", inParams, outParams, inputParams);

        Map<String, Object> advappDoc = new HashMap<>();
        advappDoc.put("id", docId);
        advappDoc.put("userName", result.get("P_USER_NAME"));
        advappDoc.put("userId", result.get("P_USER_ID"));
        advappDoc.put("officerName", result.get("P_OFFICER_NAME"));
        advappDoc.put("docNo", result.get("P_DOC_NO"));
        advappDoc.put("custName", result.get("P_CUST_NAME"));

        return advappDoc;
    }
}

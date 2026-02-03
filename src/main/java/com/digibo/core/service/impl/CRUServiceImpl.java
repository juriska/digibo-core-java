package com.digibo.core.service.impl;

import com.digibo.core.service.CRUService;
import com.digibo.core.service.base.BaseService;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.*;

/**
 * CRUServiceImpl - Real implementation of CRUService
 * Calls BOCRU Oracle package procedures
 */
@Service
@Profile("!mock")
public class CRUServiceImpl extends BaseService implements CRUService {

    public CRUServiceImpl() {
        super("BOCRU");
    }

    @Override
    public List<Map<String, Object>> find(String custId, String docId, String statuses,
                                           Date createdFrom, Date createdTill) {
        logger.debug("Calling BOCRU.find() with filters");

        List<SqlParameter> params = List.of(
                inParam("P_CUST_ID", Types.VARCHAR),
                inParam("P_DOC_ID", Types.VARCHAR),
                inParam("P_STATUSES", Types.VARCHAR),
                inParam("P_CREATED_FROM", Types.DATE),
                inParam("P_CREATED_TILL", Types.DATE)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_CUST_ID", custId);
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
    public Map<String, Object> cru(String docId) {
        logger.debug("Calling BOCRU.cru({})", docId);

        List<SqlParameter> inParams = List.of(
                inParam("P_ID", Types.VARCHAR)
        );

        List<SqlOutParameter> outParams = List.of(
                outParam("P_DOC_NO", Types.VARCHAR),
                outParam("P_USER_NAME", Types.VARCHAR),
                outParam("P_USER_ID", Types.VARCHAR),
                outParam("P_CUST_NAME", Types.VARCHAR),
                outParam("P_GLOBUS_NO", Types.VARCHAR),
                outParam("P_LOCATION", Types.VARCHAR),
                outParam("P_ITC", Types.VARCHAR),
                outParam("P_ITB", Types.VARCHAR),
                outParam("P_SIGN_TIME", Types.TIMESTAMP),
                outParam("P_SIGN_DEV_TYPE", Types.NUMERIC),
                outParam("P_SIGN_DEV_ID", Types.VARCHAR),
                outParam("P_SIGN_KEY1", Types.VARCHAR),
                outParam("P_SIGN_KEY2", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_ID", docId);

        Map<String, Object> result = executeProcedureWithOutputs("cru", inParams, outParams, inputParams);

        Map<String, Object> response = new HashMap<>();
        response.put("id", docId);
        response.put("docNo", result.get("P_DOC_NO"));
        response.put("userName", result.get("P_USER_NAME"));
        response.put("userId", result.get("P_USER_ID"));
        response.put("custName", result.get("P_CUST_NAME"));
        response.put("globusNo", result.get("P_GLOBUS_NO"));
        response.put("location", result.get("P_LOCATION"));
        response.put("infoToCustomer", result.get("P_ITC"));
        response.put("infoToBank", result.get("P_ITB"));
        response.put("signTime", result.get("P_SIGN_TIME"));
        response.put("signDevType", result.get("P_SIGN_DEV_TYPE"));
        response.put("signDevId", result.get("P_SIGN_DEV_ID"));
        response.put("signKey1", result.get("P_SIGN_KEY1"));
        response.put("signKey2", result.get("P_SIGN_KEY2"));

        return response;
    }
}

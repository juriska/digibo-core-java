package com.digibo.core.service.impl;

import com.digibo.core.service.CrontoDocService;
import com.digibo.core.service.base.BaseService;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.*;

/**
 * CrontoDocServiceImpl - Real implementation of CrontoDocService
 * Calls BOCRONTODOC Oracle package procedures
 */
@Service
@Profile("!mock")
public class CrontoDocServiceImpl extends BaseService implements CrontoDocService {

    public CrontoDocServiceImpl() {
        super("BOCRONTODOC");
    }

    @Override
    public List<Map<String, Object>> find(String custId, String custName, String userLogin,
                                           Long officerId, String pType, String docId,
                                           String statuses, Date createdFrom, Date createdTill) {
        logger.debug("Calling BOCRONTODOC.find() with filters");

        List<SqlParameter> params = List.of(
                inParam("P_CUST_ID", Types.VARCHAR),
                inParam("P_CUST_NAME", Types.VARCHAR),
                inParam("P_USER_LOGIN", Types.VARCHAR),
                inParam("P_OFFICER_ID", Types.NUMERIC),
                inParam("P_TYPE", Types.VARCHAR),
                inParam("P_DOC_ID", Types.VARCHAR),
                inParam("P_STATUSES", Types.VARCHAR),
                inParam("P_CREATED_FROM", Types.DATE),
                inParam("P_CREATED_TILL", Types.DATE)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_CUST_ID", custId);
        inputParams.put("P_CUST_NAME", custName);
        inputParams.put("P_USER_LOGIN", userLogin);
        inputParams.put("P_OFFICER_ID", officerId);
        inputParams.put("P_TYPE", pType);
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
    public int setProcessing(String docId) {
        logger.debug("Calling BOCRONTODOC.set_processing({})", docId);

        List<SqlParameter> params = List.of(
                inParam("P_ID", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_ID", docId);

        Integer result = executeScalarFunction("set_processing", params, inputParams, Types.INTEGER);
        return result != null ? result : 0;
    }

    @Override
    public List<Map<String, Object>> findMy(Long officerId) {
        logger.debug("Calling BOCRONTODOC.find_my({})", officerId);

        List<SqlParameter> params = List.of(
                inParam("P_OFFICER_ID", Types.NUMERIC)
        );

        Map<String, Object> inputParams = Map.of("P_OFFICER_ID", officerId != null ? officerId : 0L);

        return executeCursorProcedure("find_my", params, inputParams, "P_CURSOR",
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
}

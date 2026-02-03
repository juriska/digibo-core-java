package com.digibo.core.service.impl;

import com.digibo.core.service.CredLimIncService;
import com.digibo.core.service.base.BaseService;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.*;

/**
 * CredLimIncServiceImpl - Real implementation of CredLimIncService
 * Calls BOCredLimInc Oracle package procedures
 */
@Service
@Profile("!mock")
public class CredLimIncServiceImpl extends BaseService implements CredLimIncService {

    public CredLimIncServiceImpl() {
        super("BOCredLimInc");
    }

    @Override
    public List<Map<String, Object>> find(String custId, String custName, String userLogin,
                                           String docId, String statuses, String docClass,
                                           Date createdFrom, Date createdTill, Long officerId,
                                           String fromLocation) {
        logger.debug("Calling BOCredLimInc.find() with filters");

        List<SqlParameter> params = List.of(
                inParam("P_CUST_ID", Types.VARCHAR),
                inParam("P_CUST_NAME", Types.VARCHAR),
                inParam("P_USER_LOGIN", Types.VARCHAR),
                inParam("P_DOC_ID", Types.VARCHAR),
                inParam("P_STATUSES", Types.VARCHAR),
                inParam("P_DOC_CLASS", Types.VARCHAR),
                inParam("P_CREATED_FROM", Types.DATE),
                inParam("P_CREATED_TILL", Types.DATE),
                inParam("P_OFFICER_ID", Types.NUMERIC),
                inParam("P_FROM_LOCATION", Types.VARCHAR)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_CUST_ID", custId);
        inputParams.put("P_CUST_NAME", custName);
        inputParams.put("P_USER_LOGIN", userLogin);
        inputParams.put("P_DOC_ID", docId);
        inputParams.put("P_STATUSES", statuses);
        inputParams.put("P_DOC_CLASS", docClass);
        inputParams.put("P_CREATED_FROM", createdFrom);
        inputParams.put("P_CREATED_TILL", createdTill);
        inputParams.put("P_OFFICER_ID", officerId);
        inputParams.put("P_FROM_LOCATION", fromLocation);

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
    public List<Map<String, Object>> findMy(Long officerId) {
        logger.debug("Calling BOCredLimInc.find_my({})", officerId);

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

    @Override
    public Map<String, Object> setProcessing(String docId) {
        logger.debug("Setting processing status for document {}", docId);

        List<SqlParameter> params = List.of(
                inParam("P_ID", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_ID", docId);

        Integer result = executeScalarFunction("set_processing", params, inputParams, Types.INTEGER);
        int resultValue = result != null ? result : 0;

        Map<String, Object> response = new HashMap<>();
        response.put("success", resultValue > 0);
        response.put("documentId", docId);
        response.put("officerId", resultValue);
        response.put("result", resultValue);

        return response;
    }
}

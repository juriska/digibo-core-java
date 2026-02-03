package com.digibo.core.service.impl;

import com.digibo.core.service.FiAccOpenService;
import com.digibo.core.service.base.BaseService;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FiAccOpenServiceImpl - Real implementation of FiAccOpenService
 * Calls BOFiaccopen Oracle package procedures
 */
@Service
@Profile("!mock")
public class FiAccOpenServiceImpl extends BaseService implements FiAccOpenService {

    public FiAccOpenServiceImpl() {
        super("BOFiaccopen");
    }

    @Override
    public List<Map<String, Object>> find(
            String custId,
            String custName,
            String userLogin,
            String docId,
            String statuses,
            String docClass,
            Date createdFrom,
            Date createdTill
    ) {
        logger.debug("Calling BOFiaccopen.find() with filters");

        List<SqlParameter> params = List.of(
                inParam("P_CUST_ID", Types.VARCHAR),
                inParam("P_CUST_NAME", Types.VARCHAR),
                inParam("P_USER_LOGIN", Types.VARCHAR),
                inParam("P_DOC_ID", Types.VARCHAR),
                inParam("P_STATUSES", Types.VARCHAR),
                inParam("P_DOC_CLASS", Types.VARCHAR),
                inParam("P_CREATED_FROM", Types.DATE),
                inParam("P_CREATED_TILL", Types.DATE)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_CUST_ID", custId);
        inputParams.put("P_CUST_NAME", custName);
        inputParams.put("P_USER_LOGIN", userLogin);
        inputParams.put("P_DOC_ID", docId);
        inputParams.put("P_STATUSES", statuses);
        inputParams.put("P_DOC_CLASS", docClass != null ? docClass : "321,322");
        inputParams.put("P_CREATED_FROM", createdFrom);
        inputParams.put("P_CREATED_TILL", createdTill);

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
    public List<Map<String, Object>> findMy(Long officerId) {
        logger.debug("Calling BOFiaccopen.find_my({})", officerId);

        List<SqlParameter> params = List.of(
                inParam("P_OFFICER_ID", Types.NUMERIC)
        );

        Map<String, Object> inputParams = Map.of("P_OFFICER_ID", officerId != null ? officerId : 0L);

        List<Map<String, Object>> rows = executeCursorProcedure("find_my", params, inputParams, "P_CURSOR",
                (rs, rowNum) -> {
                    Map<String, Object> row = new HashMap<>();
                    int colCount = rs.getMetaData().getColumnCount();
                    for (int i = 1; i <= colCount; i++) {
                        String colName = rs.getMetaData().getColumnName(i);
                        row.put(colName, rs.getObject(i));
                    }
                    return row;
                });

        logger.debug("find_my returned {} rows", rows.size());
        return rows;
    }

    @Override
    public Integer setProcessing(String docId, Integer statusIdFrom, Integer statusIdTo) {
        logger.debug("Calling BOFiaccopen.set_processing({}, {}, {})", docId, statusIdFrom, statusIdTo);

        List<SqlParameter> params = List.of(
                inParam("P_ID", Types.VARCHAR),
                inParam("P_STATUS_ID_FROM", Types.NUMERIC),
                inParam("P_STATUS_ID_TO", Types.NUMERIC)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_ID", docId);
        inputParams.put("P_STATUS_ID_FROM", statusIdFrom);
        inputParams.put("P_STATUS_ID_TO", statusIdTo);

        Integer result = executeScalarFunction("set_processing", params, inputParams, Types.INTEGER);

        logger.debug("set_processing returned {}", result);
        return result;
    }
}

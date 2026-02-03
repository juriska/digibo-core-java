package com.digibo.core.service.impl;

import com.digibo.core.service.InsuranceService;
import com.digibo.core.service.base.BaseService;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.*;

/**
 * InsuranceServiceImpl - Real implementation of InsuranceService
 * Calls BOInsurance Oracle package procedures
 */
@Service
@Profile("!mock")
public class InsuranceServiceImpl extends BaseService implements InsuranceService {

    public InsuranceServiceImpl() {
        super("BOInsurance");
    }

    @Override
    public List<Map<String, Object>> find(String custId, String custName, String userLogin,
                                           Long officerId, String docClass, String docId,
                                           String channels, String statuses,
                                           Date createdFrom, Date createdTill, String fromLocation) {
        logger.debug("Calling BOInsurance.find()");

        List<SqlParameter> params = List.of(
                inParam("P_CUST_ID", Types.VARCHAR),
                inParam("P_CUST_NAME", Types.VARCHAR),
                inParam("P_USER_LOGIN", Types.VARCHAR),
                inParam("P_OFFICER_ID", Types.NUMERIC),
                inParam("P_DOC_CLASS", Types.VARCHAR),
                inParam("P_DOC_ID", Types.VARCHAR),
                inParam("P_CHANNELS", Types.VARCHAR),
                inParam("P_STATUSES", Types.VARCHAR),
                inParam("P_CREATED_FROM", Types.DATE),
                inParam("P_CREATED_TILL", Types.DATE),
                inParam("P_FROM_LOCATION", Types.VARCHAR)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_CUST_ID", custId);
        inputParams.put("P_CUST_NAME", custName);
        inputParams.put("P_USER_LOGIN", userLogin);
        inputParams.put("P_OFFICER_ID", officerId);
        inputParams.put("P_DOC_CLASS", docClass);
        inputParams.put("P_DOC_ID", docId);
        inputParams.put("P_CHANNELS", channels);
        inputParams.put("P_STATUSES", statuses);
        inputParams.put("P_CREATED_FROM", createdFrom);
        inputParams.put("P_CREATED_TILL", createdTill);
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
    public List<Map<String, Object>> findMy() {
        logger.debug("Calling BOInsurance.findMy()");

        List<SqlParameter> params = List.of();
        Map<String, Object> inputParams = Map.of();

        return executeCursorProcedure("findMy", params, inputParams, "P_CURSOR",
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

package com.digibo.core.service.impl;

import com.digibo.core.service.ReportsService;
import com.digibo.core.service.base.BaseService;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Profile("!mock")
public class ReportsServiceImpl extends BaseService implements ReportsService {

    public ReportsServiceImpl() {
        super("BOREPORT");
    }

    @Override
    public List<Map<String, Object>> unauthorizedConditions() {
        logger.debug("Calling BOREPORT.unauthorized_conditions()");

        List<SqlParameter> params = List.of();
        Map<String, Object> inputParams = Map.of();

        return executeCursorProcedure("unauthorized_conditions", params, inputParams, "P_CURSOR",
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
    public List<Map<String, Object>> unauthorizedUsers() {
        logger.debug("Calling BOREPORT.unauthorized_users()");

        List<SqlParameter> params = List.of();
        Map<String, Object> inputParams = Map.of();

        return executeCursorProcedure("unauthorized_users", params, inputParams, "P_CURSOR",
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

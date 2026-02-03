package com.digibo.core.service.impl;

import com.digibo.core.service.ReportService;
import com.digibo.core.service.base.BaseService;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.util.*;

/**
 * ReportServiceImpl - Real implementation of ReportService
 * Calls BOReport Oracle package procedures
 */
@Service
@Profile("!mock")
public class ReportServiceImpl extends BaseService implements ReportService {

    public ReportServiceImpl() {
        super("BOReport");
    }

    @Override
    public List<Map<String, Object>> unauthorizedConditions() {
        logger.debug("Calling BOReport.unauthorizedConditions()");

        List<SqlParameter> params = List.of();
        Map<String, Object> inputParams = Map.of();

        return executeCursorProcedure("unauthorizedConditions", params, inputParams, "P_CURSOR",
                (rs, rowNum) -> mapRow(rs));
    }

    @Override
    public List<Map<String, Object>> unauthorizedUsers() {
        logger.debug("Calling BOReport.unauthorizedUsers()");

        List<SqlParameter> params = List.of();
        Map<String, Object> inputParams = Map.of();

        return executeCursorProcedure("unauthorizedUsers", params, inputParams, "P_CURSOR",
                (rs, rowNum) -> mapRow(rs));
    }

    private Map<String, Object> mapRow(ResultSet rs) throws java.sql.SQLException {
        Map<String, Object> row = new HashMap<>();
        int colCount = rs.getMetaData().getColumnCount();
        for (int i = 1; i <= colCount; i++) {
            String colName = rs.getMetaData().getColumnName(i);
            row.put(colName, rs.getObject(i));
        }
        return row;
    }
}

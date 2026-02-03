package com.digibo.core.service.impl;

import com.digibo.core.service.AuditLogService;
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
 * AuditLogServiceImpl - Real implementation of AuditLogService
 * Calls BOAuditLog Oracle package procedures
 */
@Service
@Profile("!mock")
public class AuditLogServiceImpl extends BaseService implements AuditLogService {

    public AuditLogServiceImpl() {
        super("BOAuditLog");
    }

    @Override
    public List<Map<String, Object>> find(Date dfrom, Date dto, String events, String pObject,
                                          String pOriginator, String pChannels, Integer pResultSetSize) {
        logger.debug("Calling BOAuditLog.find({}, {}, {}, {}, {}, {}, {})",
                dfrom, dto, events, pObject, pOriginator, pChannels, pResultSetSize);

        List<SqlParameter> params = List.of(
                inParam("P_DFROM", Types.DATE),
                inParam("P_DTO", Types.DATE),
                inParam("P_EVENTS", Types.VARCHAR),
                inParam("P_OBJECT", Types.VARCHAR),
                inParam("P_ORIGINATOR", Types.VARCHAR),
                inParam("P_CHANNELS", Types.VARCHAR),
                inParam("P_RESULT_SET_SIZE", Types.NUMERIC)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_DFROM", dfrom);
        inputParams.put("P_DTO", dto);
        inputParams.put("P_EVENTS", events);
        inputParams.put("P_OBJECT", pObject);
        inputParams.put("P_ORIGINATOR", pOriginator);
        inputParams.put("P_CHANNELS", pChannels);
        inputParams.put("P_RESULT_SET_SIZE", pResultSetSize);

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
    public List<Map<String, Object>> findSession(String pSession) {
        logger.debug("Calling BOAuditLog.findSession({})", pSession);

        List<SqlParameter> params = List.of(
                inParam("P_SESSION", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_SESSION", pSession);

        return executeCursorProcedure("findSession", params, inputParams, "P_CURSOR",
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
    public List<Map<String, Object>> getTree() {
        logger.debug("Calling BOAuditLog.get_tree()");

        List<SqlParameter> params = List.of();

        Map<String, Object> inputParams = Map.of();

        return executeCursorProcedure("get_tree", params, inputParams, "P_CURSOR",
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

package com.digibo.core.service.impl;

import com.digibo.core.service.SMSService;
import com.digibo.core.service.base.BaseService;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SMSServiceImpl - Real implementation of SMSService
 * Calls BOSMS Oracle package procedures
 */
@Service
@Profile("!mock")
public class SMSServiceImpl extends BaseService implements SMSService {

    public SMSServiceImpl() {
        super("BOSMS");
    }

    @Override
    public Map<String, Object> loadUserData(Long wocId) {
        logger.debug("Calling BOSMS.loadUserData({})", wocId);

        List<SqlParameter> inParams = List.of(
                inParam("P_WOC_ID", Types.NUMERIC)
        );

        List<SqlOutParameter> outParams = List.of(
                outParam("P_USER_NAME", Types.VARCHAR),
                outParam("P_LOGIN", Types.VARCHAR),
                outParam("P_LANG", Types.VARCHAR),
                outParam("P_ACTIVE", Types.NUMERIC),
                outParam("P_ACCEPT", Types.NUMERIC)
        );

        Map<String, Object> inputParams = Map.of("P_WOC_ID", wocId);

        CursorResult<Map<String, Object>> result = executeCursorProcedureWithOutputs(
                "loadUserData", inParams, outParams, inputParams, "P_CURSOR",
                (rs, rowNum) -> {
                    Map<String, Object> row = new HashMap<>();
                    int colCount = rs.getMetaData().getColumnCount();
                    for (int i = 1; i <= colCount; i++) {
                        String colName = rs.getMetaData().getColumnName(i);
                        row.put(colName, rs.getObject(i));
                    }
                    return row;
                });

        Map<String, Object> userData = new HashMap<>();
        userData.put("wocId", wocId);
        userData.put("userName", result.getOutput("P_USER_NAME"));
        userData.put("login", result.getOutput("P_LOGIN"));
        userData.put("language", result.getOutput("P_LANG"));
        userData.put("active", toInteger(result.getOutput("P_ACTIVE")));
        userData.put("accept", toInteger(result.getOutput("P_ACCEPT")));
        userData.put("details", result.getRows());

        return userData;
    }

    private Integer toInteger(Object value) {
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).intValue();
        return Integer.parseInt(value.toString());
    }
}

package com.digibo.core.service.impl;

import com.digibo.core.service.HelpDeskService;
import com.digibo.core.service.base.BaseService;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.*;

/**
 * HelpDeskServiceImpl - Real implementation of HelpDeskService
 * Calls BOHelpDesk Oracle package procedures
 */
@Service
@Profile("!mock")
public class HelpDeskServiceImpl extends BaseService implements HelpDeskService {

    public HelpDeskServiceImpl() {
        super("BOHelpDesk");
    }

    @Override
    public List<Map<String, Object>> findUserChannel(String login, String authDev, String userName, String personalId) {
        logger.debug("Calling BOHelpDesk.find_user_channel()");

        List<SqlParameter> params = List.of(
                inParam("P_LOGIN", Types.VARCHAR),
                inParam("P_AUTH_DEV", Types.VARCHAR),
                inParam("P_USER_NAME", Types.VARCHAR),
                inParam("P_PERSONAL_ID", Types.VARCHAR)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_LOGIN", login);
        inputParams.put("P_AUTH_DEV", authDev);
        inputParams.put("P_USER_NAME", userName);
        inputParams.put("P_PERSONAL_ID", personalId);

        return executeCursorProcedure("find_user_channel", params, inputParams, "P_CURSOR",
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
    public List<Map<String, Object>> loadLog(String userId, String wocId) {
        logger.debug("Calling BOHelpDesk.load_log({}, {})", userId, wocId);

        List<SqlParameter> params = List.of(
                inParam("P_USER_ID", Types.VARCHAR),
                inParam("P_WOC_ID", Types.VARCHAR)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_USER_ID", userId);
        inputParams.put("P_WOC_ID", wocId);

        return executeCursorProcedure("load_log", params, inputParams, "P_CURSOR",
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
    public int setPassword(String channelId, String userId, String password) {
        logger.debug("Calling BOHelpDesk.set_password({}, {})", channelId, userId);

        List<SqlParameter> params = List.of(
                inParam("P_CHANNEL_ID", Types.VARCHAR),
                inParam("P_USER_ID", Types.VARCHAR),
                inParam("P_PASSWORD", Types.VARCHAR)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_CHANNEL_ID", channelId);
        inputParams.put("P_USER_ID", userId);
        inputParams.put("P_PASSWORD", password);

        Integer result = executeScalarFunction("set_password", params, inputParams, Types.INTEGER);
        return result != null ? result : -1;
    }

    @Override
    public Map<String, Object> loadUserChannel(Long id) {
        logger.debug("Calling BOHelpDesk.load_user_channel({})", id);

        List<SqlParameter> inParams = List.of(
                inParam("P_ID", Types.NUMERIC)
        );

        List<SqlOutParameter> outParams = List.of(
                outParam("P_LOGIN", Types.VARCHAR),
                outParam("P_AUTH_DEV", Types.VARCHAR),
                outParam("P_STATUS", Types.NUMERIC),
                outParam("P_SUB_STATUS", Types.NUMERIC),
                outParam("P_USER_ID", Types.VARCHAR),
                outParam("P_USER_NAME", Types.VARCHAR),
                outParam("P_PERSONAL_ID", Types.VARCHAR),
                outParam("P_PHONE", Types.VARCHAR),
                outParam("P_MOBILE", Types.VARCHAR),
                outParam("P_FAX", Types.VARCHAR),
                outParam("P_EMAIL", Types.VARCHAR),
                outParam("P_STREET", Types.VARCHAR),
                outParam("P_CITY", Types.VARCHAR),
                outParam("P_COUNTRY", Types.VARCHAR),
                outParam("P_ZIP", Types.VARCHAR),
                outParam("P_REG_DATE", Types.DATE),
                outParam("P_PASSWORD_CHANGE_ALLOWED", Types.NUMERIC),
                outParam("P_HAS_CRONTO", Types.NUMERIC),
                outParam("P_PASSWORD", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_ID", id);

        CursorResult<Map<String, Object>> result = executeCursorProcedureWithOutputs(
                "load_user_channel", inParams, outParams, inputParams, "P_CURSOR",
                (rs, rowNum) -> {
                    Map<String, Object> row = new HashMap<>();
                    int colCount = rs.getMetaData().getColumnCount();
                    for (int i = 1; i <= colCount; i++) {
                        String colName = rs.getMetaData().getColumnName(i);
                        row.put(colName, rs.getObject(i));
                    }
                    return row;
                });

        Map<String, Object> outputs = result.getOutputs();
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", id);
        userInfo.put("login", outputs.get("P_LOGIN"));
        userInfo.put("authDev", outputs.get("P_AUTH_DEV"));
        userInfo.put("status", toInteger(outputs.get("P_STATUS")));
        userInfo.put("subStatus", toInteger(outputs.get("P_SUB_STATUS")));
        userInfo.put("userId", outputs.get("P_USER_ID"));
        userInfo.put("userName", outputs.get("P_USER_NAME"));
        userInfo.put("personalId", outputs.get("P_PERSONAL_ID"));
        userInfo.put("phone", outputs.get("P_PHONE"));
        userInfo.put("mobile", outputs.get("P_MOBILE"));
        userInfo.put("fax", outputs.get("P_FAX"));
        userInfo.put("email", outputs.get("P_EMAIL"));
        userInfo.put("street", outputs.get("P_STREET"));
        userInfo.put("city", outputs.get("P_CITY"));
        userInfo.put("country", outputs.get("P_COUNTRY"));
        userInfo.put("zip", outputs.get("P_ZIP"));
        userInfo.put("regDate", outputs.get("P_REG_DATE"));
        userInfo.put("passwordChangeAllowed", toInteger(outputs.get("P_PASSWORD_CHANGE_ALLOWED")));
        userInfo.put("hasCronto", toInteger(outputs.get("P_HAS_CRONTO")));
        userInfo.put("password", outputs.get("P_PASSWORD"));

        Map<String, Object> response = new HashMap<>();
        response.put("channelData", result.getRows());
        response.put("userInfo", userInfo);
        return response;
    }

    @Override
    public Map<String, Object> loadAuthInfo(Long id) {
        logger.debug("Calling BOHelpDesk.load_auth_info({})", id);

        List<SqlParameter> inParams = List.of(
                inParam("P_ID", Types.NUMERIC)
        );

        List<SqlOutParameter> outParams = List.of(
                outParam("P_STD_Q", Types.NUMERIC),
                outParam("P_SPEC_Q", Types.VARCHAR),
                outParam("P_ANSWER", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_ID", id);

        Map<String, Object> result = executeProcedureWithOutputs("load_auth_info", inParams, outParams, inputParams);

        Map<String, Object> response = new HashMap<>();
        response.put("id", id);
        response.put("stdQ", toInteger(result.get("P_STD_Q")));
        response.put("specQ", result.get("P_SPEC_Q"));
        response.put("answer", result.get("P_ANSWER"));
        return response;
    }

    @Override
    public boolean setLock(String channelId, String userId, Integer status, Integer subStatus) {
        logger.debug("Calling BOHelpDesk.set_lock({}, {}, {}, {})", channelId, userId, status, subStatus);

        List<SqlParameter> params = List.of(
                inParam("P_CHANNEL_ID", Types.VARCHAR),
                inParam("P_USER_ID", Types.VARCHAR),
                inParam("P_STATUS", Types.NUMERIC),
                inParam("P_SUB_STATUS", Types.NUMERIC)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_CHANNEL_ID", channelId);
        inputParams.put("P_USER_ID", userId);
        inputParams.put("P_STATUS", status);
        inputParams.put("P_SUB_STATUS", subStatus);

        executeVoidProcedure("set_lock", params, inputParams);
        return true;
    }

    @Override
    public boolean resetStolen(String channelId) {
        logger.debug("Calling BOHelpDesk.reset_stolen({})", channelId);

        List<SqlParameter> params = List.of(
                inParam("P_CHANNEL_ID", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_CHANNEL_ID", channelId);

        executeVoidProcedure("reset_stolen", params, inputParams);
        return true;
    }

    private Integer toInteger(Object value) {
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).intValue();
        return Integer.parseInt(value.toString());
    }
}

package com.digibo.core.service.impl;

import com.digibo.core.service.MessageService;
import com.digibo.core.service.base.BaseService;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.*;

/**
 * MessageServiceImpl - Real implementation of MessageService
 * Calls BOMessage Oracle package procedures
 */
@Service
@Profile("!mock")
public class MessageServiceImpl extends BaseService implements MessageService {

    public MessageServiceImpl() {
        super("BOMessage");
    }

    @Override
    public List<Map<String, Object>> findMessages(String userId, String userName, String login,
                                                   Long officerId, String msgId, String message,
                                                   String type, String custId, String custName,
                                                   String statuses, Long classId,
                                                   Date dateFrom, Date dateTill, String channelId) {
        logger.debug("Calling BOMessage.find_messages()");

        List<SqlParameter> params = List.of(
                inParam("P_USER_ID", Types.VARCHAR),
                inParam("P_USER_NAME", Types.VARCHAR),
                inParam("P_LOGIN", Types.VARCHAR),
                inParam("P_OFFICER_ID", Types.NUMERIC),
                inParam("P_MSG_ID", Types.VARCHAR),
                inParam("P_MESSAGE", Types.VARCHAR),
                inParam("P_TYPE", Types.VARCHAR),
                inParam("P_CUST_ID", Types.VARCHAR),
                inParam("P_CUST_NAME", Types.VARCHAR),
                inParam("P_STATUSES", Types.VARCHAR),
                inParam("P_CLASS_ID", Types.NUMERIC),
                inParam("P_DATE_FROM", Types.DATE),
                inParam("P_DATE_TILL", Types.DATE),
                inParam("P_CHANNEL_ID", Types.VARCHAR)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_USER_ID", userId);
        inputParams.put("P_USER_NAME", userName);
        inputParams.put("P_LOGIN", login);
        inputParams.put("P_OFFICER_ID", officerId);
        inputParams.put("P_MSG_ID", msgId);
        inputParams.put("P_MESSAGE", message);
        inputParams.put("P_TYPE", type);
        inputParams.put("P_CUST_ID", custId);
        inputParams.put("P_CUST_NAME", custName);
        inputParams.put("P_STATUSES", statuses);
        inputParams.put("P_CLASS_ID", classId);
        inputParams.put("P_DATE_FROM", dateFrom);
        inputParams.put("P_DATE_TILL", dateTill);
        inputParams.put("P_CHANNEL_ID", channelId);

        return executeCursorProcedure("find_messages", params, inputParams, "P_CURSOR",
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
    public List<Map<String, Object>> findCurrent(String classes) {
        logger.debug("Calling BOMessage.find_current({})", classes);

        List<SqlParameter> params = List.of(
                inParam("P_CLASSES", Types.VARCHAR)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_CLASSES", classes);

        return executeCursorProcedure("find_current", params, inputParams, "P_CURSOR",
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
    public Map<String, Object> loadUserData(Long wocId, String msgId) {
        logger.debug("Calling BOMessage.load_user_data({}, {})", wocId, msgId);

        List<SqlParameter> inParams = List.of(
                inParam("P_WOC_ID", Types.NUMERIC),
                inParam("P_MSG_ID", Types.VARCHAR)
        );

        List<SqlOutParameter> outParams = List.of(
                outParam("P_USER_NAME", Types.VARCHAR),
                outParam("P_LOGIN", Types.VARCHAR),
                outParam("P_FROM_CUST", Types.VARCHAR),
                outParam("P_CUST_ID", Types.NUMERIC)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_WOC_ID", wocId);
        inputParams.put("P_MSG_ID", msgId);

        CursorResult<Map<String, Object>> result = executeCursorProcedureWithOutputs(
                "load_user_data", inParams, outParams, inputParams, "P_CURSOR",
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
        Map<String, Object> response = new HashMap<>();
        response.put("userName", outputs.get("P_USER_NAME"));
        response.put("login", outputs.get("P_LOGIN"));
        response.put("fromCust", outputs.get("P_FROM_CUST"));
        response.put("custId", toLong(outputs.get("P_CUST_ID")));
        response.put("customers", result.getRows());
        return response;
    }

    @Override
    public List<Map<String, Object>> loadCommunication(Long wocId) {
        logger.debug("Calling BOMessage.load_communication({})", wocId);

        List<SqlParameter> params = List.of(
                inParam("P_WOC_ID", Types.NUMERIC)
        );

        Map<String, Object> inputParams = Map.of("P_WOC_ID", wocId);

        return executeCursorProcedure("load_communication", params, inputParams, "P_CURSOR",
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
    public Map<String, Object> setLock(String lockName, String id) {
        logger.debug("Calling BOMessage.set_lock({}, {})", lockName, id);

        List<SqlParameter> inParams = List.of(
                inParam("P_LOCK_NAME", Types.VARCHAR),
                inParam("P_ID", Types.VARCHAR)
        );

        List<SqlOutParameter> outParams = List.of(
                outParam("P_OFFICER_NAME", Types.VARCHAR),
                outParam("P_OFFICER_PHONE", Types.VARCHAR),
                outParam("P_RESULT", Types.NUMERIC)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_LOCK_NAME", lockName);
        inputParams.put("P_ID", id);

        Map<String, Object> result = executeProcedureWithOutputs("set_lock", inParams, outParams, inputParams);

        Integer lockStatus = toInteger(result.get("P_RESULT"));
        boolean success = lockStatus != null && lockStatus == 0;
        String officerName = (String) result.get("P_OFFICER_NAME");

        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("lockStatus", lockStatus);
        response.put("id", id);
        response.put("officerName", officerName);
        response.put("officerPhone", result.get("P_OFFICER_PHONE"));
        response.put("message", success ? "Lock acquired" :
                lockStatus == 1 ? "Locked by " + officerName :
                        lockStatus == 2 ? "Locked by another system" : "Error acquiring lock");
        return response;
    }

    @Override
    public Map<String, Object> setSeen(Long id) {
        logger.debug("Calling BOMessage.set_seen({})", id);

        List<SqlParameter> params = List.of(
                inParam("P_ID", Types.NUMERIC)
        );

        Map<String, Object> inputParams = Map.of("P_ID", id);

        executeVoidProcedure("set_seen", params, inputParams);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("messageId", id);
        response.put("message", "Message marked as seen");
        return response;
    }

    @Override
    public Map<String, Object> answer(Long id, Long wocId, Integer status, Long classId, String message) {
        logger.debug("Calling BOMessage.answer({}, {}, {}, {})", id, wocId, status, classId);

        List<SqlParameter> params = List.of(
                inParam("P_ID", Types.NUMERIC),
                inParam("P_WOC_ID", Types.NUMERIC),
                inParam("P_STATUS", Types.NUMERIC),
                inParam("P_CLASS_ID", Types.NUMERIC),
                inParam("P_MESSAGE", Types.VARCHAR)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_ID", id);
        inputParams.put("P_WOC_ID", wocId);
        inputParams.put("P_STATUS", status);
        inputParams.put("P_CLASS_ID", classId);
        inputParams.put("P_MESSAGE", message);

        executeVoidProcedure("answer", params, inputParams);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("messageId", id);
        response.put("message", "Answer sent successfully");
        return response;
    }

    @Override
    public Map<String, Object> forward(Long id, Long classId) {
        logger.debug("Calling BOMessage.forward({}, {})", id, classId);

        List<SqlParameter> params = List.of(
                inParam("P_ID", Types.NUMERIC),
                inParam("P_CLASS_ID", Types.NUMERIC)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_ID", id);
        inputParams.put("P_CLASS_ID", classId);

        executeVoidProcedure("forward", params, inputParams);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("messageId", id);
        response.put("newClassId", classId);
        response.put("message", "Message forwarded successfully");
        return response;
    }

    private Integer toInteger(Object value) {
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).intValue();
        return Integer.parseInt(value.toString());
    }

    private Long toLong(Object value) {
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).longValue();
        return Long.parseLong(value.toString());
    }
}

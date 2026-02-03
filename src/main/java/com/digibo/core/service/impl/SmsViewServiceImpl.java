package com.digibo.core.service.impl;

import com.digibo.core.service.SmsViewService;
import com.digibo.core.service.base.BaseService;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SmsViewServiceImpl - Real implementation of SmsViewService
 * Calls bosmsview Oracle package procedures
 */
@Service
@Profile("!mock")
public class SmsViewServiceImpl extends BaseService implements SmsViewService {

    public SmsViewServiceImpl() {
        super("bosmsview");
    }

    @Override
    public List<Map<String, Object>> getTypes() {
        logger.debug("Calling bosmsview.get_types()");

        return executeCursorProcedure("get_types", List.of(), Map.of(), "P_CURSOR",
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
    public List<Map<String, Object>> find(String custId, String custName, String userLogin, Long officerId,
                                           String pType, String mobile, String text, String statuses,
                                           Date createdFrom, Date createdTill) {
        logger.debug("Calling bosmsview.find({}, {}, {}, {}, {}, {}, {}, {}, {}, {})",
                custId, custName, userLogin, officerId, pType, mobile, text, statuses, createdFrom, createdTill);

        List<SqlParameter> params = List.of(
                inParam("P_CUST_ID", Types.VARCHAR),
                inParam("P_CUST_NAME", Types.VARCHAR),
                inParam("P_USER_LOGIN", Types.VARCHAR),
                inParam("P_OFFICER_ID", Types.NUMERIC),
                inParam("P_TYPE", Types.VARCHAR),
                inParam("P_MOBILE", Types.VARCHAR),
                inParam("P_TEXT", Types.VARCHAR),
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
        inputParams.put("P_MOBILE", mobile);
        inputParams.put("P_TEXT", text);
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
    public Map<String, Object> sms(String messageId) {
        logger.debug("Calling bosmsview.sms({})", messageId);

        List<SqlParameter> inParams = List.of(
                inParam("P_ID", Types.VARCHAR)
        );

        List<SqlOutParameter> outParams = List.of(
                outParam("P_TYPE_NAME", Types.VARCHAR),
                outParam("P_PRIORITY", Types.VARCHAR),
                outParam("P_CHANGE_DATE", Types.DATE),
                outParam("P_CHARGE_DATE", Types.DATE),
                outParam("P_TEXT", Types.VARCHAR),
                outParam("P_SRC_ADDR", Types.VARCHAR),
                outParam("P_SRC_PROVIDER", Types.VARCHAR),
                outParam("P_DEST_ADDR", Types.VARCHAR),
                outParam("P_DEST_PROVIDER", Types.VARCHAR),
                outParam("P_WOC_ID", Types.VARCHAR),
                outParam("P_USER_ID", Types.VARCHAR),
                outParam("P_USER_NAME", Types.VARCHAR),
                outParam("P_LOGIN", Types.VARCHAR),
                outParam("P_STMT_ID", Types.VARCHAR),
                outParam("P_BATCH_ID", Types.VARCHAR),
                outParam("P_ERROR_TYPE", Types.VARCHAR),
                outParam("P_ERROR_TEXT", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_ID", messageId);

        Map<String, Object> result = executeProcedureWithOutputs("sms", inParams, outParams, inputParams);

        Map<String, Object> smsMessage = new HashMap<>();
        smsMessage.put("id", messageId);
        smsMessage.put("typeName", result.get("P_TYPE_NAME"));
        smsMessage.put("priority", result.get("P_PRIORITY"));
        smsMessage.put("changeDate", result.get("P_CHANGE_DATE"));
        smsMessage.put("chargeDate", result.get("P_CHARGE_DATE"));
        smsMessage.put("text", result.get("P_TEXT"));
        smsMessage.put("srcAddr", result.get("P_SRC_ADDR"));
        smsMessage.put("srcProvider", result.get("P_SRC_PROVIDER"));
        smsMessage.put("destAddr", result.get("P_DEST_ADDR"));
        smsMessage.put("destProvider", result.get("P_DEST_PROVIDER"));
        smsMessage.put("wocId", result.get("P_WOC_ID"));
        smsMessage.put("userId", result.get("P_USER_ID"));
        smsMessage.put("userName", result.get("P_USER_NAME"));
        smsMessage.put("login", result.get("P_LOGIN"));
        smsMessage.put("stmtId", result.get("P_STMT_ID"));
        smsMessage.put("batchId", result.get("P_BATCH_ID"));
        smsMessage.put("errorType", result.get("P_ERROR_TYPE"));
        smsMessage.put("errorText", result.get("P_ERROR_TEXT"));

        return smsMessage;
    }
}

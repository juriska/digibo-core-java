package com.digibo.core.service.impl;

import com.digibo.core.service.SmsDocService;
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
 * SmsDocServiceImpl - Real implementation of SmsDocService
 * Calls BOSMSDocument Oracle package procedures
 */
@Service
@Profile("!mock")
public class SmsDocServiceImpl extends BaseService implements SmsDocService {

    public SmsDocServiceImpl() {
        super("BOSMSDocument");
    }

    @Override
    public List<Map<String, Object>> find(String custId, String custName, String userLogin, Long officerId,
                                           String pType, String mobile, String docId, String statuses,
                                           Date createdFrom, Date createdTill) {
        logger.debug("Calling BOSMSDocument.find({}, {}, {}, {}, {}, {}, {}, {}, {}, {})",
                custId, custName, userLogin, officerId, pType, mobile, docId, statuses, createdFrom, createdTill);

        List<SqlParameter> params = List.of(
                inParam("P_CUST_ID", Types.VARCHAR),
                inParam("P_CUST_NAME", Types.VARCHAR),
                inParam("P_USER_LOGIN", Types.VARCHAR),
                inParam("P_OFFICER_ID", Types.NUMERIC),
                inParam("P_TYPE", Types.VARCHAR),
                inParam("P_MOBILE", Types.VARCHAR),
                inParam("P_DOC_ID", Types.VARCHAR),
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
        inputParams.put("P_DOC_ID", docId);
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
    public Map<String, Object> sms(String docId) {
        logger.debug("Calling BOSMSDocument.sms({})", docId);

        List<SqlParameter> inParams = List.of(
                inParam("P_ID", Types.VARCHAR)
        );

        List<SqlOutParameter> outParams = List.of(
                outParam("P_USER_NAME", Types.VARCHAR),
                outParam("P_USER_ID", Types.VARCHAR),
                outParam("P_OFFICER_NAME", Types.VARCHAR),
                outParam("P_CUST_NAME", Types.VARCHAR),
                outParam("P_CUST_ACCOUNT", Types.VARCHAR),
                outParam("P_AGREEMENT", Types.VARCHAR),
                outParam("P_CONTRACT_ID", Types.VARCHAR),
                outParam("P_MOBILE_OPERATOR", Types.VARCHAR),
                outParam("P_CONTACT_LANGUAGE", Types.VARCHAR),
                outParam("P_LOCATION", Types.VARCHAR),
                outParam("P_ITC", Types.VARCHAR),
                outParam("P_SMS_TIME", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_ID", docId);

        Map<String, Object> result = executeProcedureWithOutputs("sms", inParams, outParams, inputParams);

        Map<String, Object> smsDoc = new HashMap<>();
        smsDoc.put("id", docId);
        smsDoc.put("userName", result.get("P_USER_NAME"));
        smsDoc.put("userId", result.get("P_USER_ID"));
        smsDoc.put("officerName", result.get("P_OFFICER_NAME"));
        smsDoc.put("custName", result.get("P_CUST_NAME"));
        smsDoc.put("custAccount", result.get("P_CUST_ACCOUNT"));
        smsDoc.put("agreement", result.get("P_AGREEMENT"));
        smsDoc.put("contractId", result.get("P_CONTRACT_ID"));
        smsDoc.put("mobileOperator", result.get("P_MOBILE_OPERATOR"));
        smsDoc.put("contactLanguage", result.get("P_CONTACT_LANGUAGE"));
        smsDoc.put("location", result.get("P_LOCATION"));
        smsDoc.put("itc", result.get("P_ITC"));
        smsDoc.put("smsTime", result.get("P_SMS_TIME"));

        return smsDoc;
    }

    @Override
    public int alreadyExists(String phone) {
        logger.debug("Calling BOSMSDocument.already_exists({})", phone);

        List<SqlParameter> params = List.of(
                inParam("P_PHONE", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_PHONE", phone);

        Integer result = executeScalarFunction("already_exists", params, inputParams, Types.INTEGER);
        return result != null ? result : 0;
    }

    @Override
    public void updateDocument(String docId, String reason, Integer newStatus, Long messageId) {
        logger.debug("Calling BOSMSDocument.update_document({}, {}, {}, {})", docId, reason, newStatus, messageId);

        List<SqlParameter> params = List.of(
                inParam("P_DOC_ID", Types.VARCHAR),
                inParam("P_REASON", Types.VARCHAR),
                inParam("P_NEW_STATUS", Types.NUMERIC),
                inParam("P_MESSAGE_ID", Types.NUMERIC)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_DOC_ID", docId);
        inputParams.put("P_REASON", reason);
        inputParams.put("P_NEW_STATUS", newStatus);
        inputParams.put("P_MESSAGE_ID", messageId);

        executeVoidProcedure("update_document", params, inputParams);
    }
}

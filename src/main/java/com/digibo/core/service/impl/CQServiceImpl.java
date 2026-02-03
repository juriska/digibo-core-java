package com.digibo.core.service.impl;

import com.digibo.core.service.CQService;
import com.digibo.core.service.base.BaseService;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.*;

/**
 * CQServiceImpl - Real implementation of CQService
 * Calls BOCQ Oracle package procedures
 */
@Service
@Profile("!mock")
public class CQServiceImpl extends BaseService implements CQService {

    public CQServiceImpl() {
        super("BOCQ");
    }

    @Override
    public List<Map<String, Object>> find(String custId, String custName, String userLogin,
                                           Long officerId, String docClass, String docId,
                                           String statuses, Date createdFrom, Date createdTill) {
        logger.debug("Calling BOCQ.find() with filters");

        List<SqlParameter> params = List.of(
                inParam("P_CUST_ID", Types.VARCHAR),
                inParam("P_CUST_NAME", Types.VARCHAR),
                inParam("P_USER_LOGIN", Types.VARCHAR),
                inParam("P_OFFICER_ID", Types.NUMERIC),
                inParam("P_DOC_CLASS", Types.VARCHAR),
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
        inputParams.put("P_DOC_CLASS", docClass);
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
    public List<Map<String, Object>> findMy(String docClass) {
        logger.debug("Calling BOCQ.find_my({})", docClass);

        List<SqlParameter> params = List.of(
                inParam("P_DOC_CLASS", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_DOC_CLASS", docClass != null ? docClass : "");

        return executeCursorProcedure("find_my", params, inputParams, "P_CURSOR",
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
    public Map<String, Object> cq(String docId) {
        logger.debug("Calling BOCQ.cq({})", docId);

        List<SqlParameter> inParams = List.of(
                inParam("P_ID", Types.VARCHAR)
        );

        List<SqlOutParameter> outParams = List.of(
                outParam("P_DOC_NO", Types.VARCHAR),
                outParam("P_USER_NAME", Types.VARCHAR),
                outParam("P_USER_ID", Types.VARCHAR),
                outParam("P_OFFICER_NAME", Types.VARCHAR),
                outParam("P_CUST_NAME", Types.VARCHAR),
                outParam("P_CUST_ACCOUNT", Types.VARCHAR),
                outParam("P_GLOBUS_NO", Types.VARCHAR),
                outParam("P_ITC", Types.VARCHAR),
                outParam("P_ITB", Types.VARCHAR),
                outParam("P_AUTH_NAME", Types.VARCHAR),
                outParam("P_AUTH_SURNAME", Types.VARCHAR),
                outParam("P_AUTH_LEGAL_ID", Types.VARCHAR),
                outParam("P_AUTH_PASSPORT_NO", Types.VARCHAR),
                outParam("P_AUTH_PASSPORT_COUNTRY", Types.VARCHAR),
                outParam("P_AUTH_PASSPORT_INST", Types.VARCHAR),
                outParam("P_AUTH_PHONE", Types.VARCHAR),
                outParam("P_AUTH_FAX", Types.VARCHAR),
                outParam("P_AUTH_EMAIL", Types.VARCHAR),
                outParam("P_CONTACT_PERSON_NAME", Types.VARCHAR),
                outParam("P_CONTACT_PERSON_SURNAME", Types.VARCHAR),
                outParam("P_CONTACT_PERSON_PHONE", Types.VARCHAR),
                outParam("P_CONTACT_PERSON_EMAIL", Types.VARCHAR),
                outParam("P_ECONIMIC_ACTIVITY", Types.VARCHAR),
                outParam("P_RECIPIENTS", Types.VARCHAR),
                outParam("P_SUPPLIERS", Types.VARCHAR),
                outParam("P_INCOMING_PAYMENTS", Types.VARCHAR),
                outParam("P_OUTGOING_PAYMENTS", Types.VARCHAR),
                outParam("P_FINANCE_CLIENTS", Types.VARCHAR),
                outParam("P_LOCATION", Types.VARCHAR),
                outParam("P_SIGN_TIME", Types.TIMESTAMP),
                outParam("P_SIGN_DEV_TYPE", Types.NUMERIC),
                outParam("P_SIGN_DEV_ID", Types.VARCHAR),
                outParam("P_SIGN_KEY1", Types.VARCHAR),
                outParam("P_SIGN_KEY2", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_ID", docId);

        Map<String, Object> result = executeProcedureWithOutputs("cq", inParams, outParams, inputParams);

        Map<String, Object> response = new HashMap<>();
        response.put("id", docId);
        response.put("docNo", result.get("P_DOC_NO"));
        response.put("userName", result.get("P_USER_NAME"));
        response.put("userId", result.get("P_USER_ID"));
        response.put("officerName", result.get("P_OFFICER_NAME"));
        response.put("custName", result.get("P_CUST_NAME"));
        response.put("custAccount", result.get("P_CUST_ACCOUNT"));
        response.put("globusNo", result.get("P_GLOBUS_NO"));
        response.put("infoToCustomer", result.get("P_ITC"));
        response.put("infoToBank", result.get("P_ITB"));
        response.put("authName", result.get("P_AUTH_NAME"));
        response.put("authSurname", result.get("P_AUTH_SURNAME"));
        response.put("authLegalId", result.get("P_AUTH_LEGAL_ID"));
        response.put("authPassportNo", result.get("P_AUTH_PASSPORT_NO"));
        response.put("authPassportCountry", result.get("P_AUTH_PASSPORT_COUNTRY"));
        response.put("authPassportInst", result.get("P_AUTH_PASSPORT_INST"));
        response.put("authPhone", result.get("P_AUTH_PHONE"));
        response.put("authFax", result.get("P_AUTH_FAX"));
        response.put("authEmail", result.get("P_AUTH_EMAIL"));
        response.put("contactPersonName", result.get("P_CONTACT_PERSON_NAME"));
        response.put("contactPersonSurname", result.get("P_CONTACT_PERSON_SURNAME"));
        response.put("contactPersonPhone", result.get("P_CONTACT_PERSON_PHONE"));
        response.put("contactPersonEmail", result.get("P_CONTACT_PERSON_EMAIL"));
        response.put("econimicActivity", result.get("P_ECONIMIC_ACTIVITY"));
        response.put("recipients", result.get("P_RECIPIENTS"));
        response.put("suppliers", result.get("P_SUPPLIERS"));
        response.put("incomingPayments", result.get("P_INCOMING_PAYMENTS"));
        response.put("outgoingPayments", result.get("P_OUTGOING_PAYMENTS"));
        response.put("financeClients", result.get("P_FINANCE_CLIENTS"));
        response.put("location", result.get("P_LOCATION"));
        response.put("signTime", result.get("P_SIGN_TIME"));
        response.put("signDevType", result.get("P_SIGN_DEV_TYPE"));
        response.put("signDevId", result.get("P_SIGN_DEV_ID"));
        response.put("signKey1", result.get("P_SIGN_KEY1"));
        response.put("signKey2", result.get("P_SIGN_KEY2"));

        return response;
    }

    @Override
    public List<Map<String, Object>> getExtensions(String docId) {
        logger.debug("Calling BOCQ.get_extensions({})", docId);

        List<SqlParameter> params = List.of(
                inParam("P_ID", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_ID", docId);

        return executeCursorProcedure("get_extensions", params, inputParams, "P_CURSOR",
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

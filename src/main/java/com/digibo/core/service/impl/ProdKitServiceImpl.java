package com.digibo.core.service.impl;

import com.digibo.core.service.ProdKitService;
import com.digibo.core.service.base.BaseService;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.*;

/**
 * ProdKitServiceImpl - Real implementation of ProdKitService
 * Calls BOProdKit Oracle package procedures
 */
@Service
@Profile("!mock")
public class ProdKitServiceImpl extends BaseService implements ProdKitService {

    public ProdKitServiceImpl() {
        super("BOProdKit");
    }

    @Override
    public List<Map<String, Object>> find(String custId, String custName, String userLogin,
                                           String docId, String statuses, String docClass,
                                           Date createdFrom, Date createdTill) {
        logger.debug("Calling BOProdKit.find()");

        List<SqlParameter> params = List.of(
                inParam("P_CUST_ID", Types.VARCHAR),
                inParam("P_CUST_NAME", Types.VARCHAR),
                inParam("P_USER_LOGIN", Types.VARCHAR),
                inParam("P_DOC_ID", Types.VARCHAR),
                inParam("P_STATUSES", Types.VARCHAR),
                inParam("P_DOC_CLASS", Types.VARCHAR),
                inParam("P_CREATED_FROM", Types.DATE),
                inParam("P_CREATED_TILL", Types.DATE)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_CUST_ID", custId);
        inputParams.put("P_CUST_NAME", custName);
        inputParams.put("P_USER_LOGIN", userLogin);
        inputParams.put("P_DOC_ID", docId);
        inputParams.put("P_STATUSES", statuses);
        inputParams.put("P_DOC_CLASS", docClass);
        inputParams.put("P_CREATED_FROM", createdFrom);
        inputParams.put("P_CREATED_TILL", createdTill);

        return executeCursorProcedure("find", params, inputParams, "P_CURSOR",
                (rs, rowNum) -> mapRow(rs));
    }

    @Override
    public List<Map<String, Object>> findMy(Integer officerId) {
        logger.debug("Calling BOProdKit.find_my({})", officerId);

        List<SqlParameter> params = List.of(
                inParam("P_OFFICER_ID", Types.NUMERIC)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_OFFICER_ID", officerId != null ? officerId : 0);

        return executeCursorProcedure("find_my", params, inputParams, "P_CURSOR",
                (rs, rowNum) -> mapRow(rs));
    }

    @Override
    public Map<String, Object> setProcessing(String docId) {
        logger.debug("Calling BOProdKit.set_processing({})", docId);

        List<SqlParameter> params = List.of(
                inParam("P_ID", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_ID", docId);

        Integer result = executeScalarFunction("set_processing", params, inputParams, Types.INTEGER);
        int changeOfficerId = result != null ? result : 0;
        boolean success = changeOfficerId > 0;

        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("documentId", docId);
        response.put("changeOfficerId", changeOfficerId);
        response.put("result", changeOfficerId);
        return response;
    }

    @Override
    public Map<String, Object> prodkit(String docId) {
        logger.debug("Calling BOProdKit.prodkit({})", docId);

        List<SqlParameter> inParams = List.of(
                inParam("P_ID", Types.VARCHAR)
        );

        List<SqlOutParameter> outParams = List.of(
                outParam("P_USER_NAME", Types.VARCHAR),
                outParam("P_LEGAL_ID", Types.VARCHAR),
                outParam("P_OFFICER_NAME", Types.VARCHAR),
                outParam("P_CUST_NAME", Types.VARCHAR),
                outParam("P_GLOBUS_NO", Types.VARCHAR),
                outParam("P_LOCATION", Types.VARCHAR),
                outParam("P_FROM_ACCOUNT", Types.VARCHAR),
                outParam("P_UT_PHONE_NUMBER", Types.VARCHAR),
                outParam("P_PHONE_MOBILE", Types.VARCHAR),
                outParam("P_AUTH_NAME", Types.VARCHAR),
                outParam("P_AUTH_SURNAME", Types.VARCHAR),
                outParam("P_AUTH_LEGAL_ID", Types.VARCHAR),
                outParam("P_AUTH_PASSPORT_NO", Types.VARCHAR),
                outParam("P_AUTH_PASSPORT_COUNTRY", Types.VARCHAR),
                outParam("P_AUTH_PASSPORT_INST", Types.VARCHAR),
                outParam("P_AUTH_PHONE", Types.VARCHAR),
                outParam("P_AUTH_FAX", Types.VARCHAR),
                outParam("P_AUTH_EMAIL", Types.VARCHAR),
                outParam("P_ITC", Types.VARCHAR),
                outParam("P_ITB", Types.VARCHAR),
                outParam("P_CHANNEL_ID", Types.NUMERIC),
                outParam("P_SIGN_TIME", Types.DATE),
                outParam("P_SIGN_DEV_TYPE", Types.NUMERIC),
                outParam("P_SIGN_DEV_ID", Types.VARCHAR),
                outParam("P_SIGN_KEY1", Types.VARCHAR),
                outParam("P_SIGN_KEY2", Types.VARCHAR),
                outParam("P_SIGN_RSA", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_ID", docId);

        Map<String, Object> outputs = executeProcedureWithOutputs("prodkit", inParams, outParams, inputParams);

        Map<String, Object> result = new HashMap<>();
        result.put("id", docId);
        result.put("userName", outputs.get("P_USER_NAME"));
        result.put("legalId", outputs.get("P_LEGAL_ID"));
        result.put("officerName", outputs.get("P_OFFICER_NAME"));
        result.put("custName", outputs.get("P_CUST_NAME"));
        result.put("globusNo", outputs.get("P_GLOBUS_NO"));
        result.put("location", outputs.get("P_LOCATION"));
        result.put("fromAccount", outputs.get("P_FROM_ACCOUNT"));
        result.put("utPhoneNumber", outputs.get("P_UT_PHONE_NUMBER"));
        result.put("phoneMobile", outputs.get("P_PHONE_MOBILE"));
        result.put("authName", outputs.get("P_AUTH_NAME"));
        result.put("authSurname", outputs.get("P_AUTH_SURNAME"));
        result.put("authLegalId", outputs.get("P_AUTH_LEGAL_ID"));
        result.put("authPassportNo", outputs.get("P_AUTH_PASSPORT_NO"));
        result.put("authPassportCountry", outputs.get("P_AUTH_PASSPORT_COUNTRY"));
        result.put("authPassportInst", outputs.get("P_AUTH_PASSPORT_INST"));
        result.put("authPhone", outputs.get("P_AUTH_PHONE"));
        result.put("authFax", outputs.get("P_AUTH_FAX"));
        result.put("authEmail", outputs.get("P_AUTH_EMAIL"));
        result.put("infoToCustomer", outputs.get("P_ITC"));
        result.put("infoToBank", outputs.get("P_ITB"));
        result.put("channelId", outputs.get("P_CHANNEL_ID"));
        result.put("signTime", outputs.get("P_SIGN_TIME"));
        result.put("signDevType", outputs.get("P_SIGN_DEV_TYPE"));
        result.put("signDevId", outputs.get("P_SIGN_DEV_ID"));
        result.put("signKey1", outputs.get("P_SIGN_KEY1"));
        result.put("signKey2", outputs.get("P_SIGN_KEY2"));
        result.put("signRSA", outputs.get("P_SIGN_RSA"));

        return result;
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

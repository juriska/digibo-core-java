package com.digibo.core.service.impl;

import com.digibo.core.service.CapfService;
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
 * CapfServiceImpl - Real implementation of CapfService
 * Calls BOCapf Oracle package procedures
 */
@Service
@Profile("!mock")
public class CapfServiceImpl extends BaseService implements CapfService {

    public CapfServiceImpl() {
        super("BOCapf");
    }

    @Override
    public List<Map<String, Object>> find(String custId, String custName, String userLogin,
                                          String docId, String statuses, String docClass,
                                          Date createdFrom, Date createdTill,
                                          String customerName, String legalId) {
        logger.debug("Calling BOCapf.find({}, {}, {}, {}, {}, {}, {}, {}, {}, {})",
                custId, custName, userLogin, docId, statuses, docClass,
                createdFrom, createdTill, customerName, legalId);

        List<SqlParameter> params = List.of(
                inParam("P_CUST_ID", Types.VARCHAR),
                inParam("P_CUST_NAME", Types.VARCHAR),
                inParam("P_USER_LOGIN", Types.VARCHAR),
                inParam("P_DOC_ID", Types.VARCHAR),
                inParam("P_STATUSES", Types.VARCHAR),
                inParam("P_DOC_CLASS", Types.VARCHAR),
                inParam("P_CREATED_FROM", Types.DATE),
                inParam("P_CREATED_TILL", Types.DATE),
                inParam("P_CUSTOMER_NAME", Types.VARCHAR),
                inParam("P_LEGAL_ID", Types.VARCHAR)
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
        inputParams.put("P_CUSTOMER_NAME", customerName);
        inputParams.put("P_LEGAL_ID", legalId);

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
    public List<Map<String, Object>> findMy(Long officerId) {
        logger.debug("Calling BOCapf.find_my({})", officerId);

        List<SqlParameter> params = List.of(
                inParam("P_OFFICER_ID", Types.NUMERIC)
        );

        Map<String, Object> inputParams = Map.of("P_OFFICER_ID", officerId != null ? officerId : 0L);

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
    public Map<String, Object> capforder(String orderId) {
        logger.debug("Calling BOCapf.capforder({})", orderId);

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
                outParam("P_SIGN_TIME", Types.TIMESTAMP),
                outParam("P_SIGN_DEV_TYPE", Types.NUMERIC),
                outParam("P_SIGN_DEV_ID", Types.VARCHAR),
                outParam("P_SIGN_KEY1", Types.VARCHAR),
                outParam("P_SIGN_KEY2", Types.VARCHAR),
                outParam("P_SIGN_RSA", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_ID", orderId);

        Map<String, Object> result = executeProcedureWithOutputs("capforder", inParams, outParams, inputParams);

        Map<String, Object> response = new HashMap<>();
        response.put("id", orderId);
        response.put("userName", result.get("P_USER_NAME"));
        response.put("legalId", result.get("P_LEGAL_ID"));
        response.put("officerName", result.get("P_OFFICER_NAME"));
        response.put("custName", result.get("P_CUST_NAME"));
        response.put("globusNo", result.get("P_GLOBUS_NO"));
        response.put("location", result.get("P_LOCATION"));
        response.put("fromAccount", result.get("P_FROM_ACCOUNT"));
        response.put("utPhoneNumber", result.get("P_UT_PHONE_NUMBER"));
        response.put("phoneMobile", result.get("P_PHONE_MOBILE"));
        response.put("authName", result.get("P_AUTH_NAME"));
        response.put("authSurname", result.get("P_AUTH_SURNAME"));
        response.put("authLegalId", result.get("P_AUTH_LEGAL_ID"));
        response.put("authPassportNo", result.get("P_AUTH_PASSPORT_NO"));
        response.put("authPassportCountry", result.get("P_AUTH_PASSPORT_COUNTRY"));
        response.put("authPassportInst", result.get("P_AUTH_PASSPORT_INST"));
        response.put("authPhone", result.get("P_AUTH_PHONE"));
        response.put("authFax", result.get("P_AUTH_FAX"));
        response.put("authEmail", result.get("P_AUTH_EMAIL"));
        response.put("infoToCustomer", result.get("P_ITC"));
        response.put("infoToBank", result.get("P_ITB"));
        response.put("channelId", result.get("P_CHANNEL_ID"));
        response.put("signTime", result.get("P_SIGN_TIME"));
        response.put("signDevType", result.get("P_SIGN_DEV_TYPE"));
        response.put("signDevId", result.get("P_SIGN_DEV_ID"));
        response.put("signKey1", result.get("P_SIGN_KEY1"));
        response.put("signKey2", result.get("P_SIGN_KEY2"));
        response.put("signRSA", result.get("P_SIGN_RSA"));

        return response;
    }

    @Override
    public int setProcessing(String orderId) {
        logger.debug("Calling BOCapf.set_processing({})", orderId);

        List<SqlParameter> params = List.of(
                inParam("P_ID", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_ID", orderId);

        Integer result = executeScalarFunction("set_processing", params, inputParams, Types.INTEGER);
        return result != null ? result : 0;
    }
}

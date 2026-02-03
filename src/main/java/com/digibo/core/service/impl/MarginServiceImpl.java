package com.digibo.core.service.impl;

import com.digibo.core.service.MarginService;
import com.digibo.core.service.base.BaseService;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.*;

/**
 * MarginServiceImpl - Real implementation of MarginService
 * Calls BOMargin Oracle package procedures
 */
@Service
@Profile("!mock")
public class MarginServiceImpl extends BaseService implements MarginService {

    public MarginServiceImpl() {
        super("BOMargin");
    }

    @Override
    public List<Map<String, Object>> find(String custId, String custName, String userLogin, String userPassword,
                                           String docClass, Double rateFrom, Double rateTill,
                                           String orderCCY, String contraryCCY,
                                           Date expiryFrom, Date expiryTill,
                                           String docId, String statuses,
                                           Date createdFrom, Date createdTill) {
        logger.debug("Calling BOMargin.find()");

        List<SqlParameter> params = List.of(
                inParam("P_CUST_ID", Types.VARCHAR),
                inParam("P_CUST_NAME", Types.VARCHAR),
                inParam("P_USER_LOGIN", Types.VARCHAR),
                inParam("P_USER_PASSWORD", Types.VARCHAR),
                inParam("P_DOC_CLASS", Types.VARCHAR),
                inParam("P_RATE_FROM", Types.NUMERIC),
                inParam("P_RATE_TILL", Types.NUMERIC),
                inParam("P_ORDER_CCY", Types.VARCHAR),
                inParam("P_CONTRARY_CCY", Types.VARCHAR),
                inParam("P_EXPIRY_FROM", Types.DATE),
                inParam("P_EXPIRY_TILL", Types.DATE),
                inParam("P_DOC_ID", Types.VARCHAR),
                inParam("P_STATUSES", Types.VARCHAR),
                inParam("P_CREATED_FROM", Types.DATE),
                inParam("P_CREATED_TILL", Types.DATE)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_CUST_ID", custId);
        inputParams.put("P_CUST_NAME", custName);
        inputParams.put("P_USER_LOGIN", userLogin);
        inputParams.put("P_USER_PASSWORD", userPassword);
        inputParams.put("P_DOC_CLASS", docClass);
        inputParams.put("P_RATE_FROM", rateFrom);
        inputParams.put("P_RATE_TILL", rateTill);
        inputParams.put("P_ORDER_CCY", orderCCY);
        inputParams.put("P_CONTRARY_CCY", contraryCCY);
        inputParams.put("P_EXPIRY_FROM", expiryFrom);
        inputParams.put("P_EXPIRY_TILL", expiryTill);
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
        logger.debug("Calling BOMargin.findMy({})", docClass);

        List<SqlParameter> params = List.of(
                inParam("P_DOC_CLASS", Types.VARCHAR)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_DOC_CLASS", docClass);

        return executeCursorProcedure("findMy", params, inputParams, "P_CURSOR",
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
    public Map<String, Object> margin(String orderId) {
        logger.debug("Calling BOMargin.margin({})", orderId);

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
                outParam("P_OPERATION", Types.VARCHAR),
                outParam("P_INVEST_VOLUME", Types.VARCHAR),
                outParam("P_IDENT_CODE", Types.VARCHAR),
                outParam("P_ORDER_TYPE", Types.VARCHAR),
                outParam("P_GOOD_TILL", Types.DATE),
                outParam("P_CREDIT_CCY", Types.VARCHAR),
                outParam("P_DEBIT_CCY", Types.VARCHAR),
                outParam("P_EXCHANGE_RATE", Types.NUMERIC),
                outParam("P_VALUE_DATE", Types.DATE),
                outParam("P_TEXT", Types.VARCHAR),
                outParam("P_LOCATION", Types.VARCHAR),
                outParam("P_ITC", Types.VARCHAR),
                outParam("P_ITB", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_ID", orderId);

        Map<String, Object> result = executeProcedureWithOutputs("margin", inParams, outParams, inputParams);

        Map<String, Object> response = new HashMap<>();
        response.put("id", orderId);
        response.put("docNo", result.get("P_DOC_NO"));
        response.put("userName", result.get("P_USER_NAME"));
        response.put("userId", result.get("P_USER_ID"));
        response.put("officerName", result.get("P_OFFICER_NAME"));
        response.put("custName", result.get("P_CUST_NAME"));
        response.put("custAccount", result.get("P_CUST_ACCOUNT"));
        response.put("globusNo", result.get("P_GLOBUS_NO"));
        response.put("operation", result.get("P_OPERATION"));
        response.put("investVolume", result.get("P_INVEST_VOLUME"));
        response.put("identCode", result.get("P_IDENT_CODE"));
        response.put("orderType", result.get("P_ORDER_TYPE"));
        response.put("goodTill", result.get("P_GOOD_TILL"));
        response.put("creditCCY", result.get("P_CREDIT_CCY"));
        response.put("debitCCY", result.get("P_DEBIT_CCY"));
        response.put("exchangeRate", result.get("P_EXCHANGE_RATE"));
        response.put("valueDate", result.get("P_VALUE_DATE"));
        response.put("text", result.get("P_TEXT"));
        response.put("location", result.get("P_LOCATION"));
        response.put("infoToCustomer", result.get("P_ITC"));
        response.put("infoToBank", result.get("P_ITB"));
        return response;
    }
}

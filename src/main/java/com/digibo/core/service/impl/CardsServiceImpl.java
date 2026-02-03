package com.digibo.core.service.impl;

import com.digibo.core.service.CardsService;
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
 * CardsServiceImpl - Real implementation of CardsService
 * Calls BOCards Oracle package procedures
 */
@Service
@Profile("!mock")
public class CardsServiceImpl extends BaseService implements CardsService {

    public CardsServiceImpl() {
        super("BOCards");
    }

    @Override
    public List<Map<String, Object>> find(String custId, String custName, String userLogin, Long officerId,
                                          String docClass, String fromLocation,
                                          String docId, String statuses, Date createdFrom, Date createdTill,
                                          String channels) {
        logger.debug("Calling BOCards.find({}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {})",
                custId, custName, userLogin, officerId, docClass, fromLocation,
                docId, statuses, createdFrom, createdTill, channels);

        List<SqlParameter> params = List.of(
                inParam("P_CUST_ID", Types.VARCHAR),
                inParam("P_CUST_NAME", Types.VARCHAR),
                inParam("P_USER_LOGIN", Types.VARCHAR),
                inParam("P_OFFICER_ID", Types.NUMERIC),
                inParam("P_DOC_CLASS", Types.VARCHAR),
                inParam("P_FROM_LOCATION", Types.VARCHAR),
                inParam("P_DOC_ID", Types.VARCHAR),
                inParam("P_STATUSES", Types.VARCHAR),
                inParam("P_CREATED_FROM", Types.DATE),
                inParam("P_CREATED_TILL", Types.DATE),
                inParam("P_CHANNELS", Types.VARCHAR)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_CUST_ID", custId);
        inputParams.put("P_CUST_NAME", custName);
        inputParams.put("P_USER_LOGIN", userLogin);
        inputParams.put("P_OFFICER_ID", officerId);
        inputParams.put("P_DOC_CLASS", docClass);
        inputParams.put("P_FROM_LOCATION", fromLocation);
        inputParams.put("P_DOC_ID", docId);
        inputParams.put("P_STATUSES", statuses);
        inputParams.put("P_CREATED_FROM", createdFrom);
        inputParams.put("P_CREATED_TILL", createdTill);
        inputParams.put("P_CHANNELS", channels);

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
    public List<Map<String, Object>> findMy(Long officerId, String docClass) {
        logger.debug("Calling BOCards.find_my({}, {})", officerId, docClass);

        List<SqlParameter> params = List.of(
                inParam("P_OFFICER_ID", Types.NUMERIC),
                inParam("P_DOC_CLASS", Types.VARCHAR)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_OFFICER_ID", officerId);
        inputParams.put("P_DOC_CLASS", docClass);

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
    public List<Map<String, Object>> getExtensions(String docId) {
        logger.debug("Calling BOCards.get_extensions({})", docId);

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

    @Override
    public int setProcessing(String docId, Integer statusIdFrom) {
        logger.debug("Calling BOCards.set_processing({}, {})", docId, statusIdFrom);

        List<SqlParameter> params = List.of(
                inParam("P_ID", Types.VARCHAR),
                inParam("P_STATUS_ID_FROM", Types.NUMERIC)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_ID", docId);
        inputParams.put("P_STATUS_ID_FROM", statusIdFrom);

        Integer result = executeScalarFunction("set_processing", params, inputParams, Types.INTEGER);
        return result != null ? result : -1;
    }

    @Override
    public Map<String, Object> card(String docId) {
        logger.debug("Calling BOCards.card({})", docId);

        List<SqlParameter> inParams = List.of(
                inParam("P_ID", Types.VARCHAR)
        );

        List<SqlOutParameter> outParams = List.of(
                outParam("P_USER_NAME", Types.VARCHAR),
                outParam("P_USER_ID", Types.VARCHAR),
                outParam("P_OFFICER_NAME", Types.VARCHAR),
                outParam("P_CUST_NAME", Types.VARCHAR),
                outParam("P_CUST_COUNTRY", Types.VARCHAR),
                outParam("P_CUST_ACCOUNT", Types.VARCHAR),
                outParam("P_GRP_ID", Types.VARCHAR),
                outParam("P_GRP_NAME", Types.VARCHAR),
                outParam("P_PROD_NAME", Types.VARCHAR),
                outParam("P_PROD_CCY", Types.VARCHAR),
                outParam("P_PAN", Types.VARCHAR),
                outParam("P_EMAIL", Types.VARCHAR),
                outParam("P_PHONE", Types.VARCHAR),
                outParam("P_MOBILE", Types.VARCHAR),
                outParam("P_CHARGES_ACSD_ID", Types.VARCHAR),
                outParam("P_INTEREST_IBAN", Types.VARCHAR),
                outParam("P_ISSUE_FOR_ACCOUNT", Types.VARCHAR),
                outParam("P_ISSUE_FOR_CUSTOMER", Types.VARCHAR),
                outParam("P_CARD_STAN", Types.VARCHAR),
                outParam("P_CARD_STATUS_FROM", Types.VARCHAR),
                outParam("P_CARD_STATUS_TO", Types.VARCHAR),
                outParam("P_CORTEX_STATUS", Types.NUMERIC),
                outParam("P_CORTEX_DETAILS", Types.VARCHAR),
                outParam("P_LOST_TYPE", Types.NUMERIC),
                outParam("P_LOST_DATE", Types.DATE),
                outParam("P_FF_TEXT", Types.VARCHAR),
                outParam("P_LOCATION", Types.VARCHAR),
                outParam("P_ITC", Types.VARCHAR),
                outParam("P_ITB", Types.VARCHAR),
                outParam("P_SIGN_TIME", Types.TIMESTAMP),
                outParam("P_SIGN_DEV_TYPE", Types.NUMERIC),
                outParam("P_SIGN_DEV_ID", Types.VARCHAR),
                outParam("P_SIGN_KEY1", Types.VARCHAR),
                outParam("P_SIGN_KEY2", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_ID", docId);

        Map<String, Object> result = executeProcedureWithOutputs("card", inParams, outParams, inputParams);

        Map<String, Object> response = new HashMap<>();
        response.put("id", docId);
        response.put("userName", result.get("P_USER_NAME"));
        response.put("userId", result.get("P_USER_ID"));
        response.put("officerName", result.get("P_OFFICER_NAME"));
        response.put("custName", result.get("P_CUST_NAME"));
        response.put("custCountry", result.get("P_CUST_COUNTRY"));
        response.put("custAccount", result.get("P_CUST_ACCOUNT"));
        response.put("grpId", result.get("P_GRP_ID"));
        response.put("grpName", result.get("P_GRP_NAME"));
        response.put("prodName", result.get("P_PROD_NAME"));
        response.put("prodCCY", result.get("P_PROD_CCY"));
        response.put("pan", result.get("P_PAN"));
        response.put("email", result.get("P_EMAIL"));
        response.put("phone", result.get("P_PHONE"));
        response.put("mobile", result.get("P_MOBILE"));
        response.put("chargesAcsdId", result.get("P_CHARGES_ACSD_ID"));
        response.put("interestIban", result.get("P_INTEREST_IBAN"));
        response.put("issueForAccount", result.get("P_ISSUE_FOR_ACCOUNT"));
        response.put("issueForCustomer", result.get("P_ISSUE_FOR_CUSTOMER"));
        response.put("cardStan", result.get("P_CARD_STAN"));
        response.put("cardStatusFrom", result.get("P_CARD_STATUS_FROM"));
        response.put("cardStatusTo", result.get("P_CARD_STATUS_TO"));
        response.put("cortexStatus", result.get("P_CORTEX_STATUS"));
        response.put("cortexDetails", result.get("P_CORTEX_DETAILS"));
        response.put("lostType", result.get("P_LOST_TYPE"));
        response.put("lostDate", result.get("P_LOST_DATE"));
        response.put("ffText", result.get("P_FF_TEXT"));
        response.put("location", result.get("P_LOCATION"));
        response.put("infoToCustomer", result.get("P_ITC"));
        response.put("infoToBank", result.get("P_ITB"));
        response.put("signTime", result.get("P_SIGN_TIME"));
        response.put("signDevType", result.get("P_SIGN_DEV_TYPE"));
        response.put("signDevId", result.get("P_SIGN_DEV_ID"));
        response.put("signKey1", result.get("P_SIGN_KEY1"));
        response.put("signKey2", result.get("P_SIGN_KEY2"));

        return response;
    }

    @Override
    public Map<String, Object> getLostAddr(String docId) {
        logger.debug("Calling BOCards.get_lost_addr({})", docId);

        List<SqlParameter> inParams = List.of(
                inParam("P_ID", Types.VARCHAR)
        );

        List<SqlOutParameter> outParams = List.of(
                outParam("P_LOST_COUNTRY", Types.VARCHAR),
                outParam("P_LOST_CITY", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_ID", docId);

        Map<String, Object> result = executeProcedureWithOutputs("get_lost_addr", inParams, outParams, inputParams);

        Map<String, Object> response = new HashMap<>();
        response.put("documentId", docId);
        response.put("lostCountry", result.get("P_LOST_COUNTRY"));
        response.put("lostCity", result.get("P_LOST_CITY"));

        return response;
    }

    @Override
    public Map<String, Object> getIssueAddr(String docId) {
        logger.debug("Calling BOCards.get_issue_addr({})", docId);

        List<SqlParameter> inParams = List.of(
                inParam("P_ID", Types.VARCHAR)
        );

        List<SqlOutParameter> outParams = List.of(
                outParam("P_RECEIVING_TYPE", Types.NUMERIC),
                outParam("P_OFFICE", Types.VARCHAR),
                outParam("P_COUNTRY", Types.VARCHAR),
                outParam("P_ADDRESS", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_ID", docId);

        Map<String, Object> result = executeProcedureWithOutputs("get_issue_addr", inParams, outParams, inputParams);

        Map<String, Object> response = new HashMap<>();
        response.put("documentId", docId);
        response.put("receivingType", result.get("P_RECEIVING_TYPE"));
        response.put("office", result.get("P_OFFICE"));
        response.put("country", result.get("P_COUNTRY"));
        response.put("address", result.get("P_ADDRESS"));

        return response;
    }
}

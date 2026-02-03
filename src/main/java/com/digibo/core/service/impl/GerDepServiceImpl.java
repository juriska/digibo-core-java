package com.digibo.core.service.impl;

import com.digibo.core.service.GerDepService;
import com.digibo.core.service.base.BaseService;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.*;

/**
 * GerDepServiceImpl - Real implementation of GerDepService
 * Calls BOGERDEP Oracle package procedures
 */
@Service
@Profile("!mock")
public class GerDepServiceImpl extends BaseService implements GerDepService {

    public GerDepServiceImpl() {
        super("BOGERDEP");
    }

    @Override
    public List<Map<String, Object>> findNew() {
        logger.debug("Calling BOGERDEP.find_new()");

        List<SqlParameter> params = List.of();
        Map<String, Object> inputParams = Map.of();

        return executeCursorProcedure("find_new", params, inputParams, "P_CURSOR",
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
    public List<Map<String, Object>> findByFilter(String docId, String custId, String custName,
                                                   String idDocNo, String login, String status,
                                                   Date orderDateFrom, Date orderDateTo) {
        logger.debug("Calling BOGERDEP.find_by_filter()");

        List<SqlParameter> params = List.of(
                inParam("P_DOC_ID", Types.VARCHAR),
                inParam("P_CUST_ID", Types.VARCHAR),
                inParam("P_CUST_NAME", Types.VARCHAR),
                inParam("P_ID_DOC_NO", Types.VARCHAR),
                inParam("P_LOGIN", Types.VARCHAR),
                inParam("P_STATUS", Types.VARCHAR),
                inParam("P_ORDER_DATE_FROM", Types.DATE),
                inParam("P_ORDER_DATE_TO", Types.DATE)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_DOC_ID", docId);
        inputParams.put("P_CUST_ID", custId);
        inputParams.put("P_CUST_NAME", custName);
        inputParams.put("P_ID_DOC_NO", idDocNo);
        inputParams.put("P_LOGIN", login);
        inputParams.put("P_STATUS", status);
        inputParams.put("P_ORDER_DATE_FROM", orderDateFrom);
        inputParams.put("P_ORDER_DATE_TO", orderDateTo);

        return executeCursorProcedure("find_by_filter", params, inputParams, "P_CURSOR",
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
    public Map<String, Object> selectCustomer(String custId) {
        logger.debug("Calling BOGERDEP.select_customer({})", custId);

        List<SqlParameter> inParams = List.of(
                inParam("P_CUST_ID", Types.VARCHAR)
        );

        List<SqlOutParameter> outParams = List.of(
                outParam("P_RV", Types.NUMERIC)
        );

        Map<String, Object> inputParams = Map.of("P_CUST_ID", custId);

        CursorResult<Map<String, Object>> result = executeCursorProcedureWithOutputs(
                "select_customer", inParams, outParams, inputParams, "P_CURSOR",
                (rs, rowNum) -> {
                    Map<String, Object> row = new HashMap<>();
                    int colCount = rs.getMetaData().getColumnCount();
                    for (int i = 1; i <= colCount; i++) {
                        String colName = rs.getMetaData().getColumnName(i);
                        row.put(colName, rs.getObject(i));
                    }
                    return row;
                });

        Map<String, Object> response = new HashMap<>();
        response.put("rv", toInteger(result.getOutputs().get("P_RV")));
        response.put("customers", result.getRows());
        return response;
    }

    @Override
    public Map<String, Object> bindToCustomer(String docId, String custId) {
        logger.debug("Calling BOGERDEP.bind_to_customer({}, {})", docId, custId);

        List<SqlParameter> params = List.of(
                inParam("P_DOC_ID", Types.VARCHAR),
                inParam("P_CUST_ID", Types.VARCHAR)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_DOC_ID", docId);
        inputParams.put("P_CUST_ID", custId);

        executeVoidProcedure("bind_to_customer", params, inputParams);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("documentId", docId);
        response.put("customerId", custId);
        return response;
    }

    @Override
    public Map<String, Object> accountExists(String docId) {
        logger.debug("Calling BOGERDEP.account_exists({})", docId);

        List<SqlParameter> inParams = List.of(
                inParam("P_DOC_ID", Types.VARCHAR)
        );

        List<SqlOutParameter> outParams = List.of(
                outParam("P_ACC_EXISTS", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_DOC_ID", docId);

        Map<String, Object> result = executeProcedureWithOutputs("account_exists", inParams, outParams, inputParams);

        String accExists = (String) result.get("P_ACC_EXISTS");

        Map<String, Object> response = new HashMap<>();
        response.put("documentId", docId);
        response.put("accountExists", accExists);
        response.put("exists", "Y".equals(accExists) || "1".equals(accExists));
        return response;
    }

    @Override
    public Map<String, Object> createUser(String docId, String tanCardId) {
        logger.debug("Calling BOGERDEP.create_user({}, {})", docId, tanCardId);

        List<SqlParameter> params = List.of(
                inParam("P_DOC_ID", Types.VARCHAR),
                inParam("P_TAN_CARD_ID", Types.VARCHAR)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_DOC_ID", docId);
        inputParams.put("P_TAN_CARD_ID", tanCardId);

        executeVoidProcedure("create_user", params, inputParams);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("documentId", docId);
        response.put("tanCardId", tanCardId);
        return response;
    }

    @Override
    public Map<String, Object> reject(String docId, String reason) {
        logger.debug("Calling BOGERDEP.reject({}, {})", docId, reason);

        List<SqlParameter> params = List.of(
                inParam("P_DOC_ID", Types.VARCHAR),
                inParam("P_REASON", Types.VARCHAR)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_DOC_ID", docId);
        inputParams.put("P_REASON", reason);

        executeVoidProcedure("reject", params, inputParams);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("documentId", docId);
        response.put("reason", reason);
        return response;
    }

    private Integer toInteger(Object value) {
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).intValue();
        return Integer.parseInt(value.toString());
    }
}

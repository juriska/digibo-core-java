package com.digibo.core.service.impl;

import com.digibo.core.service.FFOService;
import com.digibo.core.service.base.BaseService;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.*;

/**
 * FFOServiceImpl - Real implementation of FFOService
 * Calls BOFFO Oracle package procedures
 */
@Service
@Profile("!mock")
public class FFOServiceImpl extends BaseService implements FFOService {

    public FFOServiceImpl() {
        super("BOFFO");
    }

    @Override
    public List<Map<String, Object>> findMy() {
        logger.debug("Calling BOFFO.find_my()");

        List<SqlParameter> params = List.of();
        Map<String, Object> inputParams = Map.of();

        List<Map<String, Object>> rows = executeCursorProcedure("find_my", params, inputParams, "P_CURSOR",
                (rs, rowNum) -> {
                    Map<String, Object> row = new HashMap<>();
                    int colCount = rs.getMetaData().getColumnCount();
                    for (int i = 1; i <= colCount; i++) {
                        String colName = rs.getMetaData().getColumnName(i);
                        row.put(colName, rs.getObject(i));
                    }
                    return row;
                });

        logger.debug("find_my returned {} rows", rows.size());
        return rows;
    }

    @Override
    public Map<String, Object> getById(String documentId) {
        logger.debug("Getting FFO document by ID: {}", documentId);

        // Use find_my and filter in Java (matching Node.js behavior)
        List<Map<String, Object>> rows = findMy();
        return rows.stream()
                .filter(d -> documentId.equals(String.valueOf(d.get("ID"))))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Map<String, Object>> getCategories() {
        logger.debug("Calling BOFFO.get_categories()");

        List<SqlParameter> params = List.of();
        Map<String, Object> inputParams = Map.of();

        List<Map<String, Object>> rows = executeCursorProcedure("get_categories", params, inputParams, "P_CURSOR",
                (rs, rowNum) -> {
                    Map<String, Object> row = new HashMap<>();
                    int colCount = rs.getMetaData().getColumnCount();
                    for (int i = 1; i <= colCount; i++) {
                        String colName = rs.getMetaData().getColumnName(i);
                        row.put(colName, rs.getObject(i));
                    }
                    return row;
                });

        logger.debug("get_categories returned {} rows", rows.size());
        return rows;
    }

    @Override
    public Map<String, Object> categorize(Long docId, Long categoryId, Long subCategoryId, Long assignee) {
        logger.debug("Categorizing document {}", docId);

        List<SqlParameter> params = List.of(
                inParam("P_DOC_ID", Types.NUMERIC),
                inParam("P_CATEGORY_ID", Types.NUMERIC),
                inParam("P_SUBCATEGORY_ID", Types.NUMERIC),
                inParam("P_ASSIGNEE", Types.NUMERIC)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_DOC_ID", docId);
        inputParams.put("P_CATEGORY_ID", categoryId);
        inputParams.put("P_SUBCATEGORY_ID", subCategoryId);
        inputParams.put("P_ASSIGNEE", assignee);

        Integer resultCode = executeScalarFunction("categorize", params, inputParams, Types.INTEGER);

        Map<String, Object> result = new HashMap<>();
        result.put("success", resultCode != null && resultCode == 0);
        result.put("documentId", docId);
        result.put("categoryId", categoryId);
        result.put("subCategoryId", subCategoryId);
        result.put("assignee", assignee);
        result.put("result", resultCode);
        return result;
    }

    @Override
    public List<Map<String, Object>> find(
            String custId,
            String custName,
            String userLogin,
            Long officerId,
            String docClass,
            String subject,
            String text,
            String docId,
            String channels,
            String statuses,
            Date createdFrom,
            Date createdTill,
            Long assignee,
            Long categoryId,
            Long subcategoryId
    ) {
        logger.debug("Calling BOFFO.find() with filters");

        List<SqlParameter> params = List.of(
                inParam("P_CUST_ID", Types.VARCHAR),
                inParam("P_CUST_NAME", Types.VARCHAR),
                inParam("P_USER_LOGIN", Types.VARCHAR),
                inParam("P_OFFICER_ID", Types.NUMERIC),
                inParam("P_DOC_CLASS", Types.VARCHAR),
                inParam("P_SUBJECT", Types.VARCHAR),
                inParam("P_TEXT", Types.VARCHAR),
                inParam("P_DOC_ID", Types.VARCHAR),
                inParam("P_CHANNELS", Types.VARCHAR),
                inParam("P_STATUSES", Types.VARCHAR),
                inParam("P_CREATED_FROM", Types.DATE),
                inParam("P_CREATED_TILL", Types.DATE),
                inParam("P_ASSIGNEE", Types.NUMERIC),
                inParam("P_CATEGORY_ID", Types.NUMERIC),
                inParam("P_SUBCATEGORY_ID", Types.NUMERIC)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_CUST_ID", custId);
        inputParams.put("P_CUST_NAME", custName);
        inputParams.put("P_USER_LOGIN", userLogin);
        inputParams.put("P_OFFICER_ID", officerId);
        inputParams.put("P_DOC_CLASS", docClass);
        inputParams.put("P_SUBJECT", subject);
        inputParams.put("P_TEXT", text);
        inputParams.put("P_DOC_ID", docId);
        inputParams.put("P_CHANNELS", channels);
        inputParams.put("P_STATUSES", statuses);
        inputParams.put("P_CREATED_FROM", createdFrom);
        inputParams.put("P_CREATED_TILL", createdTill);
        inputParams.put("P_ASSIGNEE", assignee);
        inputParams.put("P_CATEGORY_ID", categoryId);
        inputParams.put("P_SUBCATEGORY_ID", subcategoryId);

        List<Map<String, Object>> rows = executeCursorProcedure("find", params, inputParams, "P_CURSOR",
                (rs, rowNum) -> {
                    Map<String, Object> row = new HashMap<>();
                    int colCount = rs.getMetaData().getColumnCount();
                    for (int i = 1; i <= colCount; i++) {
                        String colName = rs.getMetaData().getColumnName(i);
                        row.put(colName, rs.getObject(i));
                    }
                    return row;
                });

        logger.debug("find returned {} rows", rows.size());
        return rows;
    }

    @Override
    public Map<String, Object> ffo(String docId) {
        logger.debug("Calling BOFFO.ffo({})", docId);

        List<SqlParameter> inParams = List.of(
                inParam("P_ID", Types.VARCHAR)
        );

        List<SqlOutParameter> outParams = List.of(
                outParam("P_USER_NAME", Types.VARCHAR),
                outParam("P_USER_ID", Types.VARCHAR),
                outParam("P_OFFICER_NAME", Types.VARCHAR),
                outParam("P_GOLD_MANAGER", Types.VARCHAR),
                outParam("P_CUST_NAME", Types.VARCHAR),
                outParam("P_CUST_ACCOUNT", Types.VARCHAR),
                outParam("P_GLOBUS_NO", Types.VARCHAR),
                outParam("P_LOCATION", Types.VARCHAR),
                outParam("P_F_TEXT", Types.VARCHAR),
                outParam("P_ITC", Types.VARCHAR),
                outParam("P_ITB", Types.VARCHAR),
                outParam("P_SIGN_TIME", Types.DATE),
                outParam("P_SIGN_RSA", Types.VARCHAR),
                outParam("P_SECTOR", Types.NUMERIC),
                outParam("P_SEGMENT", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_ID", docId);

        Map<String, Object> outputs = executeProcedureWithOutputs("ffo", inParams, outParams, inputParams);

        Map<String, Object> result = new HashMap<>();
        result.put("id", docId);
        result.put("userName", outputs.get("P_USER_NAME"));
        result.put("userId", outputs.get("P_USER_ID"));
        result.put("officerName", outputs.get("P_OFFICER_NAME"));
        result.put("goldManager", outputs.get("P_GOLD_MANAGER"));
        result.put("custName", outputs.get("P_CUST_NAME"));
        result.put("custAccount", outputs.get("P_CUST_ACCOUNT"));
        result.put("globusNo", outputs.get("P_GLOBUS_NO"));
        result.put("location", outputs.get("P_LOCATION"));
        result.put("fText", outputs.get("P_F_TEXT"));
        result.put("infoToCustomer", outputs.get("P_ITC"));
        result.put("infoToBank", outputs.get("P_ITB"));
        result.put("signTime", outputs.get("P_SIGN_TIME"));
        result.put("signRSA", outputs.get("P_SIGN_RSA"));
        result.put("sector", toInteger(outputs.get("P_SECTOR")));
        result.put("segment", outputs.get("P_SEGMENT"));
        return result;
    }

    @Override
    public Map<String, Object> setProcessing(String docId, String reason, Integer newStatus, Long messageId) {
        logger.debug("Setting processing status for document {}", docId);

        List<SqlParameter> params = List.of(
                inParam("P_ID", Types.VARCHAR),
                inParam("P_REASON", Types.VARCHAR),
                inParam("P_NEW_STATUS", Types.NUMERIC),
                inParam("P_MESSAGE_ID", Types.NUMERIC)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_ID", docId);
        inputParams.put("P_REASON", reason);
        inputParams.put("P_NEW_STATUS", newStatus);
        inputParams.put("P_MESSAGE_ID", messageId);

        Integer resultCode = executeScalarFunction("set_processing", params, inputParams, Types.INTEGER);

        Map<String, Object> result = new HashMap<>();
        result.put("success", resultCode != null && resultCode == 0);
        result.put("documentId", docId);
        result.put("newStatus", newStatus);
        result.put("result", resultCode);
        return result;
    }

    private Integer toInteger(Object value) {
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).intValue();
        return Integer.parseInt(value.toString());
    }
}

package com.digibo.core.service.impl;

import com.digibo.core.service.DocumentsService;
import com.digibo.core.service.base.BaseService;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.*;

/**
 * DocumentsServiceImpl - Real implementation of DocumentsService
 * Calls BODocuments Oracle package procedures
 */
@Service
@Profile("!mock")
public class DocumentsServiceImpl extends BaseService implements DocumentsService {

    public DocumentsServiceImpl() {
        super("BODocuments");
    }

    @Override
    public List<Map<String, Object>> getHistory(String documentId) {
        logger.debug("Calling BODocuments.history({})", documentId);

        List<SqlParameter> params = List.of(
                inParam("P_ID", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_ID", documentId);

        return executeCursorProcedure("history", params, inputParams, "P_CURSOR",
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
    public List<Map<String, Object>> getMessageHistory(String documentId) {
        logger.debug("Calling BODocuments.messageHistory({})", documentId);

        List<SqlParameter> params = List.of(
                inParam("P_ID", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_ID", documentId);

        return executeCursorProcedure("messageHistory", params, inputParams, "P_CURSOR",
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
    public Map<String, Object> setLock(String documentId) {
        logger.debug("Calling BODocuments.set_lock({})", documentId);

        List<SqlParameter> inParams = List.of(
                inParam("P_ID", Types.VARCHAR)
        );

        List<SqlOutParameter> outParams = List.of(
                outParam("P_STATUS", Types.NUMERIC),
                outParam("P_OFFICER_NAME", Types.VARCHAR),
                outParam("P_OFFICER_PHONE", Types.VARCHAR),
                outParam("P_RESULT", Types.NUMERIC)
        );

        Map<String, Object> inputParams = Map.of("P_ID", documentId);

        Map<String, Object> result = executeProcedureWithOutputs("set_lock", inParams, outParams, inputParams);

        Integer resultCode = toInteger(result.get("P_RESULT"));
        boolean lockAcquired = resultCode != null && resultCode == 0;

        Map<String, Object> response = new HashMap<>();
        response.put("lockAcquired", lockAcquired);
        response.put("status", result.get("P_STATUS"));

        if (!lockAcquired && resultCode != null && resultCode == 1) {
            Map<String, Object> lockedBy = new HashMap<>();
            lockedBy.put("name", result.get("P_OFFICER_NAME"));
            lockedBy.put("phone", result.get("P_OFFICER_PHONE"));
            response.put("lockedBy", lockedBy);
        } else {
            response.put("lockedBy", null);
        }

        return response;
    }

    @Override
    public Map<String, Object> setManualStatus(String documentId, String reason,
                                                Integer newStatus, Integer messageId) {
        logger.debug("Calling BODocuments.set_manual_status({}, {}, {}, {})",
                documentId, reason, newStatus, messageId);

        List<SqlParameter> params = List.of(
                inParam("P_ID", Types.VARCHAR),
                inParam("P_REASON", Types.VARCHAR),
                inParam("P_NEW_STATUS", Types.NUMERIC),
                inParam("P_MESSAGE_ID", Types.NUMERIC)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_ID", documentId);
        inputParams.put("P_REASON", reason);
        inputParams.put("P_NEW_STATUS", newStatus);
        inputParams.put("P_MESSAGE_ID", messageId);

        executeVoidProcedure("set_manual_status", params, inputParams);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("documentId", documentId);
        response.put("newStatus", newStatus);

        return response;
    }

    @Override
    public Map<String, Object> setManualStatusWithRef(String documentId, String reason,
                                                       Integer newStatus, Integer messageId,
                                                       String bankReference) {
        logger.debug("Calling BODocuments.set_manual_status_1({}, {}, {}, {}, {})",
                documentId, reason, newStatus, messageId, bankReference);

        List<SqlParameter> params = List.of(
                inParam("P_ID", Types.VARCHAR),
                inParam("P_REASON", Types.VARCHAR),
                inParam("P_NEW_STATUS", Types.NUMERIC),
                inParam("P_MESSAGE_ID", Types.NUMERIC),
                inParam("P_BANK_REF", Types.VARCHAR)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_ID", documentId);
        inputParams.put("P_REASON", reason);
        inputParams.put("P_NEW_STATUS", newStatus);
        inputParams.put("P_MESSAGE_ID", messageId);
        inputParams.put("P_BANK_REF", bankReference);

        executeVoidProcedure("set_manual_status_1", params, inputParams);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("documentId", documentId);
        response.put("newStatus", newStatus);
        response.put("bankReference", bankReference);

        return response;
    }

    @Override
    public Map<String, Object> getSignOwner(String certId, Date signDate) {
        logger.debug("Calling BODocuments.signOwner({}, {})", certId, signDate);

        List<SqlParameter> inParams = List.of(
                inParam("P_CERT_ID", Types.VARCHAR),
                inParam("P_SIGN_DATE", Types.DATE)
        );

        List<SqlOutParameter> outParams = List.of(
                outParam("P_USER_NAME", Types.VARCHAR),
                outParam("P_LEGAL_ID", Types.VARCHAR)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_CERT_ID", certId);
        inputParams.put("P_SIGN_DATE", signDate);

        Map<String, Object> result = executeProcedureWithOutputs("signOwner", inParams, outParams, inputParams);

        Map<String, Object> response = new HashMap<>();
        response.put("userName", result.get("P_USER_NAME"));
        response.put("legalId", result.get("P_LEGAL_ID"));
        response.put("certificateId", certId);
        response.put("signatureDate", signDate);

        return response;
    }

    @Override
    public List<Map<String, Object>> getAddresses(String documentId) {
        logger.debug("Calling BODocuments.get_addr({})", documentId);

        List<SqlParameter> params = List.of(
                inParam("P_ID", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_ID", documentId);

        return executeCursorProcedure("get_addr", params, inputParams, "P_CURSOR",
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
    public List<Map<String, Object>> getExtensions(String documentId) {
        logger.debug("Calling BODocuments.get_extensions({})", documentId);

        List<SqlParameter> params = List.of(
                inParam("P_ID", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_ID", documentId);

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
    public List<Map<String, Object>> getIBSignatures(String documentId) {
        logger.debug("Calling BODocuments.get_ib_signatures({})", documentId);

        List<SqlParameter> params = List.of(
                inParam("P_DOC_ID", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_DOC_ID", documentId);

        return executeCursorProcedure("get_ib_signatures", params, inputParams, "P_CURSOR",
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
    public Map<String, Object> setManualProcessing(String documentId) {
        logger.debug("Calling BODocuments.set_ManualProcessing({})", documentId);

        List<SqlParameter> params = List.of(
                inParam("P_ID", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_ID", documentId);

        Integer result = executeScalarFunction("set_ManualProcessing", params, inputParams, Types.INTEGER);

        Map<String, Object> response = new HashMap<>();
        response.put("success", result != null && result == 0);
        response.put("documentId", documentId);
        response.put("manualProcessingEnabled", true);

        return response;
    }

    @Override
    public Map<String, Object> getChangeOfficerId(String documentId) {
        logger.debug("Calling BODocuments.getChangeOfficerId({})", documentId);

        List<SqlParameter> params = List.of(
                inParam("P_ID", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_ID", documentId);

        Integer result = executeScalarFunction("getChangeOfficerId", params, inputParams, Types.INTEGER);

        Map<String, Object> response = new HashMap<>();
        response.put("documentId", documentId);
        response.put("changeOfficerId", result);

        return response;
    }

    @Override
    public Map<String, Object> getById(Integer documentId) {
        logger.debug("Calling BODocuments.get_by_id({})", documentId);

        List<SqlParameter> inParams = List.of(
                inParam("P_ID", Types.NUMERIC)
        );

        List<SqlOutParameter> outParams = List.of(
                outParam("P_STATUS", Types.NUMERIC),
                outParam("P_OFFICER_ID", Types.NUMERIC),
                outParam("P_ITC", Types.VARCHAR),
                outParam("P_RESULT", Types.NUMERIC)
        );

        Map<String, Object> inputParams = Map.of("P_ID", documentId);

        Map<String, Object> result = executeProcedureWithOutputs("get_by_id", inParams, outParams, inputParams);

        Integer resultCode = toInteger(result.get("P_RESULT"));

        Map<String, Object> response = new HashMap<>();
        response.put("id", documentId);
        response.put("status", result.get("P_STATUS"));
        response.put("officerId", result.get("P_OFFICER_ID"));
        response.put("infoToCustomer", result.get("P_ITC"));
        response.put("found", resultCode != null && resultCode == 0);

        return response;
    }

    private Integer toInteger(Object value) {
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).intValue();
        return Integer.parseInt(value.toString());
    }
}

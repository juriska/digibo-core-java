package com.digibo.core.service.impl;

import com.digibo.core.service.FaxViewService;
import com.digibo.core.service.base.BaseService;
import oracle.jdbc.OracleTypes;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.*;

/**
 * FaxViewServiceImpl - Real implementation of FaxViewService
 * Calls BOFaxView Oracle package procedures
 */
@Service
@Profile("!mock")
public class FaxViewServiceImpl extends BaseService implements FaxViewService {

    public FaxViewServiceImpl() {
        super("BOFaxView");
    }

    @Override
    public List<Map<String, Object>> findMyDocuments(String classes) {
        logger.debug("Calling BOFaxView.find_my_documents({})", classes);

        List<SqlParameter> params = List.of(
                inParam("P_CLASSES", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_CLASSES", classes);

        List<Map<String, Object>> rows = executeCursorProcedure("find_my_documents", params, inputParams, "P_CURSOR",
                (rs, rowNum) -> {
                    Map<String, Object> row = new HashMap<>();
                    int colCount = rs.getMetaData().getColumnCount();
                    for (int i = 1; i <= colCount; i++) {
                        String colName = rs.getMetaData().getColumnName(i);
                        row.put(colName, rs.getObject(i));
                    }
                    return row;
                });

        logger.debug("find_my_documents returned {} rows", rows.size());
        return rows;
    }

    @Override
    public Map<String, Object> setLock(String id, Integer doc) {
        logger.debug("Calling BOFaxView.set_lock({}, {})", id, doc);

        List<SqlParameter> inParams = List.of(
                inParam("P_ID", Types.VARCHAR),
                inParam("P_DOC", Types.NUMERIC)
        );

        List<SqlOutParameter> outParams = List.of(
                outParam("P_OFFICER_NAME", Types.VARCHAR),
                outParam("P_OFFICER_PHONE", Types.VARCHAR),
                outParam("P_RESULT", Types.NUMERIC)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_ID", id);
        inputParams.put("P_DOC", doc);

        Map<String, Object> outputs = executeProcedureWithOutputs("set_lock", inParams, outParams, inputParams);

        Map<String, Object> result = new HashMap<>();
        result.put("lockStatus", toInteger(outputs.get("P_RESULT")));
        result.put("id", id);
        result.put("officerName", outputs.get("P_OFFICER_NAME"));
        result.put("officerPhone", outputs.get("P_OFFICER_PHONE"));
        return result;
    }

    @Override
    public List<Map<String, Object>> loadHistory(String id) {
        logger.debug("Calling BOFaxView.load_history({})", id);

        List<SqlParameter> params = List.of(
                inParam("P_ID", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_ID", id);

        List<Map<String, Object>> rows = executeCursorProcedure("load_history", params, inputParams, "P_CURSOR",
                (rs, rowNum) -> {
                    Map<String, Object> row = new HashMap<>();
                    int colCount = rs.getMetaData().getColumnCount();
                    for (int i = 1; i <= colCount; i++) {
                        String colName = rs.getMetaData().getColumnName(i);
                        row.put(colName, rs.getObject(i));
                    }
                    return row;
                });

        logger.debug("load_history returned {} rows", rows.size());
        return rows;
    }

    @Override
    public List<Map<String, Object>> loadActual(String id) {
        logger.debug("Calling BOFaxView.load_actual({})", id);

        List<SqlParameter> params = List.of(
                inParam("P_ID", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_ID", id);

        List<Map<String, Object>> rows = executeCursorProcedure("load_actual", params, inputParams, "P_CURSOR",
                (rs, rowNum) -> {
                    Map<String, Object> row = new HashMap<>();
                    int colCount = rs.getMetaData().getColumnCount();
                    for (int i = 1; i <= colCount; i++) {
                        String colName = rs.getMetaData().getColumnName(i);
                        row.put(colName, rs.getObject(i));
                    }
                    return row;
                });

        logger.debug("load_actual returned {} rows", rows.size());
        return rows;
    }

    @Override
    public Long lastOfficer(Long custId, String fromAccount, Integer classId, String officers) {
        logger.debug("Calling BOFaxView.last_officer({}, {}, {}, {})", custId, fromAccount, classId, officers);

        List<SqlParameter> params = List.of(
                inParam("P_CUST_ID", Types.NUMERIC),
                inParam("P_FROM_ACCOUNT", Types.VARCHAR),
                inParam("P_CLASS_ID", Types.NUMERIC),
                inParam("P_OFFICERS", Types.VARCHAR)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_CUST_ID", custId);
        inputParams.put("P_FROM_ACCOUNT", fromAccount);
        inputParams.put("P_CLASS_ID", classId != null ? classId : 0);
        inputParams.put("P_OFFICERS", officers);

        Long result = executeScalarFunction("last_officer", params, inputParams, Types.NUMERIC);
        return result;
    }

    @Override
    public Long nextFaxId() {
        logger.debug("Calling BOFaxView.next_fax_id()");

        List<SqlParameter> params = List.of();
        Map<String, Object> inputParams = Map.of();

        Long result = executeScalarFunction("next_fax_id", params, inputParams, Types.NUMERIC);
        return result;
    }

    @Override
    public Map<String, Object> nextDocumentId(Long docId, String classes) {
        logger.debug("Calling BOFaxView.next_document_id({}, {})", docId, classes);

        List<SqlParameter> inParams = List.of(
                inParam("P_DOC_ID", Types.NUMERIC),
                inParam("P_CLASSES", Types.VARCHAR)
        );

        List<SqlOutParameter> outParams = List.of(
                outParam("P_DOC_ID_OUT", Types.NUMERIC),
                outParam("P_RESULT", Types.NUMERIC)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_DOC_ID", docId);
        inputParams.put("P_CLASSES", classes);

        Map<String, Object> outputs = executeProcedureWithOutputs("next_document_id", inParams, outParams, inputParams);

        Map<String, Object> result = new HashMap<>();
        result.put("faxId", toLong(outputs.get("P_RESULT")));
        result.put("nextDocId", toLong(outputs.get("P_DOC_ID_OUT")));
        return result;
    }

    @Override
    public Map<String, Object> loadFax(String id, String docId) {
        logger.debug("Calling BOFaxView.load_fax({}, {})", id, docId);

        List<SqlParameter> inParams = List.of(
                inParam("P_ID", Types.VARCHAR),
                inParam("P_DOC_ID", Types.VARCHAR)
        );

        List<SqlOutParameter> outParams = List.of(
                outParam("P_ID_OUT", Types.VARCHAR),
                outParam("P_DOC_ID_OUT", Types.VARCHAR),
                outParam("P_FROM_FAX", Types.VARCHAR),
                outParam("P_FROM_CSID", Types.VARCHAR),
                outParam("P_RECV_TIME", Types.NUMERIC),
                outParam("P_RECV_STATUS", Types.NUMERIC),
                outParam("P_FAX_STATUS", Types.NUMERIC),
                outParam("P_FTIF", Types.BLOB)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_ID", id);
        inputParams.put("P_DOC_ID", docId);

        // For load_fax, we need cursor + outputs, use custom execution
        CursorResult<Map<String, Object>> cursorResult = executeCursorProcedureWithOutputs(
                "load_fax",
                inParams,
                outParams,
                inputParams,
                "P_DOCUMENTS",
                (rs, rowNum) -> {
                    Map<String, Object> row = new HashMap<>();
                    int colCount = rs.getMetaData().getColumnCount();
                    for (int i = 1; i <= colCount; i++) {
                        String colName = rs.getMetaData().getColumnName(i);
                        row.put(colName, rs.getObject(i));
                    }
                    return row;
                }
        );

        Map<String, Object> outputs = cursorResult.getOutputs();
        Map<String, Object> result = new HashMap<>();
        result.put("id", outputs.get("P_ID_OUT"));
        result.put("docId", outputs.get("P_DOC_ID_OUT"));
        result.put("fromFax", outputs.get("P_FROM_FAX"));
        result.put("fromCSid", outputs.get("P_FROM_CSID"));
        result.put("recvTime", toLong(outputs.get("P_RECV_TIME")));
        result.put("recvStatus", toInteger(outputs.get("P_RECV_STATUS")));
        result.put("faxStatus", toInteger(outputs.get("P_FAX_STATUS")));
        result.put("fTif", outputs.get("P_FTIF"));
        result.put("documents", cursorResult.getRows());

        return result;
    }

    @Override
    public Map<String, Object> init(String id) {
        logger.debug("Calling BOFaxView.init({})", id);

        List<SqlParameter> inParams = List.of(
                inParam("P_ID", Types.VARCHAR)
        );

        List<SqlOutParameter> outParams = List.of(
                outParam("P_CLASS_ID", Types.NUMERIC),
                outParam("P_STATUS_ID", Types.NUMERIC)
        );

        Map<String, Object> inputParams = Map.of("P_ID", id);

        Map<String, Object> outputs = executeProcedureWithOutputs("init", inParams, outParams, inputParams);

        Map<String, Object> result = new HashMap<>();
        result.put("classId", toInteger(outputs.get("P_CLASS_ID")));
        result.put("statusId", toInteger(outputs.get("P_STATUS_ID")));
        return result;
    }

    private Integer toInteger(Object value) {
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).intValue();
        return Integer.parseInt(value.toString());
    }

    private Long toLong(Object value) {
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).longValue();
        return Long.parseLong(value.toString());
    }
}

package com.digibo.core.service.impl;

import com.digibo.core.service.NoteService;
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
 * NoteServiceImpl - Real implementation of NoteService
 * Calls BONote Oracle package procedures
 */
@Service
@Profile("!mock")
public class NoteServiceImpl extends BaseService implements NoteService {

    public NoteServiceImpl() {
        super("BONote");
    }

    @Override
    public List<Map<String, Object>> products() {
        logger.debug("Calling BONote.products()");

        List<SqlParameter> params = List.of();
        Map<String, Object> inputParams = Map.of();

        return executeCursorProcedure("products", params, inputParams, "P_CURSOR",
                (rs, rowNum) -> mapRow(rs));
    }

    @Override
    public List<Map<String, Object>> findNotes(String subj, String text, String cDevTypes, String corpTypes,
                                                String residTypes, String catInc, String catExc,
                                                Date dateFrom, Date dateTill) {
        logger.debug("Calling BONote.findNotes()");

        List<SqlParameter> params = List.of(
                inParam("P_SUBJ", Types.VARCHAR),
                inParam("P_TEXT", Types.VARCHAR),
                inParam("P_CDEV_TYPES", Types.VARCHAR),
                inParam("P_CORP_TYPES", Types.VARCHAR),
                inParam("P_RESID_TYPES", Types.VARCHAR),
                inParam("P_CAT_INC", Types.VARCHAR),
                inParam("P_CAT_EXC", Types.VARCHAR),
                inParam("P_DATE_FROM", Types.DATE),
                inParam("P_DATE_TILL", Types.DATE)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_SUBJ", subj);
        inputParams.put("P_TEXT", text);
        inputParams.put("P_CDEV_TYPES", cDevTypes);
        inputParams.put("P_CORP_TYPES", corpTypes);
        inputParams.put("P_RESID_TYPES", residTypes);
        inputParams.put("P_CAT_INC", catInc);
        inputParams.put("P_CAT_EXC", catExc);
        inputParams.put("P_DATE_FROM", dateFrom);
        inputParams.put("P_DATE_TILL", dateTill);

        return executeCursorProcedure("findNotes", params, inputParams, "P_CURSOR",
                (rs, rowNum) -> mapRow(rs));
    }

    @Override
    public Map<String, Object> loadNote(Long noteId) {
        logger.debug("Calling BONote.loadNote({})", noteId);

        List<SqlParameter> inParams = List.of(
                inParam("P_ID", Types.NUMERIC)
        );

        List<SqlOutParameter> outParams = List.of(
                outParam("P_SUBJ", Types.VARCHAR),
                outParam("P_TEXT", Types.VARCHAR),
                outParam("P_ACTIVE_FROM", Types.DATE),
                outParam("P_ACTIVE_TILL", Types.DATE)
        );

        Map<String, Object> inputParams = Map.of("P_ID", noteId);

        // This procedure has multiple cursors, we need custom handling
        String sql = "{ call " + packageName + ".loadNote(?, ?, ?, ?, ?, ?, ?) }";

        try (var conn = dataSource.getConnection();
             var cs = conn.prepareCall(sql)) {

            cs.setLong(1, noteId);
            cs.registerOutParameter(2, Types.VARCHAR); // P_SUBJ
            cs.registerOutParameter(3, Types.VARCHAR); // P_TEXT
            cs.registerOutParameter(4, Types.DATE);    // P_ACTIVE_FROM
            cs.registerOutParameter(5, Types.DATE);    // P_ACTIVE_TILL
            cs.registerOutParameter(6, OracleTypes.CURSOR); // P_PRODUCTS_CURSOR
            cs.registerOutParameter(7, OracleTypes.CURSOR); // P_CHANNELS_CURSOR

            cs.execute();

            Map<String, Object> result = new HashMap<>();
            result.put("id", noteId);
            result.put("subject", cs.getString(2));
            result.put("text", cs.getString(3));
            result.put("activeFrom", cs.getDate(4));
            result.put("activeTill", cs.getDate(5));

            // Process products cursor
            List<Map<String, Object>> products = new ArrayList<>();
            try (ResultSet rs = (ResultSet) cs.getObject(6)) {
                if (rs != null) {
                    while (rs.next()) {
                        products.add(mapRow(rs));
                    }
                }
            }
            result.put("products", products);

            // Process channels cursor
            List<Map<String, Object>> channels = new ArrayList<>();
            try (ResultSet rs = (ResultSet) cs.getObject(7)) {
                if (rs != null) {
                    while (rs.next()) {
                        channels.add(mapRow(rs));
                    }
                }
            }
            result.put("channels", channels);

            return result;

        } catch (Exception e) {
            logger.error("Error calling BONote.loadNote: {}", e.getMessage());
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Map<String, Object>> loadNoteHistory(Long noteId) {
        logger.debug("Calling BONote.loadNoteHistory({})", noteId);

        List<SqlParameter> params = List.of(
                inParam("P_ID", Types.NUMERIC)
        );

        Map<String, Object> inputParams = Map.of("P_ID", noteId);

        return executeCursorProcedure("loadNoteHistory", params, inputParams, "P_CURSOR",
                (rs, rowNum) -> mapRow(rs));
    }

    @Override
    public Map<String, Object> setNote(Long id, String subject, String text, Date activeFrom, Date activeTill,
                                        String cDevTypes, String corpTypes, String residTypes,
                                        String catInc, String catExc) {
        logger.debug("Calling BONote.setNote()");

        List<SqlParameter> params = List.of(
                inParam("P_ID", Types.NUMERIC),
                inParam("P_SUBJ", Types.VARCHAR),
                inParam("P_TEXT", Types.VARCHAR),
                inParam("P_ACTIVE_FROM", Types.DATE),
                inParam("P_ACTIVE_TILL", Types.DATE),
                inParam("P_CDEV_TYPES", Types.VARCHAR),
                inParam("P_CORP_TYPES", Types.VARCHAR),
                inParam("P_RESID_TYPES", Types.VARCHAR),
                inParam("P_CAT_INC", Types.VARCHAR),
                inParam("P_CAT_EXC", Types.VARCHAR)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_ID", id);
        inputParams.put("P_SUBJ", subject);
        inputParams.put("P_TEXT", text);
        inputParams.put("P_ACTIVE_FROM", activeFrom);
        inputParams.put("P_ACTIVE_TILL", activeTill);
        inputParams.put("P_CDEV_TYPES", cDevTypes);
        inputParams.put("P_CORP_TYPES", corpTypes);
        inputParams.put("P_RESID_TYPES", residTypes);
        inputParams.put("P_CAT_INC", catInc);
        inputParams.put("P_CAT_EXC", catExc);

        executeVoidProcedure("setNote", params, inputParams);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("noteId", id);
        return result;
    }

    @Override
    public Map<String, Object> setProduct(Long noteId, Long categoryId, String value) {
        logger.debug("Calling BONote.setProduct({}, {}, {})", noteId, categoryId, value);

        List<SqlParameter> params = List.of(
                inParam("P_ID", Types.NUMERIC),
                inParam("P_CAT_ID", Types.NUMERIC),
                inParam("P_VAL", Types.VARCHAR)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_ID", noteId);
        inputParams.put("P_CAT_ID", categoryId);
        inputParams.put("P_VAL", value);

        executeVoidProcedure("setProduct", params, inputParams);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("noteId", noteId);
        result.put("categoryId", categoryId);
        result.put("value", value);
        return result;
    }

    @Override
    public Map<String, Object> setChannel(Long noteId, String channel) {
        logger.debug("Calling BONote.setChannel({}, {})", noteId, channel);

        List<SqlParameter> params = List.of(
                inParam("P_ID", Types.NUMERIC),
                inParam("P_CHANNEL", Types.VARCHAR)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_ID", noteId);
        inputParams.put("P_CHANNEL", channel);

        executeVoidProcedure("setChannel", params, inputParams);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("noteId", noteId);
        result.put("channel", channel);
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

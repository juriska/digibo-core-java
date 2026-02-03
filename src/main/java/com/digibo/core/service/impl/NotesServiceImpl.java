package com.digibo.core.service.impl;

import com.digibo.core.service.NotesService;
import com.digibo.core.service.base.BaseService;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Profile("!mock")
public class NotesServiceImpl extends BaseService implements NotesService {

    public NotesServiceImpl() {
        super("BONOTE");
    }

    @Override
    public List<Map<String, Object>> products() {
        logger.debug("Calling BONOTE.products()");

        List<SqlParameter> params = List.of();
        Map<String, Object> inputParams = Map.of();

        return executeCursorProcedure("products", params, inputParams, "P_CURSOR",
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
    public List<Map<String, Object>> findNotes(String subj, String text, String cDevTypes, String corpTypes,
                                               String residTypes, String catInc, String catExc,
                                               String dateFrom, String dateTill) {
        logger.debug("Calling BONOTE.find_notes()");

        List<SqlParameter> params = List.of(
                inParam("P_SUBJ", Types.VARCHAR),
                inParam("P_TEXT", Types.VARCHAR),
                inParam("P_CDEVTYPES", Types.VARCHAR),
                inParam("P_CORPTYPES", Types.VARCHAR),
                inParam("P_RESIDTYPES", Types.VARCHAR),
                inParam("P_CATINC", Types.VARCHAR),
                inParam("P_CATEXC", Types.VARCHAR),
                inParam("P_DATEFROM", Types.VARCHAR),
                inParam("P_DATETILL", Types.VARCHAR)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_SUBJ", subj);
        inputParams.put("P_TEXT", text);
        inputParams.put("P_CDEVTYPES", cDevTypes);
        inputParams.put("P_CORPTYPES", corpTypes);
        inputParams.put("P_RESIDTYPES", residTypes);
        inputParams.put("P_CATINC", catInc);
        inputParams.put("P_CATEXC", catExc);
        inputParams.put("P_DATEFROM", dateFrom);
        inputParams.put("P_DATETILL", dateTill);

        return executeCursorProcedure("find_notes", params, inputParams, "P_CURSOR",
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
    public Map<String, Object> loadNote(Integer id) {
        logger.debug("Calling BONOTE.load_note({})", id);

        List<SqlParameter> params = List.of(
                inParam("P_ID", Types.INTEGER)
        );

        Map<String, Object> inputParams = Map.of("P_ID", id);

        List<Map<String, Object>> result = executeCursorProcedure("load_note", params, inputParams, "P_CURSOR",
                (rs, rowNum) -> {
                    Map<String, Object> row = new HashMap<>();
                    int colCount = rs.getMetaData().getColumnCount();
                    for (int i = 1; i <= colCount; i++) {
                        String colName = rs.getMetaData().getColumnName(i);
                        row.put(colName, rs.getObject(i));
                    }
                    return row;
                });

        return result.isEmpty() ? Map.of() : result.get(0);
    }

    @Override
    public List<Map<String, Object>> loadNoteHistory(Integer id) {
        logger.debug("Calling BONOTE.load_note_history({})", id);

        List<SqlParameter> params = List.of(
                inParam("P_ID", Types.INTEGER)
        );

        Map<String, Object> inputParams = Map.of("P_ID", id);

        return executeCursorProcedure("load_note_history", params, inputParams, "P_CURSOR",
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
    public Map<String, Object> setNote(Map<String, Object> noteData) {
        logger.debug("Calling BONOTE.set_note()");

        List<SqlParameter> params = List.of(
                inParam("P_ID", Types.INTEGER),
                inParam("P_SUBJECT", Types.VARCHAR),
                inParam("P_TEXT", Types.VARCHAR),
                inParam("P_ACTIVE_FROM", Types.VARCHAR),
                inParam("P_ACTIVE_TILL", Types.VARCHAR),
                inParam("P_CDEVTYPES", Types.VARCHAR),
                inParam("P_CORPTYPES", Types.VARCHAR),
                inParam("P_RESIDTYPES", Types.VARCHAR),
                inParam("P_CATINC", Types.VARCHAR),
                inParam("P_CATEXC", Types.VARCHAR)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_ID", noteData.get("id"));
        inputParams.put("P_SUBJECT", noteData.get("subject"));
        inputParams.put("P_TEXT", noteData.get("text"));
        inputParams.put("P_ACTIVE_FROM", noteData.get("activeFrom"));
        inputParams.put("P_ACTIVE_TILL", noteData.get("activeTill"));
        inputParams.put("P_CDEVTYPES", noteData.get("cDevTypes"));
        inputParams.put("P_CORPTYPES", noteData.get("corpTypes"));
        inputParams.put("P_RESIDTYPES", noteData.get("residTypes"));
        inputParams.put("P_CATINC", noteData.get("catInc"));
        inputParams.put("P_CATEXC", noteData.get("catExc"));

        return executeProcedure("set_note", params, inputParams);
    }

    @Override
    public Map<String, Object> setProduct(Integer noteId, Integer categoryId, String value) {
        logger.debug("Calling BONOTE.set_product({}, {}, {})", noteId, categoryId, value);

        List<SqlParameter> params = List.of(
                inParam("P_NOTE_ID", Types.INTEGER),
                inParam("P_CATEGORY_ID", Types.INTEGER),
                inParam("P_VALUE", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of(
                "P_NOTE_ID", noteId,
                "P_CATEGORY_ID", categoryId,
                "P_VALUE", value
        );

        return executeProcedure("set_product", params, inputParams);
    }

    @Override
    public Map<String, Object> setChannel(Integer noteId, String channel) {
        logger.debug("Calling BONOTE.set_channel({}, {})", noteId, channel);

        List<SqlParameter> params = List.of(
                inParam("P_NOTE_ID", Types.INTEGER),
                inParam("P_CHANNEL", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of(
                "P_NOTE_ID", noteId,
                "P_CHANNEL", channel
        );

        return executeProcedure("set_channel", params, inputParams);
    }
}

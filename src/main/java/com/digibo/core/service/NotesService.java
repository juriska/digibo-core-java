package com.digibo.core.service;

import java.util.List;
import java.util.Map;

public interface NotesService {

    List<Map<String, Object>> products();

    List<Map<String, Object>> findNotes(String subj, String text, String cDevTypes, String corpTypes,
                                        String residTypes, String catInc, String catExc,
                                        String dateFrom, String dateTill);

    Map<String, Object> loadNote(Integer id);

    List<Map<String, Object>> loadNoteHistory(Integer id);

    Map<String, Object> setNote(Map<String, Object> noteData);

    Map<String, Object> setProduct(Integer noteId, Integer categoryId, String value);

    Map<String, Object> setChannel(Integer noteId, String channel);
}

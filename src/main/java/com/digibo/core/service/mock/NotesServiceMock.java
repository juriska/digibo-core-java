package com.digibo.core.service.mock;

import com.digibo.core.service.NotesService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Profile("mock")
public class NotesServiceMock implements NotesService {

    @Override
    public List<Map<String, Object>> products() {
        List<Map<String, Object>> products = new ArrayList<>();
        products.add(Map.of("id", 1, "name", "Product A", "category", "Category 1"));
        products.add(Map.of("id", 2, "name", "Product B", "category", "Category 2"));
        return products;
    }

    @Override
    public List<Map<String, Object>> findNotes(String subj, String text, String cDevTypes, String corpTypes,
                                               String residTypes, String catInc, String catExc,
                                               String dateFrom, String dateTill) {
        List<Map<String, Object>> notes = new ArrayList<>();
        Map<String, Object> note = new HashMap<>();
        note.put("id", 1);
        note.put("subject", "Mock Note");
        note.put("text", "This is a mock note");
        note.put("activeFrom", "2024-01-01");
        note.put("activeTill", "2024-12-31");
        notes.add(note);
        return notes;
    }

    @Override
    public Map<String, Object> loadNote(Integer id) {
        Map<String, Object> note = new HashMap<>();
        note.put("id", id);
        note.put("subject", "Mock Note " + id);
        note.put("text", "This is mock note content for note " + id);
        note.put("activeFrom", "2024-01-01");
        note.put("activeTill", "2024-12-31");
        return note;
    }

    @Override
    public List<Map<String, Object>> loadNoteHistory(Integer id) {
        List<Map<String, Object>> history = new ArrayList<>();
        history.add(Map.of("id", 1, "noteId", id, "action", "Created", "timestamp", "2024-01-01 10:00:00"));
        history.add(Map.of("id", 2, "noteId", id, "action", "Modified", "timestamp", "2024-01-15 14:30:00"));
        return history;
    }

    @Override
    public Map<String, Object> setNote(Map<String, Object> noteData) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("id", noteData.get("id") != null ? noteData.get("id") : 999);
        return result;
    }

    @Override
    public Map<String, Object> setProduct(Integer noteId, Integer categoryId, String value) {
        return Map.of("success", true, "noteId", noteId, "categoryId", categoryId);
    }

    @Override
    public Map<String, Object> setChannel(Integer noteId, String channel) {
        return Map.of("success", true, "noteId", noteId, "channel", channel);
    }
}

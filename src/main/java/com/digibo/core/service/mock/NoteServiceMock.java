package com.digibo.core.service.mock;

import com.digibo.core.service.NoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * NoteServiceMock - Mock implementation of NoteService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class NoteServiceMock implements NoteService {

    private static final Logger logger = LoggerFactory.getLogger(NoteServiceMock.class);

    private static final List<Map<String, Object>> mockNotes = new ArrayList<>();

    static {
        Map<String, Object> note1 = new HashMap<>();
        note1.put("ID", 1L);
        note1.put("SUBJECT", "System Maintenance Notice");
        note1.put("TEXT", "Scheduled maintenance on Saturday 10PM-2AM");
        note1.put("ACTIVE_FROM", new Date());
        note1.put("ACTIVE_TILL", new Date(System.currentTimeMillis() + 7L * 24 * 60 * 60 * 1000));
        note1.put("CREATED_BY", "admin");
        note1.put("CREATED_DATE", new Date());
        mockNotes.add(note1);

        Map<String, Object> note2 = new HashMap<>();
        note2.put("ID", 2L);
        note2.put("SUBJECT", "New Feature Announcement");
        note2.put("TEXT", "Mobile banking app has been updated with new features");
        note2.put("ACTIVE_FROM", new Date());
        note2.put("ACTIVE_TILL", new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000));
        note2.put("CREATED_BY", "admin");
        note2.put("CREATED_DATE", new Date());
        mockNotes.add(note2);
    }

    @Override
    public List<Map<String, Object>> products() {
        logger.debug("[MOCK] BONote.products() called");

        List<Map<String, Object>> productCategories = new ArrayList<>();

        productCategories.add(createProduct(1L, "Accounts", null));
        productCategories.add(createProduct(2L, "Cards", null));
        productCategories.add(createProduct(3L, "Loans", null));
        productCategories.add(createProduct(4L, "Payments", null));
        productCategories.add(createProduct(11L, "Current Account", 1L));
        productCategories.add(createProduct(12L, "Savings Account", 1L));
        productCategories.add(createProduct(21L, "Debit Card", 2L));
        productCategories.add(createProduct(22L, "Credit Card", 2L));

        logger.debug("[MOCK] Returning {} product categories", productCategories.size());
        return productCategories;
    }

    private Map<String, Object> createProduct(Long id, String name, Long parentId) {
        Map<String, Object> product = new HashMap<>();
        product.put("ID", id);
        product.put("NAME", name);
        product.put("PARENT_ID", parentId);
        return product;
    }

    @Override
    public List<Map<String, Object>> findNotes(String subj, String text, String cDevTypes, String corpTypes,
                                                String residTypes, String catInc, String catExc,
                                                Date dateFrom, Date dateTill) {
        logger.debug("[MOCK] BONote.findNotes() called");

        List<Map<String, Object>> results = new ArrayList<>(mockNotes);

        if (subj != null && !subj.isEmpty()) {
            String subjLower = subj.toLowerCase();
            results.removeIf(note -> {
                String noteSubj = (String) note.get("SUBJECT");
                return noteSubj == null || !noteSubj.toLowerCase().contains(subjLower);
            });
        }

        if (text != null && !text.isEmpty()) {
            String textLower = text.toLowerCase();
            results.removeIf(note -> {
                String noteText = (String) note.get("TEXT");
                return noteText == null || !noteText.toLowerCase().contains(textLower);
            });
        }

        logger.debug("[MOCK] Returning {} notes", results.size());
        return results;
    }

    @Override
    public Map<String, Object> loadNote(Long noteId) {
        logger.debug("[MOCK] BONote.loadNote({}) called", noteId);

        Map<String, Object> note = mockNotes.stream()
                .filter(n -> noteId.equals(n.get("ID")))
                .findFirst()
                .orElse(null);

        Map<String, Object> result = new HashMap<>();
        if (note == null) {
            result.put("id", noteId);
            result.put("subject", "Note not found");
            result.put("text", "");
            result.put("activeFrom", null);
            result.put("activeTill", null);
            result.put("products", List.of());
            result.put("channels", List.of());
        } else {
            result.put("id", noteId);
            result.put("subject", note.get("SUBJECT"));
            result.put("text", note.get("TEXT"));
            result.put("activeFrom", note.get("ACTIVE_FROM"));
            result.put("activeTill", note.get("ACTIVE_TILL"));

            List<Map<String, Object>> products = new ArrayList<>();
            Map<String, Object> prod1 = new HashMap<>();
            prod1.put("NOTE_ID", noteId);
            prod1.put("CATEGORY_ID", 1L);
            prod1.put("VALUE", "Y");
            products.add(prod1);
            result.put("products", products);

            List<Map<String, Object>> channels = new ArrayList<>();
            Map<String, Object> ch1 = new HashMap<>();
            ch1.put("CHANNEL_ID", 5L);
            ch1.put("CHANNEL_NAME", "Internet Banking");
            channels.add(ch1);
            Map<String, Object> ch2 = new HashMap<>();
            ch2.put("CHANNEL_ID", 28L);
            ch2.put("CHANNEL_NAME", "Mobile Banking");
            channels.add(ch2);
            result.put("channels", channels);
        }

        return result;
    }

    @Override
    public List<Map<String, Object>> loadNoteHistory(Long noteId) {
        logger.debug("[MOCK] BONote.loadNoteHistory({}) called", noteId);

        List<Map<String, Object>> history = new ArrayList<>();

        Map<String, Object> h1 = new HashMap<>();
        h1.put("NOTE_ID", noteId);
        h1.put("ACTION", "CREATE");
        h1.put("ACTION_DATE", new Date(System.currentTimeMillis() - 7L * 24 * 60 * 60 * 1000));
        h1.put("OFFICER_NAME", "Admin User");
        history.add(h1);

        Map<String, Object> h2 = new HashMap<>();
        h2.put("NOTE_ID", noteId);
        h2.put("ACTION", "UPDATE");
        h2.put("ACTION_DATE", new Date());
        h2.put("OFFICER_NAME", "Admin User");
        history.add(h2);

        logger.debug("[MOCK] Returning {} history records", history.size());
        return history;
    }

    @Override
    public Map<String, Object> setNote(Long id, String subject, String text, Date activeFrom, Date activeTill,
                                        String cDevTypes, String corpTypes, String residTypes,
                                        String catInc, String catExc) {
        logger.debug("[MOCK] BONote.setNote() called");

        Long noteId = id != null ? id : (long) (Math.random() * 10000);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("noteId", noteId);

        logger.debug("[MOCK] Note {} {} successfully", noteId, id != null ? "updated" : "created");
        return result;
    }

    @Override
    public Map<String, Object> setProduct(Long noteId, Long categoryId, String value) {
        logger.debug("[MOCK] BONote.setProduct({}, {}, {}) called", noteId, categoryId, value);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("noteId", noteId);
        result.put("categoryId", categoryId);
        result.put("value", value);

        logger.debug("[MOCK] Product set successfully");
        return result;
    }

    @Override
    public Map<String, Object> setChannel(Long noteId, String channel) {
        logger.debug("[MOCK] BONote.setChannel({}, {}) called", noteId, channel);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("noteId", noteId);
        result.put("channel", channel);

        logger.debug("[MOCK] Channel set successfully");
        return result;
    }
}

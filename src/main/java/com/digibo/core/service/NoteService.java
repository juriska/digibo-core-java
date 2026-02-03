package com.digibo.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * NoteService - Service interface for BONote Oracle package
 * Handles note management operations
 */
public interface NoteService {

    /**
     * Get available products
     * @return List of product categories
     */
    List<Map<String, Object>> products();

    /**
     * Find notes by search criteria
     * @param subj Subject filter
     * @param text Text filter
     * @param cDevTypes C-device types filter
     * @param corpTypes Corporate types filter
     * @param residTypes Residency types filter
     * @param catInc Category include filter
     * @param catExc Category exclude filter
     * @param dateFrom Date from filter
     * @param dateTill Date till filter
     * @return List of matching notes
     */
    List<Map<String, Object>> findNotes(String subj, String text, String cDevTypes, String corpTypes,
                                         String residTypes, String catInc, String catExc,
                                         Date dateFrom, Date dateTill);

    /**
     * Load note details by ID
     * @param noteId Note ID
     * @return Note details including products and channels
     */
    Map<String, Object> loadNote(Long noteId);

    /**
     * Load note history
     * @param noteId Note ID
     * @return List of note history records
     */
    List<Map<String, Object>> loadNoteHistory(Long noteId);

    /**
     * Set/update note
     * @param id Note ID (null for new)
     * @param subject Subject
     * @param text Text content
     * @param activeFrom Active from date
     * @param activeTill Active till date
     * @param cDevTypes C-device types
     * @param corpTypes Corporate types
     * @param residTypes Residency types
     * @param catInc Category include
     * @param catExc Category exclude
     * @return Result map with success status and noteId
     */
    Map<String, Object> setNote(Long id, String subject, String text, Date activeFrom, Date activeTill,
                                 String cDevTypes, String corpTypes, String residTypes,
                                 String catInc, String catExc);

    /**
     * Set product for note
     * @param noteId Note ID
     * @param categoryId Product category ID
     * @param value Value
     * @return Result map
     */
    Map<String, Object> setProduct(Long noteId, Long categoryId, String value);

    /**
     * Set channel for note
     * @param noteId Note ID
     * @param channel Channel value
     * @return Result map
     */
    Map<String, Object> setChannel(Long noteId, String channel);
}

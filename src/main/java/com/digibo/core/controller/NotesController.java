package com.digibo.core.controller;

import com.digibo.core.exception.ValidationException;
import com.digibo.core.service.NotesService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * NotesController - REST controller for notes operations
 * Maps to /api/notes endpoints
 *
 * Authorization: Uses Oracle grants via @PreAuthorize.
 * User must have EXECUTE grant on BONOTE package procedures.
 */
@RestController
@RequestMapping("/api/notes")
public class NotesController {

    private final NotesService notesService;

    public NotesController(NotesService notesService) {
        this.notesService = notesService;
    }

    /**
     * GET /api/notes/products
     * Get available products using BONote.products()
     *
     * Requires: EXECUTE on BONOTE.PRODUCTS
     */
    @GetMapping("/products")
    @PreAuthorize("hasPermission(null, 'BONOTE.PRODUCTS')")
    public ResponseEntity<List<Map<String, Object>>> getProducts() {
        List<Map<String, Object>> result = notesService.products();
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/notes/search
     * Search notes using BONote.findNotes()
     *
     * Query params:
     * - subj: Subject filter
     * - text: Text filter
     * - cDevTypes: Device types
     * - corpTypes: Corporate types
     * - residTypes: Residency types
     * - catInc: Categories to include
     * - catExc: Categories to exclude
     * - dateFrom: Active from date
     * - dateTill: Active till date
     *
     * Requires: EXECUTE on BONOTE.FIND_NOTES
     */
    @GetMapping("/search")
    @PreAuthorize("hasPermission(null, 'BONOTE.FIND_NOTES')")
    public ResponseEntity<List<Map<String, Object>>> searchNotes(
            @RequestParam(required = false) String subj,
            @RequestParam(required = false) String text,
            @RequestParam(required = false) String cDevTypes,
            @RequestParam(required = false) String corpTypes,
            @RequestParam(required = false) String residTypes,
            @RequestParam(required = false) String catInc,
            @RequestParam(required = false) String catExc,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTill) {
        List<Map<String, Object>> result = notesService.findNotes(
                subj, text, cDevTypes, corpTypes, residTypes, catInc, catExc, dateFrom, dateTill);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/notes/:id
     * Get note details using BONote.loadNote()
     *
     * Requires: EXECUTE on BONOTE.LOAD_NOTE
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'BONOTE.LOAD_NOTE')")
    public ResponseEntity<Map<String, Object>> getNote(@PathVariable Integer id) {
        Map<String, Object> result = notesService.loadNote(id);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/notes/:id/history
     * Get note history using BONote.loadNoteHistory()
     *
     * Requires: EXECUTE on BONOTE.LOAD_NOTE_HISTORY
     */
    @GetMapping("/{id}/history")
    @PreAuthorize("hasPermission(null, 'BONOTE.LOAD_NOTE_HISTORY')")
    public ResponseEntity<List<Map<String, Object>>> getNoteHistory(@PathVariable Integer id) {
        List<Map<String, Object>> result = notesService.loadNoteHistory(id);
        return ResponseEntity.ok(result);
    }

    /**
     * PUT /api/notes/:id
     * Update or create note using BONote.setNote()
     *
     * Body: {
     *   subject: string,
     *   text: string,
     *   activeFrom: date (optional),
     *   activeTill: date (optional),
     *   cDevTypes: string (optional),
     *   corpTypes: string (optional),
     *   residTypes: string (optional),
     *   catInc: string (optional),
     *   catExc: string (optional)
     * }
     *
     * Requires: EXECUTE on BONOTE.SET_NOTE
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'BONOTE.SET_NOTE')")
    public ResponseEntity<Map<String, Object>> updateNote(
            @PathVariable String id,
            @RequestBody Map<String, Object> noteData) {
        String subject = (String) noteData.get("subject");
        String text = (String) noteData.get("text");

        if (subject == null || text == null) {
            throw new ValidationException("Missing required fields: subject, text");
        }

        Integer noteId = "new".equals(id) ? null : Integer.parseInt(id);
        noteData.put("id", noteId);

        Map<String, Object> result = notesService.setNote(noteData);
        result.put("message", "Note saved successfully");
        return ResponseEntity.ok(result);
    }

    /**
     * PUT /api/notes/:id/product
     * Set product for note using BONote.setProduct()
     *
     * Body: {
     *   categoryId: number,
     *   value: string
     * }
     *
     * Requires: EXECUTE on BONOTE.SET_PRODUCT
     */
    @PutMapping("/{id}/product")
    @PreAuthorize("hasPermission(null, 'BONOTE.SET_PRODUCT')")
    public ResponseEntity<Map<String, Object>> setProduct(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> request) {
        Integer categoryId = (Integer) request.get("categoryId");
        String value = (String) request.get("value");

        if (categoryId == null || value == null) {
            throw new ValidationException("Missing required fields: categoryId, value");
        }

        Map<String, Object> result = notesService.setProduct(id, categoryId, value);
        result.put("message", "Product set successfully");
        return ResponseEntity.ok(result);
    }

    /**
     * PUT /api/notes/:id/channel
     * Set channel for note using BONote.setChannel()
     *
     * Body: {
     *   channel: string
     * }
     *
     * Requires: EXECUTE on BONOTE.SET_CHANNEL
     */
    @PutMapping("/{id}/channel")
    @PreAuthorize("hasPermission(null, 'BONOTE.SET_CHANNEL')")
    public ResponseEntity<Map<String, Object>> setChannel(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> request) {
        String channel = (String) request.get("channel");

        if (channel == null) {
            throw new ValidationException("Missing required field: channel");
        }

        Map<String, Object> result = notesService.setChannel(id, channel);
        result.put("message", "Channel set successfully");
        return ResponseEntity.ok(result);
    }
}

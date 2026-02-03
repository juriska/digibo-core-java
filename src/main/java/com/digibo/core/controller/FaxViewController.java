package com.digibo.core.controller;

import com.digibo.core.exception.ResourceNotFoundException;
import com.digibo.core.exception.ValidationException;
import com.digibo.core.service.FaxViewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * FaxViewController - REST controller for viewing fax documents
 * Maps to /api/faxview endpoints
 */
@RestController
@RequestMapping("/api/faxview")
public class FaxViewController {

    private final FaxViewService faxViewService;

    public FaxViewController(FaxViewService faxViewService) {
        this.faxViewService = faxViewService;
    }

    /**
     * GET /api/faxview/documents/my
     * Get user's assigned fax documents using BOFaxView.find_my_documents()
     *
     * Query params:
     * - classes: Comma-separated class IDs (required)
     */
    @GetMapping("/documents/my")
    public ResponseEntity<List<Map<String, Object>>> findMyDocuments(
            @RequestParam String classes) {

        if (classes == null || classes.isEmpty()) {
            throw new ValidationException("Missing required parameter: classes");
        }

        List<Map<String, Object>> result = faxViewService.findMyDocuments(classes);
        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/faxview/:id/lock
     * Set lock on fax or fax document using BOFaxView.set_lock()
     */
    @PostMapping("/{id}/lock")
    public ResponseEntity<Map<String, Object>> setLock(
            @PathVariable String id,
            @RequestBody Map<String, Object> request) {

        Integer doc = (Integer) request.get("doc");

        if (doc == null) {
            throw new ValidationException("Missing required field: doc");
        }

        Map<String, Object> result = faxViewService.setLock(id, doc);

        Integer lockStatus = (Integer) result.get("lockStatus");
        String message;
        if (lockStatus == null || lockStatus == 0) {
            message = "Lock acquired successfully";
        } else if (lockStatus == 1) {
            message = "Locked by " + result.get("officerName") + " (" + result.get("officerPhone") + ")";
        } else if (lockStatus == 2) {
            message = "Locked by another user";
        } else {
            message = "Error acquiring lock";
        }

        result.put("message", message);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/faxview/:id/history
     * Load history of fax document using BOFaxView.load_history()
     */
    @GetMapping("/{id}/history")
    public ResponseEntity<List<Map<String, Object>>> loadHistory(@PathVariable String id) {
        List<Map<String, Object>> result = faxViewService.loadHistory(id);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/faxview/:id/actual
     * Load actual (current) state of fax document using BOFaxView.load_actual()
     */
    @GetMapping("/{id}/actual")
    public ResponseEntity<List<Map<String, Object>>> loadActual(@PathVariable String id) {
        List<Map<String, Object>> result = faxViewService.loadActual(id);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/faxview/last-officer
     * Get last officer who processed document for customer using BOFaxView.last_officer()
     *
     * Query params:
     * - custId: Customer ID (optional)
     * - fromAccount: Account number (optional)
     * - classId: Document class ID, 0 = any (optional, default 0)
     * - officers: Comma-separated officer IDs (required)
     */
    @GetMapping("/last-officer")
    public ResponseEntity<Map<String, Object>> lastOfficer(
            @RequestParam(required = false) Long custId,
            @RequestParam(required = false) String fromAccount,
            @RequestParam(required = false, defaultValue = "0") Integer classId,
            @RequestParam String officers) {

        if (officers == null || officers.isEmpty()) {
            throw new ValidationException("Missing required parameter: officers");
        }

        Long officerId = faxViewService.lastOfficer(custId, fromAccount, classId, officers);

        return ResponseEntity.ok(Map.of("officerId", officerId != null ? officerId : 0L));
    }

    /**
     * GET /api/faxview/next-fax-id
     * Get next new fax ID using BOFaxView.next_fax_id()
     */
    @GetMapping("/next-fax-id")
    public ResponseEntity<Map<String, Object>> nextFaxId() {
        Long faxId = faxViewService.nextFaxId();
        return ResponseEntity.ok(Map.of("faxId", faxId != null ? faxId : 0L));
    }

    /**
     * GET /api/faxview/next-document-id
     * Get next document ID for user's documents using BOFaxView.next_document_id()
     *
     * Query params:
     * - docId: Current document ID (required)
     * - classes: Comma-separated class IDs (required)
     */
    @GetMapping("/next-document-id")
    public ResponseEntity<Map<String, Object>> nextDocumentId(
            @RequestParam Long docId,
            @RequestParam String classes) {

        if (docId == null || classes == null || classes.isEmpty()) {
            throw new ValidationException("Missing required parameters: docId, classes");
        }

        Map<String, Object> result = faxViewService.nextDocumentId(docId, classes);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/faxview/:id
     * Load fax with all details and documents using BOFaxView.load_fax()
     *
     * Query params:
     * - docId: Optional document ID filter
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> loadFax(
            @PathVariable String id,
            @RequestParam(required = false) String docId) {

        Map<String, Object> result = faxViewService.loadFax(id, docId);

        if (result.get("id") == null) {
            throw new ResourceNotFoundException("Fax", id);
        }

        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/faxview/:id/init
     * Initialize/get basic info for fax document using BOFaxView.init()
     */
    @GetMapping("/{id}/init")
    public ResponseEntity<Map<String, Object>> init(@PathVariable String id) {
        Map<String, Object> result = faxViewService.init(id);

        if (result.get("classId") == null) {
            throw new ResourceNotFoundException("Document", id);
        }

        return ResponseEntity.ok(result);
    }
}

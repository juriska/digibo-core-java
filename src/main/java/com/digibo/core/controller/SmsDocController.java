package com.digibo.core.controller;

import com.digibo.core.exception.ValidationException;
import com.digibo.core.service.SmsDocService;
import com.digibo.core.util.DateUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * SmsDocController - REST controller for SMS document operations
 * Maps to /api/smsdoc endpoints
 */
@RestController
@RequestMapping("/api/smsdoc")
public class SmsDocController {

    private final SmsDocService smsDocService;

    public SmsDocController(SmsDocService smsDocService) {
        this.smsDocService = smsDocService;
    }

    /**
     * GET /api/smsdoc/documents/search
     * Search SMS documents using BOSMSDocument.find()
     *
     * Query params:
     * - custId: Customer ID
     * - custName: Customer name (partial match)
     * - userLogin: User login (partial match)
     * - officerId: Officer ID
     * - pType: Document type (comma-separated class IDs: 401=SMS_CREATE, 402=SMS_UPDATE)
     * - mobile: Mobile phone number
     * - docId: Document ID
     * - statuses: Comma-separated status IDs
     * - createdFrom: Start date (ISO format)
     * - createdTill: End date (ISO format)
     */
    @GetMapping("/documents/search")
    public ResponseEntity<List<Map<String, Object>>> searchDocuments(
            @RequestParam(required = false) String custId,
            @RequestParam(required = false) String custName,
            @RequestParam(required = false) String userLogin,
            @RequestParam(required = false) Long officerId,
            @RequestParam(required = false) String pType,
            @RequestParam(required = false) String mobile,
            @RequestParam(required = false) String docId,
            @RequestParam(required = false) String statuses,
            @RequestParam(required = false) String createdFrom,
            @RequestParam(required = false) String createdTill) {

        List<Map<String, Object>> result = smsDocService.find(
                custId, custName, userLogin, officerId,
                pType, mobile, docId, statuses,
                DateUtils.parseDate(createdFrom), DateUtils.parseDate(createdTill));
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/smsdoc/documents/:id/details
     * Get detailed SMS document info using BOSMSDocument.sms() procedure
     */
    @GetMapping("/documents/{id}/details")
    public ResponseEntity<Map<String, Object>> getDocumentDetails(@PathVariable String id) {
        Map<String, Object> result = smsDocService.sms(id);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/smsdoc/phone/check
     * Check if phone number already exists using BOSMSDocument.already_exists()
     *
     * Query params:
     * - phone: Phone number to check
     */
    @GetMapping("/phone/check")
    public ResponseEntity<Map<String, Object>> checkPhone(@RequestParam String phone) {
        if (phone == null || phone.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "exists", false,
                    "message", "Phone number is required"
            ));
        }

        int count = smsDocService.alreadyExists(phone);
        return ResponseEntity.ok(Map.of(
                "exists", count > 0,
                "count", count,
                "phone", phone
        ));
    }

    /**
     * POST /api/smsdoc/documents/:id/update
     * Update/process SMS document using BOSMSDocument.update_document()
     * This creates SMS channel on approval (status = EXECUTED)
     *
     * Body params:
     * - reason: Reason for status change (optional)
     * - newStatus: New status ID (e.g., 7=EXECUTED for approval)
     * - messageId: Message ID for audit log
     */
    @PostMapping("/documents/{id}/update")
    public ResponseEntity<Map<String, Object>> updateDocument(
            @PathVariable String id,
            @RequestBody Map<String, Object> body) {

        String reason = (String) body.get("reason");
        Integer newStatus = body.get("newStatus") != null ? ((Number) body.get("newStatus")).intValue() : null;
        Long messageId = body.get("messageId") != null ? ((Number) body.get("messageId")).longValue() : null;

        if (newStatus == null || messageId == null) {
            throw new ValidationException("newStatus and messageId are required");
        }

        smsDocService.updateDocument(id, reason, newStatus, messageId);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Document updated successfully"
        ));
    }
}

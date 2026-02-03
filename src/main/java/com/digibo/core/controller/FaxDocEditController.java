package com.digibo.core.controller;

import com.digibo.core.exception.ValidationException;
import com.digibo.core.service.FaxDocEditService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

/**
 * FaxDocEditController - REST controller for fax document editing operations
 * Maps to /api/faxdocedit endpoints
 */
@RestController
@RequestMapping("/api/faxdocedit")
public class FaxDocEditController {

    private final FaxDocEditService faxDocEditService;

    public FaxDocEditController(FaxDocEditService faxDocEditService) {
        this.faxDocEditService = faxDocEditService;
    }

    /**
     * PUT /api/faxdocedit/documents/:id
     * Save/update fax document using BOFaxDocEdit.save_document()
     */
    @PutMapping("/documents/{id}")
    public ResponseEntity<Map<String, Object>> saveDocument(
            @PathVariable String id,
            @RequestBody Map<String, Object> request) {

        Long officerId = getLongValue(request.get("officerId"));
        String custId = (String) request.get("custId");
        String fromAccount = (String) request.get("fromAccount");
        BigDecimal amount = request.get("amount") != null ?
                new BigDecimal(request.get("amount").toString()) : null;
        String currency = (String) request.get("currency");
        String partner = (String) request.get("partner");
        String note = (String) request.get("note");
        String subject = (String) request.get("subject");
        Integer status = getIntegerValue(request.get("status"));

        // Validate required fields
        if (officerId == null || custId == null || currency == null || status == null) {
            throw new ValidationException("Missing required fields: officerId, custId, currency, status");
        }

        Map<String, Object> result = faxDocEditService.saveDocument(
                id,
                officerId,
                custId,
                fromAccount,
                amount,
                currency,
                partner,
                note,
                subject,
                status
        );

        return ResponseEntity.ok(result);
    }

    private Long getLongValue(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Long) {
            return (Long) value;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return Long.parseLong(value.toString());
    }

    private Integer getIntegerValue(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return Integer.parseInt(value.toString());
    }
}

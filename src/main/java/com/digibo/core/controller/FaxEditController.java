package com.digibo.core.controller;

import com.digibo.core.exception.ValidationException;
import com.digibo.core.service.FaxEditService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.Map;

/**
 * FaxEditController - REST controller for fax editing operations
 * Maps to /api/faxedit endpoints
 */
@RestController
@RequestMapping("/api/faxedit")
public class FaxEditController {

    private final FaxEditService faxEditService;

    public FaxEditController(FaxEditService faxEditService) {
        this.faxEditService = faxEditService;
    }

    /**
     * POST /api/faxedit/documents
     * Add a new document from fax using BOFaxEdit.add_document()
     */
    @PostMapping("/documents")
    public ResponseEntity<Map<String, Object>> addDocument(@RequestBody Map<String, Object> request) {

        String faxId = (String) request.get("faxId");
        Integer docClass = getIntegerValue(request.get("docClass"));
        Long officerId = getLongValue(request.get("officerId"));
        String custId = (String) request.get("custId");
        String fromAccount = (String) request.get("fromAccount");
        BigDecimal amnt = request.get("amnt") != null ?
                new BigDecimal(request.get("amnt").toString()) : null;
        String ccy = (String) request.get("ccy");
        String partner = (String) request.get("partner");
        String note = (String) request.get("note");
        String subj = (String) request.get("subj");
        Integer docStatus = getIntegerValue(request.get("docStatus"));
        String dTif = (String) request.get("dTif");

        // Validate required fields
        if (faxId == null || docClass == null || officerId == null || docStatus == null) {
            throw new ValidationException("Missing required fields: faxId, docClass, officerId, docStatus");
        }

        // Convert base64 TIF to byte array if provided
        byte[] tifBuffer = null;
        if (dTif != null && !dTif.isEmpty()) {
            tifBuffer = Base64.getDecoder().decode(dTif);
        }

        Map<String, Object> result = faxEditService.addDocument(
                faxId,
                docClass,
                officerId,
                custId,
                fromAccount,
                amnt,
                ccy,
                partner,
                note,
                subj,
                docStatus,
                tifBuffer
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
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

package com.digibo.core.controller;

import com.digibo.core.service.SmsViewService;
import com.digibo.core.util.DateUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * SmsViewController - REST controller for SMS view operations
 * Maps to /api/smsview endpoints
 */
@RestController
@RequestMapping("/api/smsview")
public class SmsViewController {

    private final SmsViewService smsViewService;

    public SmsViewController(SmsViewService smsViewService) {
        this.smsViewService = smsViewService;
    }

    /**
     * GET /api/smsview/types
     * Get SMS message types using bosmsview.get_types()
     */
    @GetMapping("/types")
    public ResponseEntity<List<Map<String, Object>>> getTypes() {
        List<Map<String, Object>> result = smsViewService.getTypes();
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/smsview/messages/search
     * Search SMS messages using bosmsview.find()
     *
     * Query params:
     * - custId: Customer ID
     * - custName: Customer name (partial match)
     * - userLogin: User login (phone number, partial match)
     * - officerId: Officer ID
     * - pType: Message type ('I'=Incoming, 'O'=Outgoing, or type IDs)
     * - mobile: Mobile phone number
     * - text: Message text (partial match)
     * - statuses: Comma-separated status IDs
     * - createdFrom: Start date (ISO format)
     * - createdTill: End date (ISO format)
     */
    @GetMapping("/messages/search")
    public ResponseEntity<List<Map<String, Object>>> searchMessages(
            @RequestParam(required = false) String custId,
            @RequestParam(required = false) String custName,
            @RequestParam(required = false) String userLogin,
            @RequestParam(required = false) Long officerId,
            @RequestParam(required = false) String pType,
            @RequestParam(required = false) String mobile,
            @RequestParam(required = false) String text,
            @RequestParam(required = false) String statuses,
            @RequestParam(required = false) String createdFrom,
            @RequestParam(required = false) String createdTill) {

        List<Map<String, Object>> result = smsViewService.find(
                custId, custName, userLogin, officerId,
                pType, mobile, text, statuses,
                DateUtils.parseDate(createdFrom), DateUtils.parseDate(createdTill));
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/smsview/messages/:id/details
     * Get detailed SMS message info using bosmsview.sms() procedure
     */
    @GetMapping("/messages/{id}/details")
    public ResponseEntity<Map<String, Object>> getMessageDetails(@PathVariable String id) {
        Map<String, Object> result = smsViewService.sms(id);
        return ResponseEntity.ok(result);
    }
}

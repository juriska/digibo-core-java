package com.digibo.core.controller;

import com.digibo.core.service.FindUsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * FindUsersController - REST controller for user search operations
 * Maps to /api/findusers endpoints
 */
@RestController
@RequestMapping("/api/findusers")
public class FindUsersController {

    private final FindUsersService findUsersService;

    public FindUsersController(FindUsersService findUsersService) {
        this.findUsersService = findUsersService;
    }

    /**
     * GET /api/findusers/search
     * Search users by various criteria using BOFindUsers.find_users()
     *
     * Query params:
     * - userId: User ID (exact match)
     * - globusUserId: Globus user ID (exact match)
     * - userName: User name (partial match)
     * - personalId: Personal ID (partial match)
     * - officerId: Officer ID
     * - phone: Phone number (partial match, searches both phone and mobile)
     * - fax: Fax number (partial match)
     * - email: Email address (partial match)
     * - channelId: Channel ID
     * - login: Login name (partial match)
     * - cDevNum: Device serial number (partial match)
     * - channel: Channel number
     * - custId: Customer ID
     * - custResidence: Customer residence country code
     * - custType: Customer type
     * - dateFrom: Registration date from (ISO format)
     * - dateTill: Registration date till (ISO format)
     * - status: Status ID
     */
    @GetMapping("/search")
    public ResponseEntity<List<Map<String, Object>>> search(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String globusUserId,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String personalId,
            @RequestParam(required = false) Long officerId,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String fax,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String channelId,
            @RequestParam(required = false) String login,
            @RequestParam(required = false) String cDevNum,
            @RequestParam(required = false) Long channel,
            @RequestParam(required = false) String custId,
            @RequestParam(required = false) String custResidence,
            @RequestParam(required = false) String custType,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTill,
            @RequestParam(required = false) Integer status) {

        Date dateFromDate = parseDate(dateFrom);
        Date dateTillDate = parseDate(dateTill);

        List<Map<String, Object>> result = findUsersService.findUsers(
                userId,
                globusUserId,
                userName,
                personalId,
                officerId,
                phone,
                fax,
                email,
                channelId,
                login,
                cDevNum,
                channel,
                custId,
                custResidence,
                custType,
                dateFromDate,
                dateTillDate,
                status
        );

        return ResponseEntity.ok(result);
    }

    private Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }
}

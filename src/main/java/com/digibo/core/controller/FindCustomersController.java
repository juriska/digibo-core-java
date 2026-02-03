package com.digibo.core.controller;

import com.digibo.core.exception.ResourceNotFoundException;
import com.digibo.core.exception.ValidationException;
import com.digibo.core.service.FindCustomersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * FindCustomersController - REST controller for customer search operations
 * Maps to /api/findcustomers endpoints
 */
@RestController
@RequestMapping("/api/findcustomers")
public class FindCustomersController {

    private final FindCustomersService findCustomersService;

    public FindCustomersController(FindCustomersService findCustomersService) {
        this.findCustomersService = findCustomersService;
    }

    /**
     * GET /api/findcustomers/search
     * Search customers by criteria using BOFindCustomers.find_customers()
     *
     * Query params:
     * - custId: Customer ID (exact match)
     * - custName: Customer name (partial match)
     * - legalId: Legal ID / Personal ID (partial match)
     * - licence: Licence number (partial match)
     */
    @GetMapping("/search")
    public ResponseEntity<List<Map<String, Object>>> search(
            @RequestParam(required = false) String custId,
            @RequestParam(required = false) String custName,
            @RequestParam(required = false) String legalId,
            @RequestParam(required = false) String licence) {

        List<Map<String, Object>> result = findCustomersService.findCustomers(
                custId,
                custName,
                legalId,
                licence
        );

        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/findcustomers/:id
     * Load detailed customer information by ID using BOFindCustomers.load_customer_by_id()
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable String id) {
        Long customerId;
        try {
            customerId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new ValidationException("Invalid customer ID: Customer ID must be a valid number");
        }

        Map<String, Object> result = findCustomersService.loadCustomerById(customerId);

        if (result == null || result.get("id") == null) {
            throw new ResourceNotFoundException("Customer", customerId);
        }

        return ResponseEntity.ok(result);
    }
}

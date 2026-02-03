package com.digibo.core.service;

import java.util.List;
import java.util.Map;

/**
 * FindCustomersService - Service interface for BOFindCustomers Oracle package
 * Handles customer search and retrieval operations
 */
public interface FindCustomersService {

    /**
     * Find customers by search criteria
     *
     * @param custId Customer ID
     * @param custName Customer name
     * @param legalId Legal ID
     * @param licence Licence
     * @return List of matching customers
     */
    List<Map<String, Object>> findCustomers(String custId, String custName, String legalId, String licence);

    /**
     * Load detailed customer information by ID
     *
     * @param customerId Customer ID
     * @return Customer details map with id, name, issuerCountry, personalId, passportNo,
     *         street, city, country, zip, phone, mobile, fax, email, apart, house,
     *         stdQ, specQ, answer, regDate, changeDate, changeOfficerId, changeLogin,
     *         type, hasAgreementInGlobus
     */
    Map<String, Object> loadCustomerById(Long customerId);
}

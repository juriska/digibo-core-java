package com.digibo.core.service.mock;

import com.digibo.core.service.FindCustomersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * FindCustomersServiceMock - Mock implementation of FindCustomersService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class FindCustomersServiceMock implements FindCustomersService {

    private static final Logger logger = LoggerFactory.getLogger(FindCustomersServiceMock.class);

    @Override
    public List<Map<String, Object>> findCustomers(String custId, String custName, String legalId, String licence) {
        logger.debug("[MOCK] findCustomers({}, {}, {}, {})", custId, custName, legalId, licence);

        List<Map<String, Object>> results = new ArrayList<>();

        Map<String, Object> cust1 = new HashMap<>();
        cust1.put("CUST_ID", "CUST001");
        cust1.put("CUST_NAME", "John Doe");
        cust1.put("LEGAL_ID", "LID001");
        cust1.put("LICENCE", "LIC001");
        cust1.put("TYPE", "P");
        cust1.put("COUNTRY", "US");
        cust1.put("CITY", "New York");
        results.add(cust1);

        Map<String, Object> cust2 = new HashMap<>();
        cust2.put("CUST_ID", "CUST002");
        cust2.put("CUST_NAME", "Acme Corp");
        cust2.put("LEGAL_ID", "LID002");
        cust2.put("LICENCE", "LIC002");
        cust2.put("TYPE", "C");
        cust2.put("COUNTRY", "US");
        cust2.put("CITY", "Los Angeles");
        results.add(cust2);

        return results;
    }

    @Override
    public Map<String, Object> loadCustomerById(Long customerId) {
        logger.debug("[MOCK] loadCustomerById({})", customerId);

        Map<String, Object> result = new HashMap<>();
        result.put("id", customerId);
        result.put("name", "John Doe");
        result.put("issuerCountry", "US");
        result.put("personalId", "123456789");
        result.put("passportNo", "AB123456");
        result.put("street", "123 Main St");
        result.put("city", "New York");
        result.put("country", "US");
        result.put("zip", "10001");
        result.put("phone", "+1-555-1234");
        result.put("mobile", "+1-555-5678");
        result.put("fax", "+1-555-9999");
        result.put("email", "john.doe@example.com");
        result.put("apart", "4A");
        result.put("house", "12B");
        result.put("stdQ", 1);
        result.put("specQ", "What is your pet's name?");
        result.put("answer", "Fluffy");
        result.put("regDate", new Date(System.currentTimeMillis() - 365L * 24 * 60 * 60 * 1000));
        result.put("changeDate", new Date());
        result.put("changeOfficerId", "OFF001");
        result.put("changeLogin", "admin");
        result.put("type", "P");
        result.put("hasAgreementInGlobus", 1);

        return result;
    }
}

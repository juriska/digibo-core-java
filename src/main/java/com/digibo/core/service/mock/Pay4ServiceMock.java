package com.digibo.core.service.mock;

import com.digibo.core.service.Pay4Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Pay4ServiceMock - Mock implementation of Pay4Service
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class Pay4ServiceMock implements Pay4Service {

    private static final Logger logger = LoggerFactory.getLogger(Pay4ServiceMock.class);

    private static final List<Map<String, Object>> mockPaymentDocuments = new ArrayList<>();

    static {
        Map<String, Object> payment1 = new HashMap<>();
        payment1.put("ID", "PAY001");
        payment1.put("CUST_ID", "CUST001");
        payment1.put("LOGIN", "jdoe");
        payment1.put("STATUS_ID", 1);
        payment1.put("CLASS_ID", 10);
        payment1.put("ORDER_DATE", new Date());
        payment1.put("CREDIT_CCY", "EUR");
        payment1.put("DEBIT_CCY", "EUR");
        payment1.put("AMOUNT", "1000.00");
        mockPaymentDocuments.add(payment1);

        Map<String, Object> payment2 = new HashMap<>();
        payment2.put("ID", "PAY002");
        payment2.put("CUST_ID", "CUST002");
        payment2.put("LOGIN", "jsmith");
        payment2.put("STATUS_ID", 5);
        payment2.put("CLASS_ID", 20);
        payment2.put("ORDER_DATE", new Date());
        payment2.put("CREDIT_CCY", "USD");
        payment2.put("DEBIT_CCY", "EUR");
        payment2.put("AMOUNT", "5000.00");
        mockPaymentDocuments.add(payment2);
    }

    @Override
    public List<Map<String, Object>> find(String custId, String custName, String userLogin, Long officerId,
                                           String benName, String pmtDetails, String fromContract,
                                           String amountFrom, String amountTill, String currencies, String pmtClass,
                                           Date effectFrom, Date effectTill,
                                           String paymentId, String channels, String statuses,
                                           Date createdFrom, Date createdTill) {
        logger.debug("[MOCK] BOPayment.find() called");

        List<Map<String, Object>> results = new ArrayList<>(mockPaymentDocuments);

        if (custId != null && !custId.isEmpty()) {
            results.removeIf(d -> !custId.equals(d.get("CUST_ID")));
        }

        if (userLogin != null && !userLogin.isEmpty()) {
            String loginLower = userLogin.toLowerCase();
            results.removeIf(d -> {
                String login = (String) d.get("LOGIN");
                return login == null || !login.toLowerCase().contains(loginLower);
            });
        }

        if (paymentId != null && !paymentId.isEmpty()) {
            results.removeIf(d -> !paymentId.equals(d.get("ID")));
        }

        if (statuses != null && !statuses.isEmpty()) {
            List<Integer> statusList = Arrays.stream(statuses.split(","))
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .toList();
            results.removeIf(d -> !statusList.contains((Integer) d.get("STATUS_ID")));
        }

        if (pmtClass != null && !pmtClass.isEmpty()) {
            List<Integer> classList = Arrays.stream(pmtClass.split(","))
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .toList();
            results.removeIf(d -> !classList.contains((Integer) d.get("CLASS_ID")));
        }

        if (currencies != null && !currencies.isEmpty()) {
            List<String> currList = Arrays.stream(currencies.split(","))
                    .map(String::trim)
                    .map(String::toUpperCase)
                    .toList();
            results.removeIf(d -> !currList.contains(d.get("CREDIT_CCY")) && !currList.contains(d.get("DEBIT_CCY")));
        }

        logger.debug("[MOCK] Returning {} filtered payment documents", results.size());
        return results;
    }

    @Override
    public Map<String, Object> payment(String paymentId) {
        logger.debug("[MOCK] BOPayment.payment({}) called", paymentId);

        Map<String, Object> doc = mockPaymentDocuments.stream()
                .filter(d -> paymentId.equals(d.get("ID")))
                .findFirst()
                .orElse(null);

        Map<String, Object> result = new HashMap<>();
        result.put("id", paymentId);
        result.put("userName", doc != null ? doc.get("LOGIN") : "Mock User");
        result.put("userId", "MOCK001");
        result.put("officerName", "Mock Officer");
        result.put("custName", "Mock Customer");
        result.put("custAccount", "LV00MOCK0000000000001");
        result.put("benName", "Mock Beneficiary");
        result.put("benId", "BEN001");
        result.put("benRes", "LV");
        result.put("benCity", "Riga");
        result.put("benStreet", "Mock Street 1");
        result.put("benAcnt", "LV00MOCK0000000000002");
        result.put("benType", "COMPANY");
        result.put("ordName", "Mock Orderer");
        result.put("ordId", "ORD001");
        result.put("ordRes", "LV");
        result.put("ordAcnt", "LV00MOCK0000000000001");
        result.put("benBankName", "Mock Bank");
        result.put("benBankBranch", "Main Branch");
        result.put("benBankSwiftCode", "MOCKSWFT");
        result.put("benBankOtherCode", null);
        result.put("benBankAddr", "Mock Bank Address");
        result.put("imBankName", null);
        result.put("imBankAcnt", null);
        result.put("imBankSwiftCode", null);
        result.put("imBankOtherCode", null);
        result.put("imBankAddr", null);
        result.put("paymentDetails", "Mock payment details");
        result.put("itb", "Info to bank");
        result.put("itc", "Info to customer");
        result.put("epc", null);
        result.put("itd", null);
        result.put("exchangeRate", "1.00");
        result.put("comType", "SHA");
        result.put("typeId", 1);
        result.put("eCheque", null);
        result.put("eExpiry", null);
        result.put("signTime", new Date());
        result.put("signDevType", 5);
        result.put("signDevId", "MOCK_DEVICE");
        result.put("signKey1", null);
        result.put("signKey2", null);
        result.put("signRSA", null);
        result.put("templateName", null);
        result.put("globusFt", null);
        result.put("bookingDate", new Date());
        result.put("execDate", new Date());
        result.put("taxPayerId", null);
        result.put("isTaxDoc", 0);
        result.put("isUtPayment", 0);
        result.put("location", "LV");

        return result;
    }
}

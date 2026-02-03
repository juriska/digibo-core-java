package com.digibo.core.service.mock;

import com.digibo.core.dto.request.PaymentSearchRequest;
import com.digibo.core.dto.response.PaymentDetailsResponse;
import com.digibo.core.dto.response.PaymentSearchResponse;
import com.digibo.core.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * PaymentServiceMock - Mock implementation of PaymentService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class PaymentServiceMock implements PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceMock.class);

    @Override
    public PaymentSearchResponse find(PaymentSearchRequest filters) {
        logger.debug("[MOCK] find()");

        List<Map<String, Object>> payments = new ArrayList<>();

        Map<String, Object> payment1 = new HashMap<>();
        payment1.put("PAYMENT_ID", "PMT001");
        payment1.put("CUST_NAME", "John Doe");
        payment1.put("BEN_NAME", "Jane Smith");
        payment1.put("AMOUNT", "1000.00");
        payment1.put("CURRENCY", "EUR");
        payment1.put("STATUS", "PENDING");
        payment1.put("CREATED_DATE", new Date());
        payments.add(payment1);

        Map<String, Object> payment2 = new HashMap<>();
        payment2.put("PAYMENT_ID", "PMT002");
        payment2.put("CUST_NAME", "Alice Brown");
        payment2.put("BEN_NAME", "Bob Wilson");
        payment2.put("AMOUNT", "2500.00");
        payment2.put("CURRENCY", "USD");
        payment2.put("STATUS", "COMPLETED");
        payment2.put("CREATED_DATE", new Date(System.currentTimeMillis() - 86400000));
        payments.add(payment2);

        return PaymentSearchResponse.builder()
                .payments(payments)
                .pmtClass(filters.getPmtClass() != null ? filters.getPmtClass() : "STANDARD")
                .build();
    }

    @Override
    public PaymentDetailsResponse getPaymentDetails(String paymentId) {
        logger.debug("[MOCK] getPaymentDetails({})", paymentId);

        return PaymentDetailsResponse.builder()
                .userName("John Doe")
                .userId("USER001")
                .officerName("Officer Smith")
                .goldManager("Manager Brown")
                .custName("Acme Corporation")
                .custAccount("ACC123456")
                .benName("Beneficiary Corp")
                .benId("BEN001")
                .benRes("LV")
                .benCity("Riga")
                .benStreet("Main St 123")
                .benAcnt("BEN-ACC-789")
                .benType("CORP")
                .ordName("John Doe")
                .ordId("ORD001")
                .ordRes("LV")
                .ordAcnt("ORD-ACC-456")
                .benBankName("Beneficiary Bank")
                .benBankBranch("Main Branch")
                .benBankSwiftCode("BENBLVXX")
                .benBankOtherCode("123456")
                .benBankAddr("Bank Street 1")
                .imBankName("Intermediary Bank")
                .imBankAcnt("IM-ACC-001")
                .imBankSwiftCode("IMBNLVXX")
                .imBankOtherCode("654321")
                .imBankAddr("Int Bank Ave 5")
                .paymentDetails("Payment for services")
                .itb("ITB001")
                .itc("ITC001")
                .epc("EPC001")
                .itd("ITD001")
                .exchangeRate("1.0")
                .comType("SHA")
                .typeId(1)
                .eCheque("N")
                .eExpiry(new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000))
                .signTime(new Date())
                .signRSA("RSA_SIGNATURE_MOCK")
                .templateName("Standard Transfer")
                .globusFt("FT123456")
                .bookingDate(new Date())
                .execDate(new Date())
                .taxPayerId("TAX123")
                .isTaxDoc(0)
                .isUtPayment(0)
                .location("RIGA")
                .sector(1)
                .segment("CORPORATE")
                .benMobile("+371 2000 0000")
                .build();
    }

    @Override
    public void changeTemplateGroup(String paymentId, String groupId) {
        logger.debug("[MOCK] changeTemplateGroup({}, {})", paymentId, groupId);
        // Mock - no operation needed
    }
}

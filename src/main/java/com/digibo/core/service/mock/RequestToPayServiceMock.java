package com.digibo.core.service.mock;

import com.digibo.core.service.RequestToPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * RequestToPayServiceMock - Mock implementation of RequestToPayService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class RequestToPayServiceMock implements RequestToPayService {

    private static final Logger logger = LoggerFactory.getLogger(RequestToPayServiceMock.class);

    private static final List<Map<String, Object>> mockRequestToPayRecords = new ArrayList<>();

    static {
        Map<String, Object> rtp1 = new HashMap<>();
        rtp1.put("PAYMENT_ID", "RTP001");
        rtp1.put("CB_PAYMENT_ID", "CB001");
        rtp1.put("CUST_ID", "CUST001");
        rtp1.put("CUST_NAME", "Test Customer 1");
        rtp1.put("USER_LOGIN", "jdoe");
        rtp1.put("OFFICER_ID", 1001L);
        rtp1.put("BEN_NAME", "Beneficiary 1");
        rtp1.put("FROM_CONTRACT", "LV00TEST0000000000001");
        rtp1.put("FROM_LOCATION", "LV");
        rtp1.put("PMT_DETAILS", "Invoice payment");
        rtp1.put("AMOUNT", new BigDecimal("500.00"));
        rtp1.put("CURRENCY", "EUR");
        rtp1.put("PMT_CLASS", 10);
        rtp1.put("EFFECT_DATE", new Date());
        rtp1.put("CHANNEL_ID", 5);
        rtp1.put("STATUS_ID", 1);
        rtp1.put("CREATED_DATE", new Date());
        mockRequestToPayRecords.add(rtp1);

        Map<String, Object> rtp2 = new HashMap<>();
        rtp2.put("PAYMENT_ID", "RTP002");
        rtp2.put("CB_PAYMENT_ID", "CB002");
        rtp2.put("CUST_ID", "CUST002");
        rtp2.put("CUST_NAME", "Test Customer 2");
        rtp2.put("USER_LOGIN", "jsmith");
        rtp2.put("OFFICER_ID", 1002L);
        rtp2.put("BEN_NAME", "Beneficiary 2");
        rtp2.put("FROM_CONTRACT", "LV00TEST0000000000002");
        rtp2.put("FROM_LOCATION", "LV");
        rtp2.put("PMT_DETAILS", "Service payment");
        rtp2.put("AMOUNT", new BigDecimal("1500.00"));
        rtp2.put("CURRENCY", "USD");
        rtp2.put("PMT_CLASS", 20);
        rtp2.put("EFFECT_DATE", new Date());
        rtp2.put("CHANNEL_ID", 28);
        rtp2.put("STATUS_ID", 5);
        rtp2.put("CREATED_DATE", new Date(System.currentTimeMillis() - 1L * 24 * 60 * 60 * 1000));
        mockRequestToPayRecords.add(rtp2);
    }

    @Override
    public List<Map<String, Object>> find(String custId, String custName, String userLogin, Long officerId,
                                           String benName, String fromContract, String fromLocation, String pmtDetails,
                                           String amountFrom, String amountTill, String currencies, String pmtClass,
                                           Date effectFrom, Date effectTill,
                                           String paymentId, String cbPaymentId, String channels, String statuses,
                                           Date createdFrom, Date createdTill) {
        logger.debug("[MOCK] BORequestToPay.find() called");

        List<Map<String, Object>> results = new ArrayList<>(mockRequestToPayRecords);

        // Apply remitter filters
        if (custId != null && !custId.isEmpty()) {
            results.removeIf(r -> !custId.equals(r.get("CUST_ID")));
        }

        if (custName != null && !custName.isEmpty()) {
            String nameLower = custName.toLowerCase();
            results.removeIf(r -> {
                String name = (String) r.get("CUST_NAME");
                return name == null || !name.toLowerCase().contains(nameLower);
            });
        }

        if (userLogin != null && !userLogin.isEmpty()) {
            String loginLower = userLogin.toLowerCase();
            results.removeIf(r -> {
                String login = (String) r.get("USER_LOGIN");
                return login == null || !login.toLowerCase().contains(loginLower);
            });
        }

        if (officerId != null) {
            results.removeIf(r -> !officerId.equals(r.get("OFFICER_ID")));
        }

        // Apply payment filters
        if (benName != null && !benName.isEmpty()) {
            String benNameLower = benName.toLowerCase();
            results.removeIf(r -> {
                String name = (String) r.get("BEN_NAME");
                return name == null || !name.toLowerCase().contains(benNameLower);
            });
        }

        if (fromContract != null && !fromContract.isEmpty()) {
            results.removeIf(r -> {
                String contract = (String) r.get("FROM_CONTRACT");
                return contract == null || !contract.contains(fromContract);
            });
        }

        if (fromLocation != null && !fromLocation.isEmpty()) {
            results.removeIf(r -> !fromLocation.equals(r.get("FROM_LOCATION")));
        }

        if (pmtDetails != null && !pmtDetails.isEmpty()) {
            String detailsLower = pmtDetails.toLowerCase();
            results.removeIf(r -> {
                String details = (String) r.get("PMT_DETAILS");
                return details == null || !details.toLowerCase().contains(detailsLower);
            });
        }

        if (amountFrom != null && !amountFrom.isEmpty()) {
            BigDecimal amtFrom = new BigDecimal(amountFrom);
            results.removeIf(r -> {
                BigDecimal amt = (BigDecimal) r.get("AMOUNT");
                return amt == null || amt.compareTo(amtFrom) < 0;
            });
        }

        if (amountTill != null && !amountTill.isEmpty()) {
            BigDecimal amtTill = new BigDecimal(amountTill);
            results.removeIf(r -> {
                BigDecimal amt = (BigDecimal) r.get("AMOUNT");
                return amt == null || amt.compareTo(amtTill) > 0;
            });
        }

        if (currencies != null && !currencies.isEmpty()) {
            List<String> currencyList = Arrays.stream(currencies.split(","))
                    .map(String::trim)
                    .toList();
            results.removeIf(r -> !currencyList.contains(r.get("CURRENCY")));
        }

        if (pmtClass != null && !pmtClass.isEmpty()) {
            List<Integer> classList = Arrays.stream(pmtClass.split(","))
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .toList();
            results.removeIf(r -> !classList.contains((Integer) r.get("PMT_CLASS")));
        }

        // Apply system filters
        if (paymentId != null && !paymentId.isEmpty()) {
            results.removeIf(r -> !paymentId.equals(r.get("PAYMENT_ID")));
        }

        if (cbPaymentId != null && !cbPaymentId.isEmpty()) {
            results.removeIf(r -> !cbPaymentId.equals(r.get("CB_PAYMENT_ID")));
        }

        if (channels != null && !channels.isEmpty()) {
            List<Integer> channelList = Arrays.stream(channels.split(","))
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .toList();
            results.removeIf(r -> !channelList.contains((Integer) r.get("CHANNEL_ID")));
        }

        if (statuses != null && !statuses.isEmpty()) {
            List<Integer> statusList = Arrays.stream(statuses.split(","))
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .toList();
            results.removeIf(r -> !statusList.contains((Integer) r.get("STATUS_ID")));
        }

        if (createdFrom != null) {
            results.removeIf(r -> {
                Date created = (Date) r.get("CREATED_DATE");
                return created == null || created.before(createdFrom);
            });
        }

        if (createdTill != null) {
            results.removeIf(r -> {
                Date created = (Date) r.get("CREATED_DATE");
                return created == null || created.after(createdTill);
            });
        }

        logger.debug("[MOCK] Returning {} filtered Request to Pay records", results.size());
        return results;
    }
}

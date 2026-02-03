package com.digibo.core.service.mock;

import com.digibo.core.service.SmsAgreementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * SmsAgreementServiceMock - Mock implementation of SmsAgreementService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class SmsAgreementServiceMock implements SmsAgreementService {

    private static final Logger logger = LoggerFactory.getLogger(SmsAgreementServiceMock.class);

    @Override
    public List<Map<String, Object>> getOperators() {
        logger.debug("[MOCK] getOperators()");

        List<Map<String, Object>> operators = new ArrayList<>();

        Map<String, Object> op1 = new HashMap<>();
        op1.put("OPERATOR_ID", 1L);
        op1.put("OPERATOR_NAME", "Operator A");
        op1.put("CODE", "OPA");
        operators.add(op1);

        Map<String, Object> op2 = new HashMap<>();
        op2.put("OPERATOR_ID", 2L);
        op2.put("OPERATOR_NAME", "Operator B");
        op2.put("CODE", "OPB");
        operators.add(op2);

        return operators;
    }

    @Override
    public List<Map<String, Object>> getAccounts(String custId, String location) {
        logger.debug("[MOCK] getAccounts({}, {})", custId, location);

        List<Map<String, Object>> accounts = new ArrayList<>();

        Map<String, Object> acc1 = new HashMap<>();
        acc1.put("ACCOUNT_ID", "ACC001");
        acc1.put("ACCOUNT_NAME", "Main Account");
        acc1.put("CURRENCY", "USD");
        acc1.put("BALANCE", 10000.00);
        accounts.add(acc1);

        Map<String, Object> acc2 = new HashMap<>();
        acc2.put("ACCOUNT_ID", "ACC002");
        acc2.put("ACCOUNT_NAME", "Savings Account");
        acc2.put("CURRENCY", "EUR");
        acc2.put("BALANCE", 5000.00);
        accounts.add(acc2);

        return accounts;
    }

    @Override
    public List<Map<String, Object>> getLogins(Long userId, Long custId, String location) {
        logger.debug("[MOCK] getLogins({}, {}, {})", userId, custId, location);

        List<Map<String, Object>> logins = new ArrayList<>();

        Map<String, Object> login1 = new HashMap<>();
        login1.put("LOGIN_ID", 1L);
        login1.put("LOGIN_NAME", "user1");
        login1.put("STATUS", "ACTIVE");
        logins.add(login1);

        return logins;
    }

    @Override
    public List<Map<String, Object>> loadRights1(Long wocId, Long custId, String location) {
        logger.debug("[MOCK] loadRights1({}, {}, {})", wocId, custId, location);

        List<Map<String, Object>> rights = new ArrayList<>();

        Map<String, Object> right1 = new HashMap<>();
        right1.put("RIGHT_ID", 1L);
        right1.put("RIGHT_NAME", "VIEW_BALANCE");
        right1.put("ENABLED", 1);
        rights.add(right1);

        Map<String, Object> right2 = new HashMap<>();
        right2.put("RIGHT_ID", 2L);
        right2.put("RIGHT_NAME", "TRANSFER");
        right2.put("ENABLED", 1);
        rights.add(right2);

        return rights;
    }

    @Override
    public List<Map<String, Object>> loadRights2(Long wocId, Long custId, String location) {
        logger.debug("[MOCK] loadRights2({}, {}, {})", wocId, custId, location);

        List<Map<String, Object>> rights = new ArrayList<>();

        Map<String, Object> right1 = new HashMap<>();
        right1.put("RIGHT_ID", 10L);
        right1.put("RIGHT_NAME", "INTERNATIONAL_TRANSFER");
        right1.put("ENABLED", 1);
        rights.add(right1);

        return rights;
    }

    @Override
    public List<Map<String, Object>> loadCardRights(Long wocId, Long custId, String location) {
        logger.debug("[MOCK] loadCardRights({}, {}, {})", wocId, custId, location);

        List<Map<String, Object>> rights = new ArrayList<>();

        Map<String, Object> right1 = new HashMap<>();
        right1.put("CARD_ID", "CARD001");
        right1.put("CARD_TYPE", "VISA");
        right1.put("RIGHT_LEVEL", 3);
        rights.add(right1);

        return rights;
    }

    @Override
    public int checkLogin(String login) {
        logger.debug("[MOCK] checkLogin({})", login);
        return "validuser".equals(login) ? 0 : 1;
    }

    @Override
    public int getLoginCount(String login) {
        logger.debug("[MOCK] getLoginCount({})", login);
        return "existinguser".equals(login) ? 1 : 0;
    }

    @Override
    public int loginForCustomerExists(Long wocId, Long custId, String location, String login) {
        logger.debug("[MOCK] loginForCustomerExists({}, {}, {}, {})", wocId, custId, location, login);
        return "existinglogin".equals(login) ? 1 : 0;
    }

    @Override
    public Map<String, Object> loadChannel(String wocId) {
        logger.debug("[MOCK] loadChannel({})", wocId);

        Map<String, Object> channel = new HashMap<>();
        channel.put("wocId", wocId);
        channel.put("login", "smsuser");
        channel.put("operator", 1);
        channel.put("chargesAcc", 12345);
        channel.put("parentId", "PARENT001");
        channel.put("language", 1);
        channel.put("sellerId", 100);
        channel.put("distribCenterId", 200);
        channel.put("ffSMS", 1);
        channel.put("password", "***");
        channel.put("hasDefault", 1);
        channel.put("smsTime", "09:00-18:00");

        return channel;
    }
}

package com.digibo.core.service.impl;

import com.digibo.core.service.SmsAgreementService;
import com.digibo.core.service.base.BaseService;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SmsAgreementServiceImpl - Real implementation of SmsAgreementService
 * Calls BOSMSAgreement Oracle package procedures
 */
@Service
@Profile("!mock")
public class SmsAgreementServiceImpl extends BaseService implements SmsAgreementService {

    public SmsAgreementServiceImpl() {
        super("BOSMSAgreement");
    }

    @Override
    public List<Map<String, Object>> getOperators() {
        logger.debug("Calling BOSMSAgreement.get_operators()");

        return executeCursorProcedure("get_operators", List.of(), Map.of(), "P_CURSOR",
                (rs, rowNum) -> {
                    Map<String, Object> row = new HashMap<>();
                    int colCount = rs.getMetaData().getColumnCount();
                    for (int i = 1; i <= colCount; i++) {
                        String colName = rs.getMetaData().getColumnName(i);
                        row.put(colName, rs.getObject(i));
                    }
                    return row;
                });
    }

    @Override
    public List<Map<String, Object>> getAccounts(String custId, String location) {
        logger.debug("Calling BOSMSAgreement.get_accounts({}, {})", custId, location);

        List<SqlParameter> params = List.of(
                inParam("P_CUST_ID", Types.VARCHAR),
                inParam("P_LOCATION", Types.VARCHAR)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_CUST_ID", custId);
        inputParams.put("P_LOCATION", location);

        return executeCursorProcedure("get_accounts", params, inputParams, "P_CURSOR",
                (rs, rowNum) -> {
                    Map<String, Object> row = new HashMap<>();
                    int colCount = rs.getMetaData().getColumnCount();
                    for (int i = 1; i <= colCount; i++) {
                        String colName = rs.getMetaData().getColumnName(i);
                        row.put(colName, rs.getObject(i));
                    }
                    return row;
                });
    }

    @Override
    public List<Map<String, Object>> getLogins(Long userId, Long custId, String location) {
        logger.debug("Calling BOSMSAgreement.get_logins({}, {}, {})", userId, custId, location);

        List<SqlParameter> params = List.of(
                inParam("P_USER_ID", Types.NUMERIC),
                inParam("P_CUST_ID", Types.NUMERIC),
                inParam("P_LOCATION", Types.VARCHAR)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_USER_ID", userId);
        inputParams.put("P_CUST_ID", custId);
        inputParams.put("P_LOCATION", location);

        return executeCursorProcedure("get_logins", params, inputParams, "P_CURSOR",
                (rs, rowNum) -> {
                    Map<String, Object> row = new HashMap<>();
                    int colCount = rs.getMetaData().getColumnCount();
                    for (int i = 1; i <= colCount; i++) {
                        String colName = rs.getMetaData().getColumnName(i);
                        row.put(colName, rs.getObject(i));
                    }
                    return row;
                });
    }

    @Override
    public List<Map<String, Object>> loadRights1(Long wocId, Long custId, String location) {
        logger.debug("Calling BOSMSAgreement.load_rights_1({}, {}, {})", wocId, custId, location);

        List<SqlParameter> params = List.of(
                inParam("P_WOC_ID", Types.NUMERIC),
                inParam("P_CUST_ID", Types.NUMERIC),
                inParam("P_LOCATION", Types.VARCHAR)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_WOC_ID", wocId);
        inputParams.put("P_CUST_ID", custId);
        inputParams.put("P_LOCATION", location);

        return executeCursorProcedure("load_rights_1", params, inputParams, "P_CURSOR",
                (rs, rowNum) -> {
                    Map<String, Object> row = new HashMap<>();
                    int colCount = rs.getMetaData().getColumnCount();
                    for (int i = 1; i <= colCount; i++) {
                        String colName = rs.getMetaData().getColumnName(i);
                        row.put(colName, rs.getObject(i));
                    }
                    return row;
                });
    }

    @Override
    public List<Map<String, Object>> loadRights2(Long wocId, Long custId, String location) {
        logger.debug("Calling BOSMSAgreement.load_rights_2({}, {}, {})", wocId, custId, location);

        List<SqlParameter> params = List.of(
                inParam("P_WOC_ID", Types.NUMERIC),
                inParam("P_CUST_ID", Types.NUMERIC),
                inParam("P_LOCATION", Types.VARCHAR)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_WOC_ID", wocId);
        inputParams.put("P_CUST_ID", custId);
        inputParams.put("P_LOCATION", location);

        return executeCursorProcedure("load_rights_2", params, inputParams, "P_CURSOR",
                (rs, rowNum) -> {
                    Map<String, Object> row = new HashMap<>();
                    int colCount = rs.getMetaData().getColumnCount();
                    for (int i = 1; i <= colCount; i++) {
                        String colName = rs.getMetaData().getColumnName(i);
                        row.put(colName, rs.getObject(i));
                    }
                    return row;
                });
    }

    @Override
    public List<Map<String, Object>> loadCardRights(Long wocId, Long custId, String location) {
        logger.debug("Calling BOSMSAgreement.load_card_rights({}, {}, {})", wocId, custId, location);

        List<SqlParameter> params = List.of(
                inParam("P_WOC_ID", Types.NUMERIC),
                inParam("P_CUST_ID", Types.NUMERIC),
                inParam("P_LOCATION", Types.VARCHAR)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_WOC_ID", wocId);
        inputParams.put("P_CUST_ID", custId);
        inputParams.put("P_LOCATION", location);

        return executeCursorProcedure("load_card_rights", params, inputParams, "P_CURSOR",
                (rs, rowNum) -> {
                    Map<String, Object> row = new HashMap<>();
                    int colCount = rs.getMetaData().getColumnCount();
                    for (int i = 1; i <= colCount; i++) {
                        String colName = rs.getMetaData().getColumnName(i);
                        row.put(colName, rs.getObject(i));
                    }
                    return row;
                });
    }

    @Override
    public int checkLogin(String login) {
        logger.debug("Calling BOSMSAgreement.check_login({})", login);

        List<SqlParameter> params = List.of(
                inParam("P_LOGIN", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_LOGIN", login);

        Integer result = executeScalarFunction("check_login", params, inputParams, Types.INTEGER);
        return result != null ? result : -1;
    }

    @Override
    public int getLoginCount(String login) {
        logger.debug("Calling BOSMSAgreement.get_login_count({})", login);

        List<SqlParameter> params = List.of(
                inParam("P_LOGIN", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_LOGIN", login);

        Integer result = executeScalarFunction("get_login_count", params, inputParams, Types.INTEGER);
        return result != null ? result : 0;
    }

    @Override
    public int loginForCustomerExists(Long wocId, Long custId, String location, String login) {
        logger.debug("Calling BOSMSAgreement.login_for_customer_exists({}, {}, {}, {})", wocId, custId, location, login);

        List<SqlParameter> params = List.of(
                inParam("P_WOC_ID", Types.NUMERIC),
                inParam("P_CUST_ID", Types.NUMERIC),
                inParam("P_LOCATION", Types.VARCHAR),
                inParam("P_LOGIN", Types.VARCHAR)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_WOC_ID", wocId);
        inputParams.put("P_CUST_ID", custId);
        inputParams.put("P_LOCATION", location);
        inputParams.put("P_LOGIN", login);

        Integer result = executeScalarFunction("login_for_customer_exists", params, inputParams, Types.INTEGER);
        return result != null ? result : 0;
    }

    @Override
    public Map<String, Object> loadChannel(String wocId) {
        logger.debug("Calling BOSMSAgreement.load_channel({})", wocId);

        List<SqlParameter> inParams = List.of(
                inParam("P_WOC_ID", Types.VARCHAR)
        );

        List<SqlOutParameter> outParams = List.of(
                outParam("P_LOGIN", Types.VARCHAR),
                outParam("P_OPERATOR", Types.NUMERIC),
                outParam("P_CHARGES_ACC", Types.NUMERIC),
                outParam("P_PARENT_ID", Types.VARCHAR),
                outParam("P_LANGUAGE", Types.NUMERIC),
                outParam("P_SELLER_ID", Types.NUMERIC),
                outParam("P_DISTRIB_CENTER_ID", Types.NUMERIC),
                outParam("P_FF_SMS", Types.NUMERIC),
                outParam("P_PASSWORD", Types.VARCHAR),
                outParam("P_HAS_DEFAULT", Types.NUMERIC),
                outParam("P_SMS_TIME", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_WOC_ID", wocId);

        Map<String, Object> result = executeProcedureWithOutputs("load_channel", inParams, outParams, inputParams);

        Map<String, Object> channel = new HashMap<>();
        channel.put("wocId", wocId);
        channel.put("login", result.get("P_LOGIN"));
        channel.put("operator", toInteger(result.get("P_OPERATOR")));
        channel.put("chargesAcc", toInteger(result.get("P_CHARGES_ACC")));
        channel.put("parentId", result.get("P_PARENT_ID"));
        channel.put("language", toInteger(result.get("P_LANGUAGE")));
        channel.put("sellerId", toInteger(result.get("P_SELLER_ID")));
        channel.put("distribCenterId", toInteger(result.get("P_DISTRIB_CENTER_ID")));
        channel.put("ffSMS", toInteger(result.get("P_FF_SMS")));
        channel.put("password", result.get("P_PASSWORD"));
        channel.put("hasDefault", toInteger(result.get("P_HAS_DEFAULT")));
        channel.put("smsTime", result.get("P_SMS_TIME"));

        return channel;
    }

    private Integer toInteger(Object value) {
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).intValue();
        return Integer.parseInt(value.toString());
    }
}

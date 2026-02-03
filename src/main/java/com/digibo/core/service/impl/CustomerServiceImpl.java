package com.digibo.core.service.impl;

import com.digibo.core.dto.response.ChannelResponse;
import com.digibo.core.dto.response.UserResponse;
import com.digibo.core.service.CustomerService;
import com.digibo.core.service.base.BaseService;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CustomerServiceImpl - Real implementation of CustomerService
 * Calls BOCustomer Oracle package procedures
 */
@Service
@Profile("!mock")
public class CustomerServiceImpl extends BaseService implements CustomerService {

    public CustomerServiceImpl() {
        super("BOCustomer");
    }

    @Override
    public int customerExists(String id) {
        logger.debug("Calling BOCustomer.customer_exists({})", id);

        List<SqlParameter> params = List.of(
                inParam("P_ID", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_ID", id);

        Integer result = executeScalarFunction("customer_exists", params, inputParams, Types.INTEGER);
        return result != null ? result : 0;
    }

    @Override
    public List<Map<String, Object>> loadUserChannels(String id) {
        logger.debug("Calling BOCustomer.load_user_channels({})", id);

        List<SqlParameter> params = List.of(
                inParam("P_ID", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_ID", id);

        return executeCursorProcedure("load_user_channels", params, inputParams, "P_CURSOR",
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
    public List<Map<String, Object>> loadUserInfo(Long id) {
        logger.debug("Calling BOCustomer.load_user_info({})", id);

        List<SqlParameter> params = List.of(
                inParam("P_ID", Types.NUMERIC)
        );

        Map<String, Object> inputParams = Map.of("P_ID", id);

        return executeCursorProcedure("load_user_info", params, inputParams, "P_CURSOR",
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
    public List<Map<String, Object>> loadUserHistory(Long id) {
        logger.debug("Calling BOCustomer.load_user_history({})", id);

        List<SqlParameter> params = List.of(
                inParam("P_ID", Types.NUMERIC)
        );

        Map<String, Object> inputParams = Map.of("P_ID", id);

        return executeCursorProcedure("load_user_history", params, inputParams, "P_CURSOR",
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
    public List<Map<String, Object>> loadCustomerTree(String custId, String location) {
        logger.debug("Calling BOCustomer.load_customer_tree({}, {})", custId, location);

        List<SqlParameter> params = List.of(
                inParam("P_CUST_ID", Types.VARCHAR),
                inParam("P_LOCATION", Types.VARCHAR)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_CUST_ID", custId);
        inputParams.put("P_LOCATION", location);

        return executeCursorProcedure("load_customer_tree", params, inputParams, "P_CURSOR",
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
    public List<Map<String, Object>> loadLicenses(String custId) {
        logger.debug("Calling BOCustomer.load_licenses({})", custId);

        List<SqlParameter> params = List.of(
                inParam("P_CUST_ID", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_CUST_ID", custId);

        return executeCursorProcedure("load_licenses", params, inputParams, "P_CURSOR",
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
    public int checkLicense(String id) {
        logger.debug("Calling BOCustomer.check_license({})", id);

        List<SqlParameter> params = List.of(
                inParam("P_ID", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_ID", id);

        Integer result = executeScalarFunction("check_license", params, inputParams, Types.INTEGER);
        return result != null ? result : 0;
    }

    @Override
    public int checkLogin(Long userId, String login, String license, Long channelId) {
        logger.debug("Calling BOCustomer.check_login({}, {}, {}, {})", userId, login, license, channelId);

        List<SqlParameter> params = List.of(
                inParam("P_USER_ID", Types.NUMERIC),
                inParam("P_LOGIN", Types.VARCHAR),
                inParam("P_LICENSE", Types.VARCHAR),
                inParam("P_CHANNEL_ID", Types.NUMERIC)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_USER_ID", userId);
        inputParams.put("P_LOGIN", login);
        inputParams.put("P_LICENSE", license);
        inputParams.put("P_CHANNEL_ID", channelId);

        Integer result = executeScalarFunction("check_login", params, inputParams, Types.INTEGER);
        return result != null ? result : -1;
    }

    @Override
    public List<Map<String, Object>> loadUsers(String custId, Long channel, String license, String location) {
        logger.debug("Calling BOCustomer.load_users({}, {}, {}, {})", custId, channel, license, location);

        List<SqlParameter> params = List.of(
                inParam("P_CUST_ID", Types.VARCHAR),
                inParam("P_CHANNEL", Types.NUMERIC),
                inParam("P_LICENSE", Types.VARCHAR),
                inParam("P_LOCATION", Types.VARCHAR)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_CUST_ID", custId);
        inputParams.put("P_CHANNEL", channel);
        inputParams.put("P_LICENSE", license);
        inputParams.put("P_LOCATION", location);

        return executeCursorProcedure("load_users", params, inputParams, "P_CURSOR",
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
    public UserResponse loadUser(Long id) {
        logger.debug("Calling BOCustomer.load_user({})", id);

        List<SqlParameter> inParams = List.of(
                inParam("P_ID", Types.NUMERIC)
        );

        List<SqlOutParameter> outParams = List.of(
                outParam("P_NAME", Types.VARCHAR),
                outParam("P_ISSUER_COUNTRY", Types.VARCHAR),
                outParam("P_PERSONAL_ID", Types.VARCHAR),
                outParam("P_PASSPORT_NO", Types.VARCHAR),
                outParam("P_STREET", Types.VARCHAR),
                outParam("P_CITY", Types.VARCHAR),
                outParam("P_COUNTRY", Types.VARCHAR),
                outParam("P_ZIP", Types.VARCHAR),
                outParam("P_PHONE", Types.VARCHAR),
                outParam("P_MOBILE", Types.VARCHAR),
                outParam("P_FAX", Types.VARCHAR),
                outParam("P_EMAIL", Types.VARCHAR),
                outParam("P_APART", Types.VARCHAR),
                outParam("P_HOUSE", Types.VARCHAR),
                outParam("P_STD_Q", Types.NUMERIC),
                outParam("P_SPEC_Q", Types.VARCHAR),
                outParam("P_ANSWER", Types.VARCHAR),
                outParam("P_REG_DATE", Types.DATE),
                outParam("P_CHANGE_DATE", Types.DATE),
                outParam("P_CHANGE_OFFICER_ID", Types.VARCHAR),
                outParam("P_CHANGE_LOGIN", Types.VARCHAR),
                outParam("P_CUSTOMER_ID", Types.NUMERIC),
                outParam("P_MIGR_STATUS", Types.NUMERIC),
                outParam("P_HAS_AGREEMENT_IN_GLOBUS", Types.NUMERIC)
        );

        Map<String, Object> inputParams = Map.of("P_ID", id);

        Map<String, Object> result = executeProcedureWithOutputs("load_user", inParams, outParams, inputParams);

        return UserResponse.builder()
                .id(id)
                .name((String) result.get("P_NAME"))
                .issuerCountry((String) result.get("P_ISSUER_COUNTRY"))
                .personalId((String) result.get("P_PERSONAL_ID"))
                .passportNo((String) result.get("P_PASSPORT_NO"))
                .street((String) result.get("P_STREET"))
                .city((String) result.get("P_CITY"))
                .country((String) result.get("P_COUNTRY"))
                .zip((String) result.get("P_ZIP"))
                .phone((String) result.get("P_PHONE"))
                .mobile((String) result.get("P_MOBILE"))
                .fax((String) result.get("P_FAX"))
                .email((String) result.get("P_EMAIL"))
                .apart((String) result.get("P_APART"))
                .house((String) result.get("P_HOUSE"))
                .stdQ(toInteger(result.get("P_STD_Q")))
                .specQ((String) result.get("P_SPEC_Q"))
                .answer((String) result.get("P_ANSWER"))
                .regDate((Date) result.get("P_REG_DATE"))
                .changeDate((Date) result.get("P_CHANGE_DATE"))
                .changeOfficerId((String) result.get("P_CHANGE_OFFICER_ID"))
                .changeLogin((String) result.get("P_CHANGE_LOGIN"))
                .customerId(toLong(result.get("P_CUSTOMER_ID")))
                .migrStatus(toInteger(result.get("P_MIGR_STATUS")))
                .hasAgreementInGlobus(toInteger(result.get("P_HAS_AGREEMENT_IN_GLOBUS")))
                .build();
    }

    @Override
    public ChannelResponse loadChannel(String wocId, String custId) {
        logger.debug("Calling BOCustomer.load_channel({}, {})", wocId, custId);

        List<SqlParameter> inParams = List.of(
                inParam("P_WOC_ID", Types.VARCHAR),
                inParam("P_CUST_ID", Types.VARCHAR)
        );

        List<SqlOutParameter> outParams = List.of(
                outParam("P_CDEV_TYPE", Types.NUMERIC),
                outParam("P_CDEV_NUM", Types.VARCHAR),
                outParam("P_SELLER_ID", Types.NUMERIC),
                outParam("P_DISTRIB_CENTER_ID", Types.NUMERIC),
                outParam("P_LEVEL", Types.NUMERIC),
                outParam("P_TMP_LEVEL", Types.NUMERIC),
                outParam("P_CHANGE_OFFICER", Types.VARCHAR),
                outParam("P_SPEC_RATE", Types.NUMERIC),
                outParam("P_INFO2BANK", Types.NUMERIC),
                outParam("P_DF_ACCESS_RIGHT", Types.NUMERIC)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_WOC_ID", wocId);
        inputParams.put("P_CUST_ID", custId);

        Map<String, Object> result = executeProcedureWithOutputs("load_channel", inParams, outParams, inputParams);

        return ChannelResponse.builder()
                .wocId(wocId)
                .custId(custId)
                .cdevType(toInteger(result.get("P_CDEV_TYPE")))
                .cdevNum((String) result.get("P_CDEV_NUM"))
                .sellerId(toLong(result.get("P_SELLER_ID")))
                .distribCenterId(toLong(result.get("P_DISTRIB_CENTER_ID")))
                .level(toInteger(result.get("P_LEVEL")))
                .tmpLevel(toInteger(result.get("P_TMP_LEVEL")))
                .changeOfficer((String) result.get("P_CHANGE_OFFICER"))
                .specRate(toInteger(result.get("P_SPEC_RATE")))
                .info2Bank(toInteger(result.get("P_INFO2BANK")))
                .dfAccessRight(toInteger(result.get("P_DF_ACCESS_RIGHT")))
                .build();
    }

    private Integer toInteger(Object value) {
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).intValue();
        return Integer.parseInt(value.toString());
    }

    private Long toLong(Object value) {
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).longValue();
        return Long.parseLong(value.toString());
    }
}

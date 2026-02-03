package com.digibo.core.service.impl;

import com.digibo.core.service.BrokerService;
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
 * BrokerServiceImpl - Real implementation of BrokerService
 * Calls BOBroker Oracle package procedures
 */
@Service
@Profile("!mock")
public class BrokerServiceImpl extends BaseService implements BrokerService {

    public BrokerServiceImpl() {
        super("BOBroker");
    }

    @Override
    public List<Map<String, Object>> find(String custId, String custName, String userLogin, String userPassword,
                                          String docClass, String operationType, Integer docCount, String currencies,
                                          Date expiryFrom, Date expiryTill,
                                          String docId, String statuses, Date createdFrom, Date createdTill) {
        logger.debug("Calling BOBroker.find({}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {})",
                custId, custName, userLogin, userPassword, docClass, operationType, docCount, currencies,
                expiryFrom, expiryTill, docId, statuses, createdFrom, createdTill);

        List<SqlParameter> params = List.of(
                inParam("P_CUST_ID", Types.VARCHAR),
                inParam("P_CUST_NAME", Types.VARCHAR),
                inParam("P_USER_LOGIN", Types.VARCHAR),
                inParam("P_USER_PASSWORD", Types.VARCHAR),
                inParam("P_DOC_CLASS", Types.VARCHAR),
                inParam("P_OPERATION_TYPE", Types.VARCHAR),
                inParam("P_DOC_COUNT", Types.NUMERIC),
                inParam("P_CURRENCIES", Types.VARCHAR),
                inParam("P_EXPIRY_FROM", Types.DATE),
                inParam("P_EXPIRY_TILL", Types.DATE),
                inParam("P_DOC_ID", Types.VARCHAR),
                inParam("P_STATUSES", Types.VARCHAR),
                inParam("P_CREATED_FROM", Types.DATE),
                inParam("P_CREATED_TILL", Types.DATE)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_CUST_ID", custId);
        inputParams.put("P_CUST_NAME", custName);
        inputParams.put("P_USER_LOGIN", userLogin);
        inputParams.put("P_USER_PASSWORD", userPassword);
        inputParams.put("P_DOC_CLASS", docClass);
        inputParams.put("P_OPERATION_TYPE", operationType);
        inputParams.put("P_DOC_COUNT", docCount);
        inputParams.put("P_CURRENCIES", currencies);
        inputParams.put("P_EXPIRY_FROM", expiryFrom);
        inputParams.put("P_EXPIRY_TILL", expiryTill);
        inputParams.put("P_DOC_ID", docId);
        inputParams.put("P_STATUSES", statuses);
        inputParams.put("P_CREATED_FROM", createdFrom);
        inputParams.put("P_CREATED_TILL", createdTill);

        return executeCursorProcedure("find", params, inputParams, "P_CURSOR",
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
    public List<Map<String, Object>> findMy(String docClass) {
        logger.debug("Calling BOBroker.find_my({})", docClass);

        List<SqlParameter> params = List.of(
                inParam("P_DOC_CLASS", Types.VARCHAR)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_DOC_CLASS", docClass);

        return executeCursorProcedure("find_my", params, inputParams, "P_CURSOR",
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
    public Map<String, Object> broker(String docId) {
        logger.debug("Calling BOBroker.broker({})", docId);

        List<SqlParameter> inParams = List.of(
                inParam("P_ID", Types.VARCHAR)
        );

        List<SqlOutParameter> outParams = List.of(
                outParam("P_DOC_NO", Types.VARCHAR),
                outParam("P_USER_NAME", Types.VARCHAR),
                outParam("P_USER_ID", Types.VARCHAR),
                outParam("P_OFFICER_NAME", Types.VARCHAR),
                outParam("P_CUST_NAME", Types.VARCHAR),
                outParam("P_CUST_ACCOUNT", Types.VARCHAR),
                outParam("P_GLOBUS_NO", Types.VARCHAR),
                outParam("P_PORTFOLIO_ID", Types.VARCHAR),
                outParam("P_OPERATION", Types.VARCHAR),
                outParam("P_ISIN_CODE", Types.VARCHAR),
                outParam("P_FUND_NAME", Types.VARCHAR),
                outParam("P_INVEST_VOLUME", Types.VARCHAR),
                outParam("P_IDENT_CODE", Types.VARCHAR),
                outParam("P_STOCK_SYMBOL", Types.VARCHAR),
                outParam("P_OPTION_SYMBOL", Types.VARCHAR),
                outParam("P_EMITENT_NAME", Types.VARCHAR),
                outParam("P_EXCHANGE_NAME", Types.VARCHAR),
                outParam("P_OPTION_TYPE", Types.VARCHAR),
                outParam("P_OPTION_PREMIUM", Types.VARCHAR),
                outParam("P_MATURITY_DATE", Types.DATE),
                outParam("P_COUPON_RATE", Types.VARCHAR),
                outParam("P_CURRENCY", Types.VARCHAR),
                outParam("P_ORDER_TYPE", Types.VARCHAR),
                outParam("P_PRICE", Types.VARCHAR),
                outParam("P_STOP_PRICE", Types.VARCHAR),
                outParam("P_TRAIL_AMOUNT", Types.VARCHAR),
                outParam("P_GOOD_TILL", Types.VARCHAR),
                outParam("P_TEXT", Types.VARCHAR),
                outParam("P_LOCATION", Types.VARCHAR),
                outParam("P_ITC", Types.VARCHAR),
                outParam("P_ITB", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_ID", docId);

        Map<String, Object> result = executeProcedureWithOutputs("broker", inParams, outParams, inputParams);

        Map<String, Object> response = new HashMap<>();
        response.put("id", docId);
        response.put("docNo", result.get("P_DOC_NO"));
        response.put("userName", result.get("P_USER_NAME"));
        response.put("userId", result.get("P_USER_ID"));
        response.put("officerName", result.get("P_OFFICER_NAME"));
        response.put("custName", result.get("P_CUST_NAME"));
        response.put("custAccount", result.get("P_CUST_ACCOUNT"));
        response.put("globusNo", result.get("P_GLOBUS_NO"));
        response.put("portfolioId", result.get("P_PORTFOLIO_ID"));
        response.put("operation", result.get("P_OPERATION"));
        response.put("isinCode", result.get("P_ISIN_CODE"));
        response.put("fundName", result.get("P_FUND_NAME"));
        response.put("investVolume", result.get("P_INVEST_VOLUME"));
        response.put("identCode", result.get("P_IDENT_CODE"));
        response.put("stockSymbol", result.get("P_STOCK_SYMBOL"));
        response.put("optionSymbol", result.get("P_OPTION_SYMBOL"));
        response.put("emitentName", result.get("P_EMITENT_NAME"));
        response.put("exchangeName", result.get("P_EXCHANGE_NAME"));
        response.put("optionType", result.get("P_OPTION_TYPE"));
        response.put("optionPremium", result.get("P_OPTION_PREMIUM"));
        response.put("maturityDate", result.get("P_MATURITY_DATE"));
        response.put("couponRate", result.get("P_COUPON_RATE"));
        response.put("currency", result.get("P_CURRENCY"));
        response.put("orderType", result.get("P_ORDER_TYPE"));
        response.put("price", result.get("P_PRICE"));
        response.put("stopPrice", result.get("P_STOP_PRICE"));
        response.put("trailAmount", result.get("P_TRAIL_AMOUNT"));
        response.put("goodTill", result.get("P_GOOD_TILL"));
        response.put("text", result.get("P_TEXT"));
        response.put("location", result.get("P_LOCATION"));
        response.put("infoToCustomer", result.get("P_ITC"));
        response.put("infoToBank", result.get("P_ITB"));

        return response;
    }
}

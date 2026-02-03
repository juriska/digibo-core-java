package com.digibo.core.service.impl;

import com.digibo.core.service.RequestToPayService;
import com.digibo.core.service.base.BaseService;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.*;

/**
 * RequestToPayServiceImpl - Real implementation of RequestToPayService
 * Calls BORequestToPay Oracle package procedures
 */
@Service
@Profile("!mock")
public class RequestToPayServiceImpl extends BaseService implements RequestToPayService {

    public RequestToPayServiceImpl() {
        super("BORequestToPay");
    }

    @Override
    public List<Map<String, Object>> find(String custId, String custName, String userLogin, Long officerId,
                                           String benName, String fromContract, String fromLocation, String pmtDetails,
                                           String amountFrom, String amountTill, String currencies, String pmtClass,
                                           Date effectFrom, Date effectTill,
                                           String paymentId, String cbPaymentId, String channels, String statuses,
                                           Date createdFrom, Date createdTill) {
        logger.debug("Calling BORequestToPay.find()");

        List<SqlParameter> params = List.of(
                // Remitter filters
                inParam("P_CUST_ID", Types.VARCHAR),
                inParam("P_CUST_NAME", Types.VARCHAR),
                inParam("P_USER_LOGIN", Types.VARCHAR),
                inParam("P_OFFICER_ID", Types.NUMERIC),
                // Payment filters
                inParam("P_BEN_NAME", Types.VARCHAR),
                inParam("P_FROM_CONTRACT", Types.VARCHAR),
                inParam("P_FROM_LOCATION", Types.VARCHAR),
                inParam("P_PMT_DETAILS", Types.VARCHAR),
                inParam("P_AMOUNT_FROM", Types.VARCHAR),
                inParam("P_AMOUNT_TILL", Types.VARCHAR),
                inParam("P_CURRENCIES", Types.VARCHAR),
                inParam("P_PMT_CLASS", Types.VARCHAR),
                inParam("P_EFFECT_FROM", Types.DATE),
                inParam("P_EFFECT_TILL", Types.DATE),
                // System filters
                inParam("P_PAYMENT_ID", Types.VARCHAR),
                inParam("P_CB_PAYMENT_ID", Types.VARCHAR),
                inParam("P_CHANNELS", Types.VARCHAR),
                inParam("P_STATUSES", Types.VARCHAR),
                inParam("P_CREATED_FROM", Types.DATE),
                inParam("P_CREATED_TILL", Types.DATE)
        );

        Map<String, Object> inputParams = new HashMap<>();
        // Remitter filters
        inputParams.put("P_CUST_ID", custId);
        inputParams.put("P_CUST_NAME", custName);
        inputParams.put("P_USER_LOGIN", userLogin);
        inputParams.put("P_OFFICER_ID", officerId);
        // Payment filters
        inputParams.put("P_BEN_NAME", benName);
        inputParams.put("P_FROM_CONTRACT", fromContract);
        inputParams.put("P_FROM_LOCATION", fromLocation);
        inputParams.put("P_PMT_DETAILS", pmtDetails);
        inputParams.put("P_AMOUNT_FROM", amountFrom);
        inputParams.put("P_AMOUNT_TILL", amountTill);
        inputParams.put("P_CURRENCIES", currencies);
        inputParams.put("P_PMT_CLASS", pmtClass);
        inputParams.put("P_EFFECT_FROM", effectFrom);
        inputParams.put("P_EFFECT_TILL", effectTill);
        // System filters
        inputParams.put("P_PAYMENT_ID", paymentId);
        inputParams.put("P_CB_PAYMENT_ID", cbPaymentId);
        inputParams.put("P_CHANNELS", channels);
        inputParams.put("P_STATUSES", statuses);
        inputParams.put("P_CREATED_FROM", createdFrom);
        inputParams.put("P_CREATED_TILL", createdTill);

        return executeCursorProcedure("find", params, inputParams, "P_CURSOR",
                (rs, rowNum) -> mapRow(rs));
    }

    private Map<String, Object> mapRow(ResultSet rs) throws java.sql.SQLException {
        Map<String, Object> row = new HashMap<>();
        int colCount = rs.getMetaData().getColumnCount();
        for (int i = 1; i <= colCount; i++) {
            String colName = rs.getMetaData().getColumnName(i);
            row.put(colName, rs.getObject(i));
        }
        return row;
    }
}

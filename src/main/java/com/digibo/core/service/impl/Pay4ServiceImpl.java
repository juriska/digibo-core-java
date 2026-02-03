package com.digibo.core.service.impl;

import com.digibo.core.service.Pay4Service;
import com.digibo.core.service.base.BaseService;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.*;

/**
 * Pay4ServiceImpl - Real implementation of Pay4Service
 * Calls BOPayment Oracle package procedures
 */
@Service
@Profile("!mock")
public class Pay4ServiceImpl extends BaseService implements Pay4Service {

    public Pay4ServiceImpl() {
        super("BOPayment");
    }

    @Override
    public List<Map<String, Object>> find(String custId, String custName, String userLogin, Long officerId,
                                           String benName, String pmtDetails, String fromContract,
                                           String amountFrom, String amountTill, String currencies, String pmtClass,
                                           Date effectFrom, Date effectTill,
                                           String paymentId, String channels, String statuses,
                                           Date createdFrom, Date createdTill) {
        logger.debug("Calling BOPayment.find()");

        List<SqlParameter> params = List.of(
                inParam("P_CUST_ID", Types.VARCHAR),
                inParam("P_CUST_NAME", Types.VARCHAR),
                inParam("P_USER_LOGIN", Types.VARCHAR),
                inParam("P_OFFICER_ID", Types.NUMERIC),
                inParam("P_BEN_NAME", Types.VARCHAR),
                inParam("P_PMT_DETAILS", Types.VARCHAR),
                inParam("P_FROM_CONTRACT", Types.VARCHAR),
                inParam("P_AMOUNT_FROM", Types.VARCHAR),
                inParam("P_AMOUNT_TILL", Types.VARCHAR),
                inParam("P_CURRENCIES", Types.VARCHAR),
                inParam("P_PMT_CLASS", Types.VARCHAR),
                inParam("P_EFFECT_FROM", Types.DATE),
                inParam("P_EFFECT_TILL", Types.DATE),
                inParam("P_PAYMENT_ID", Types.VARCHAR),
                inParam("P_CHANNELS", Types.VARCHAR),
                inParam("P_STATUSES", Types.VARCHAR),
                inParam("P_CREATED_FROM", Types.DATE),
                inParam("P_CREATED_TILL", Types.DATE)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_CUST_ID", custId);
        inputParams.put("P_CUST_NAME", custName);
        inputParams.put("P_USER_LOGIN", userLogin);
        inputParams.put("P_OFFICER_ID", officerId);
        inputParams.put("P_BEN_NAME", benName);
        inputParams.put("P_PMT_DETAILS", pmtDetails);
        inputParams.put("P_FROM_CONTRACT", fromContract);
        inputParams.put("P_AMOUNT_FROM", amountFrom);
        inputParams.put("P_AMOUNT_TILL", amountTill);
        inputParams.put("P_CURRENCIES", currencies);
        inputParams.put("P_PMT_CLASS", pmtClass);
        inputParams.put("P_EFFECT_FROM", effectFrom);
        inputParams.put("P_EFFECT_TILL", effectTill);
        inputParams.put("P_PAYMENT_ID", paymentId);
        inputParams.put("P_CHANNELS", channels);
        inputParams.put("P_STATUSES", statuses);
        inputParams.put("P_CREATED_FROM", createdFrom);
        inputParams.put("P_CREATED_TILL", createdTill);

        return executeCursorProcedure("find", params, inputParams, "P_CURSOR",
                (rs, rowNum) -> mapRow(rs));
    }

    @Override
    public Map<String, Object> payment(String paymentId) {
        logger.debug("Calling BOPayment.payment({})", paymentId);

        List<SqlParameter> inParams = List.of(
                inParam("P_ID", Types.VARCHAR)
        );

        List<SqlOutParameter> outParams = List.of(
                outParam("P_USER_NAME", Types.VARCHAR),
                outParam("P_USER_ID", Types.VARCHAR),
                outParam("P_OFFICER_NAME", Types.VARCHAR),
                outParam("P_CUST_NAME", Types.VARCHAR),
                outParam("P_CUST_ACCOUNT", Types.VARCHAR),
                outParam("P_BEN_NAME", Types.VARCHAR),
                outParam("P_BEN_ID", Types.VARCHAR),
                outParam("P_BEN_RES", Types.VARCHAR),
                outParam("P_BEN_CITY", Types.VARCHAR),
                outParam("P_BEN_STREET", Types.VARCHAR),
                outParam("P_BEN_ACNT", Types.VARCHAR),
                outParam("P_BEN_TYPE", Types.VARCHAR),
                outParam("P_ORD_NAME", Types.VARCHAR),
                outParam("P_ORD_ID", Types.VARCHAR),
                outParam("P_ORD_RES", Types.VARCHAR),
                outParam("P_ORD_ACNT", Types.VARCHAR),
                outParam("P_BEN_BANK_NAME", Types.VARCHAR),
                outParam("P_BEN_BANK_BRANCH", Types.VARCHAR),
                outParam("P_BEN_BANK_SWIFT_CODE", Types.VARCHAR),
                outParam("P_BEN_BANK_OTHER_CODE", Types.VARCHAR),
                outParam("P_BEN_BANK_ADDR", Types.VARCHAR),
                outParam("P_IM_BANK_NAME", Types.VARCHAR),
                outParam("P_IM_BANK_ACNT", Types.VARCHAR),
                outParam("P_IM_BANK_SWIFT_CODE", Types.VARCHAR),
                outParam("P_IM_BANK_OTHER_CODE", Types.VARCHAR),
                outParam("P_IM_BANK_ADDR", Types.VARCHAR),
                outParam("P_PAYMENT_DETAILS", Types.VARCHAR),
                outParam("P_ITB", Types.VARCHAR),
                outParam("P_ITC", Types.VARCHAR),
                outParam("P_EPC", Types.VARCHAR),
                outParam("P_ITD", Types.VARCHAR),
                outParam("P_EXCHANGE_RATE", Types.VARCHAR),
                outParam("P_COM_TYPE", Types.VARCHAR),
                outParam("P_TYPE_ID", Types.NUMERIC),
                outParam("P_E_CHEQUE", Types.VARCHAR),
                outParam("P_E_EXPIRY", Types.DATE),
                outParam("P_SIGN_TIME", Types.DATE),
                outParam("P_SIGN_DEV_TYPE", Types.NUMERIC),
                outParam("P_SIGN_DEV_ID", Types.VARCHAR),
                outParam("P_SIGN_KEY1", Types.VARCHAR),
                outParam("P_SIGN_KEY2", Types.VARCHAR),
                outParam("P_SIGN_RSA", Types.VARCHAR),
                outParam("P_TEMPLATE_NAME", Types.VARCHAR),
                outParam("P_GLOBUS_FT", Types.VARCHAR),
                outParam("P_BOOKING_DATE", Types.DATE),
                outParam("P_EXEC_DATE", Types.DATE),
                outParam("P_TAX_PAYER_ID", Types.VARCHAR),
                outParam("P_IS_TAX_DOC", Types.NUMERIC),
                outParam("P_IS_UT_PAYMENT", Types.NUMERIC),
                outParam("P_LOCATION", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_ID", paymentId);

        Map<String, Object> outputs = executeProcedureWithOutputs("payment", inParams, outParams, inputParams);

        Map<String, Object> result = new HashMap<>();
        result.put("id", paymentId);
        result.put("userName", outputs.get("P_USER_NAME"));
        result.put("userId", outputs.get("P_USER_ID"));
        result.put("officerName", outputs.get("P_OFFICER_NAME"));
        result.put("custName", outputs.get("P_CUST_NAME"));
        result.put("custAccount", outputs.get("P_CUST_ACCOUNT"));
        result.put("benName", outputs.get("P_BEN_NAME"));
        result.put("benId", outputs.get("P_BEN_ID"));
        result.put("benRes", outputs.get("P_BEN_RES"));
        result.put("benCity", outputs.get("P_BEN_CITY"));
        result.put("benStreet", outputs.get("P_BEN_STREET"));
        result.put("benAcnt", outputs.get("P_BEN_ACNT"));
        result.put("benType", outputs.get("P_BEN_TYPE"));
        result.put("ordName", outputs.get("P_ORD_NAME"));
        result.put("ordId", outputs.get("P_ORD_ID"));
        result.put("ordRes", outputs.get("P_ORD_RES"));
        result.put("ordAcnt", outputs.get("P_ORD_ACNT"));
        result.put("benBankName", outputs.get("P_BEN_BANK_NAME"));
        result.put("benBankBranch", outputs.get("P_BEN_BANK_BRANCH"));
        result.put("benBankSwiftCode", outputs.get("P_BEN_BANK_SWIFT_CODE"));
        result.put("benBankOtherCode", outputs.get("P_BEN_BANK_OTHER_CODE"));
        result.put("benBankAddr", outputs.get("P_BEN_BANK_ADDR"));
        result.put("imBankName", outputs.get("P_IM_BANK_NAME"));
        result.put("imBankAcnt", outputs.get("P_IM_BANK_ACNT"));
        result.put("imBankSwiftCode", outputs.get("P_IM_BANK_SWIFT_CODE"));
        result.put("imBankOtherCode", outputs.get("P_IM_BANK_OTHER_CODE"));
        result.put("imBankAddr", outputs.get("P_IM_BANK_ADDR"));
        result.put("paymentDetails", outputs.get("P_PAYMENT_DETAILS"));
        result.put("itb", outputs.get("P_ITB"));
        result.put("itc", outputs.get("P_ITC"));
        result.put("epc", outputs.get("P_EPC"));
        result.put("itd", outputs.get("P_ITD"));
        result.put("exchangeRate", outputs.get("P_EXCHANGE_RATE"));
        result.put("comType", outputs.get("P_COM_TYPE"));
        result.put("typeId", outputs.get("P_TYPE_ID"));
        result.put("eCheque", outputs.get("P_E_CHEQUE"));
        result.put("eExpiry", outputs.get("P_E_EXPIRY"));
        result.put("signTime", outputs.get("P_SIGN_TIME"));
        result.put("signDevType", outputs.get("P_SIGN_DEV_TYPE"));
        result.put("signDevId", outputs.get("P_SIGN_DEV_ID"));
        result.put("signKey1", outputs.get("P_SIGN_KEY1"));
        result.put("signKey2", outputs.get("P_SIGN_KEY2"));
        result.put("signRSA", outputs.get("P_SIGN_RSA"));
        result.put("templateName", outputs.get("P_TEMPLATE_NAME"));
        result.put("globusFt", outputs.get("P_GLOBUS_FT"));
        result.put("bookingDate", outputs.get("P_BOOKING_DATE"));
        result.put("execDate", outputs.get("P_EXEC_DATE"));
        result.put("taxPayerId", outputs.get("P_TAX_PAYER_ID"));
        result.put("isTaxDoc", outputs.get("P_IS_TAX_DOC"));
        result.put("isUtPayment", outputs.get("P_IS_UT_PAYMENT"));
        result.put("location", outputs.get("P_LOCATION"));

        return result;
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

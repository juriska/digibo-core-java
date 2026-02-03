package com.digibo.core.service.impl;

import com.digibo.core.dto.request.PaymentSearchRequest;
import com.digibo.core.dto.response.PaymentDetailsResponse;
import com.digibo.core.dto.response.PaymentSearchResponse;
import com.digibo.core.service.PaymentService;
import com.digibo.core.service.base.BaseService;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.*;

/**
 * PaymentServiceImpl - Real implementation of PaymentService
 * Calls BOPayment Oracle package procedures
 */
@Service
@Profile("!mock")
public class PaymentServiceImpl extends BaseService implements PaymentService {

    public PaymentServiceImpl() {
        super("BOPayment");
    }

    @Override
    public PaymentSearchResponse find(PaymentSearchRequest filters) {
        logger.debug("Calling BOPayment.find()");

        List<SqlParameter> inParams = List.of(
                inParam("P_CUST_ID", Types.VARCHAR),
                inParam("P_CUST_NAME", Types.VARCHAR),
                inParam("P_USER_LOGIN", Types.VARCHAR),
                inParam("P_OFFICER_ID", Types.NUMERIC),
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
                inParam("P_PAYMENT_ID", Types.VARCHAR),
                inParam("P_CHANNELS", Types.VARCHAR),
                inParam("P_STATUSES", Types.VARCHAR),
                inParam("P_CREATED_FROM", Types.DATE),
                inParam("P_CREATED_TILL", Types.DATE)
        );

        List<SqlOutParameter> outParams = List.of(
                outParam("P_PMT_CLASS_OUT", Types.VARCHAR)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_CUST_ID", filters.getCustId());
        inputParams.put("P_CUST_NAME", filters.getCustName());
        inputParams.put("P_USER_LOGIN", filters.getUserLogin());
        inputParams.put("P_OFFICER_ID", filters.getOfficerId());
        inputParams.put("P_BEN_NAME", filters.getBenName());
        inputParams.put("P_FROM_CONTRACT", filters.getFromContract());
        inputParams.put("P_FROM_LOCATION", filters.getFromLocation());
        inputParams.put("P_PMT_DETAILS", filters.getPmtDetails());
        inputParams.put("P_AMOUNT_FROM", filters.getAmountFrom());
        inputParams.put("P_AMOUNT_TILL", filters.getAmountTill());
        inputParams.put("P_CURRENCIES", filters.getCurrencies());
        inputParams.put("P_PMT_CLASS", filters.getPmtClass());
        inputParams.put("P_EFFECT_FROM", filters.getEffectFrom());
        inputParams.put("P_EFFECT_TILL", filters.getEffectTill());
        inputParams.put("P_PAYMENT_ID", filters.getPaymentId());
        inputParams.put("P_CHANNELS", filters.getChannels());
        inputParams.put("P_STATUSES", filters.getStatuses());
        inputParams.put("P_CREATED_FROM", filters.getCreatedFrom());
        inputParams.put("P_CREATED_TILL", filters.getCreatedTill());

        CursorResult<Map<String, Object>> result = executeCursorProcedureWithOutputs(
                "find",
                inParams,
                outParams,
                inputParams,
                "P_CURSOR",
                (rs, rowNum) -> {
                    Map<String, Object> row = new HashMap<>();
                    int colCount = rs.getMetaData().getColumnCount();
                    for (int i = 1; i <= colCount; i++) {
                        String colName = rs.getMetaData().getColumnName(i);
                        row.put(colName, rs.getObject(i));
                    }
                    return row;
                }
        );

        logger.debug("find returned {} rows", result.getRows().size());

        return PaymentSearchResponse.builder()
                .payments(result.getRows())
                .pmtClass((String) result.getOutput("P_PMT_CLASS_OUT"))
                .build();
    }

    @Override
    public PaymentDetailsResponse getPaymentDetails(String paymentId) {
        logger.debug("Getting payment details for: {}", paymentId);

        List<SqlParameter> inParams = List.of(
                inParam("P_ID", Types.VARCHAR)
        );

        List<SqlOutParameter> outParams = List.of(
                outParam("P_USER_NAME", Types.VARCHAR),
                outParam("P_USER_ID", Types.VARCHAR),
                outParam("P_OFFICER_NAME", Types.VARCHAR),
                outParam("P_GOLD_MANAGER", Types.VARCHAR),
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
                outParam("P_SIGN_RSA", Types.VARCHAR),
                outParam("P_TEMPLATE_NAME", Types.VARCHAR),
                outParam("P_GLOBUS_FT", Types.VARCHAR),
                outParam("P_BOOKING_DATE", Types.DATE),
                outParam("P_EXEC_DATE", Types.DATE),
                outParam("P_TAX_PAYER_ID", Types.VARCHAR),
                outParam("P_IS_TAX_DOC", Types.NUMERIC),
                outParam("P_IS_UT_PAYMENT", Types.NUMERIC),
                outParam("P_UT_TARIF_TYPE", Types.VARCHAR),
                outParam("P_UT_TARIF_PRICE", Types.VARCHAR),
                outParam("P_UT_TARIF_AMOUNT", Types.VARCHAR),
                outParam("P_UT_OVER_AMOUNT", Types.VARCHAR),
                outParam("P_UT_PENALTY_TYPE", Types.VARCHAR),
                outParam("P_UT_PENALTY_DAYS", Types.NUMERIC),
                outParam("P_UT_PENALTY_AMNT", Types.VARCHAR),
                outParam("P_UT_BOOKING_DATE", Types.DATE),
                outParam("P_UT_DATE_START", Types.DATE),
                outParam("P_UT_DATE_END", Types.DATE),
                outParam("P_UT_VOLUME_START", Types.VARCHAR),
                outParam("P_UT_VOLUME_END", Types.VARCHAR),
                outParam("P_UT_QUANTITY", Types.VARCHAR),
                outParam("P_UT_CORP_CUST_CODE", Types.VARCHAR),
                outParam("P_UT_CORP_CUST_BRANCH", Types.VARCHAR),
                outParam("P_UT_BILL_NUMBER", Types.VARCHAR),
                outParam("P_UT_PHONE_NUMBER", Types.VARCHAR),
                outParam("P_ABONENT_CODE", Types.VARCHAR),
                outParam("P_ABONENT_NAME", Types.VARCHAR),
                outParam("P_ABONENT_SURNAME", Types.VARCHAR),
                outParam("P_ABONENT_ACCOUNT", Types.VARCHAR),
                outParam("P_ABONENT_LEGAL_ID", Types.VARCHAR),
                outParam("P_LOCATION", Types.VARCHAR),
                outParam("P_SAVING_ACC_CHARGE_ID", Types.NUMERIC),
                outParam("P_REJECTOR", Types.VARCHAR),
                outParam("P_REJECT_DATE", Types.DATE),
                outParam("P_CHARGES_ACCOUNT", Types.VARCHAR),
                outParam("P_SALARY_PAYMENT_DATE", Types.DATE),
                outParam("P_SECTOR", Types.NUMERIC),
                outParam("P_SEGMENT", Types.VARCHAR),
                outParam("P_BEN_MOBILE", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_ID", paymentId);

        Map<String, Object> result = executeProcedureWithOutputs("payment", inParams, outParams, inputParams);

        return PaymentDetailsResponse.builder()
                .userName((String) result.get("P_USER_NAME"))
                .userId((String) result.get("P_USER_ID"))
                .officerName((String) result.get("P_OFFICER_NAME"))
                .goldManager((String) result.get("P_GOLD_MANAGER"))
                .custName((String) result.get("P_CUST_NAME"))
                .custAccount((String) result.get("P_CUST_ACCOUNT"))
                .benName((String) result.get("P_BEN_NAME"))
                .benId((String) result.get("P_BEN_ID"))
                .benRes((String) result.get("P_BEN_RES"))
                .benCity((String) result.get("P_BEN_CITY"))
                .benStreet((String) result.get("P_BEN_STREET"))
                .benAcnt((String) result.get("P_BEN_ACNT"))
                .benType((String) result.get("P_BEN_TYPE"))
                .ordName((String) result.get("P_ORD_NAME"))
                .ordId((String) result.get("P_ORD_ID"))
                .ordRes((String) result.get("P_ORD_RES"))
                .ordAcnt((String) result.get("P_ORD_ACNT"))
                .benBankName((String) result.get("P_BEN_BANK_NAME"))
                .benBankBranch((String) result.get("P_BEN_BANK_BRANCH"))
                .benBankSwiftCode((String) result.get("P_BEN_BANK_SWIFT_CODE"))
                .benBankOtherCode((String) result.get("P_BEN_BANK_OTHER_CODE"))
                .benBankAddr((String) result.get("P_BEN_BANK_ADDR"))
                .imBankName((String) result.get("P_IM_BANK_NAME"))
                .imBankAcnt((String) result.get("P_IM_BANK_ACNT"))
                .imBankSwiftCode((String) result.get("P_IM_BANK_SWIFT_CODE"))
                .imBankOtherCode((String) result.get("P_IM_BANK_OTHER_CODE"))
                .imBankAddr((String) result.get("P_IM_BANK_ADDR"))
                .paymentDetails((String) result.get("P_PAYMENT_DETAILS"))
                .itb((String) result.get("P_ITB"))
                .itc((String) result.get("P_ITC"))
                .epc((String) result.get("P_EPC"))
                .itd((String) result.get("P_ITD"))
                .exchangeRate((String) result.get("P_EXCHANGE_RATE"))
                .comType((String) result.get("P_COM_TYPE"))
                .typeId(toInteger(result.get("P_TYPE_ID")))
                .eCheque((String) result.get("P_E_CHEQUE"))
                .eExpiry((Date) result.get("P_E_EXPIRY"))
                .signTime((Date) result.get("P_SIGN_TIME"))
                .signRSA((String) result.get("P_SIGN_RSA"))
                .templateName((String) result.get("P_TEMPLATE_NAME"))
                .globusFt((String) result.get("P_GLOBUS_FT"))
                .bookingDate((Date) result.get("P_BOOKING_DATE"))
                .execDate((Date) result.get("P_EXEC_DATE"))
                .taxPayerId((String) result.get("P_TAX_PAYER_ID"))
                .isTaxDoc(toInteger(result.get("P_IS_TAX_DOC")))
                .isUtPayment(toInteger(result.get("P_IS_UT_PAYMENT")))
                .utTarifType((String) result.get("P_UT_TARIF_TYPE"))
                .utTarifPrice((String) result.get("P_UT_TARIF_PRICE"))
                .utTarifAmount((String) result.get("P_UT_TARIF_AMOUNT"))
                .utOverAmount((String) result.get("P_UT_OVER_AMOUNT"))
                .utPenaltyType((String) result.get("P_UT_PENALTY_TYPE"))
                .utPenaltyDays(toInteger(result.get("P_UT_PENALTY_DAYS")))
                .utPenaltyAmnt((String) result.get("P_UT_PENALTY_AMNT"))
                .utBookingDate((Date) result.get("P_UT_BOOKING_DATE"))
                .utDateStart((Date) result.get("P_UT_DATE_START"))
                .utDateEnd((Date) result.get("P_UT_DATE_END"))
                .utVolumeStart((String) result.get("P_UT_VOLUME_START"))
                .utVolumeEnd((String) result.get("P_UT_VOLUME_END"))
                .utQuantity((String) result.get("P_UT_QUANTITY"))
                .utCorpCustCode((String) result.get("P_UT_CORP_CUST_CODE"))
                .utCorpCustBranch((String) result.get("P_UT_CORP_CUST_BRANCH"))
                .utBillNumber((String) result.get("P_UT_BILL_NUMBER"))
                .utPhoneNumber((String) result.get("P_UT_PHONE_NUMBER"))
                .abonentCode((String) result.get("P_ABONENT_CODE"))
                .abonentName((String) result.get("P_ABONENT_NAME"))
                .abonentSurname((String) result.get("P_ABONENT_SURNAME"))
                .abonentAccount((String) result.get("P_ABONENT_ACCOUNT"))
                .abonentLegalId((String) result.get("P_ABONENT_LEGAL_ID"))
                .location((String) result.get("P_LOCATION"))
                .savingAccChargeId(toLong(result.get("P_SAVING_ACC_CHARGE_ID")))
                .rejector((String) result.get("P_REJECTOR"))
                .rejectDate((Date) result.get("P_REJECT_DATE"))
                .chargesAccount((String) result.get("P_CHARGES_ACCOUNT"))
                .salaryPaymentDate((Date) result.get("P_SALARY_PAYMENT_DATE"))
                .sector(toInteger(result.get("P_SECTOR")))
                .segment((String) result.get("P_SEGMENT"))
                .benMobile((String) result.get("P_BEN_MOBILE"))
                .build();
    }

    @Override
    public void changeTemplateGroup(String paymentId, String groupId) {
        logger.debug("Changing template group for payment: {}", paymentId);

        List<SqlParameter> params = List.of(
                inParam("P_ID", Types.VARCHAR),
                inParam("P_GROUP", Types.VARCHAR)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_ID", paymentId);
        inputParams.put("P_GROUP", groupId);

        executeVoidProcedure("change_template_group", params, inputParams);
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

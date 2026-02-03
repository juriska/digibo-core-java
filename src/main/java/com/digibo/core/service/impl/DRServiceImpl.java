package com.digibo.core.service.impl;

import com.digibo.core.service.DRService;
import com.digibo.core.service.base.BaseService;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.*;

/**
 * DRServiceImpl - Real implementation of DRService
 * Calls BODR Oracle package procedures
 */
@Service
@Profile("!mock")
public class DRServiceImpl extends BaseService implements DRService {

    public DRServiceImpl() {
        super("BODR");
    }

    @Override
    public List<Map<String, Object>> find(String custId, String custName, String userLogin,
                                           Long officerId, Long pClassId, String pTerm,
                                           String amountFrom, String amountTill, String currencies,
                                           String docId, String statuses,
                                           Date createdFrom, Date createdTill) {
        logger.debug("Calling BODR.find() with filters");

        List<SqlParameter> params = List.of(
                inParam("P_CUST_ID", Types.VARCHAR),
                inParam("P_CUST_NAME", Types.VARCHAR),
                inParam("P_USER_LOGIN", Types.VARCHAR),
                inParam("P_OFFICER_ID", Types.NUMERIC),
                inParam("P_CLASS_ID", Types.NUMERIC),
                inParam("P_TERM", Types.VARCHAR),
                inParam("P_AMOUNT_FROM", Types.VARCHAR),
                inParam("P_AMOUNT_TILL", Types.VARCHAR),
                inParam("P_CURRENCIES", Types.VARCHAR),
                inParam("P_DOC_ID", Types.VARCHAR),
                inParam("P_STATUSES", Types.VARCHAR),
                inParam("P_CREATED_FROM", Types.DATE),
                inParam("P_CREATED_TILL", Types.DATE)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_CUST_ID", custId);
        inputParams.put("P_CUST_NAME", custName);
        inputParams.put("P_USER_LOGIN", userLogin);
        inputParams.put("P_OFFICER_ID", officerId);
        inputParams.put("P_CLASS_ID", pClassId);
        inputParams.put("P_TERM", pTerm);
        inputParams.put("P_AMOUNT_FROM", amountFrom);
        inputParams.put("P_AMOUNT_TILL", amountTill);
        inputParams.put("P_CURRENCIES", currencies);
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
    public Map<String, Object> dr(String docId) {
        logger.debug("Calling BODR.dr({})", docId);

        List<SqlParameter> inParams = List.of(
                inParam("P_ID", Types.VARCHAR)
        );

        List<SqlOutParameter> outParams = List.of(
                outParam("P_CLASS_ID", Types.NUMERIC),
                outParam("P_GLOBUS_NO", Types.VARCHAR),
                outParam("P_USER_NAME", Types.VARCHAR),
                outParam("P_USER_ID", Types.VARCHAR),
                outParam("P_OFFICER_NAME", Types.VARCHAR),
                outParam("P_CUST_NAME", Types.VARCHAR),
                outParam("P_CUST_ACCOUNT", Types.VARCHAR),
                outParam("P_RATE", Types.VARCHAR),
                outParam("P_PRODUCT", Types.VARCHAR),
                outParam("P_FREQUENCY", Types.VARCHAR),
                outParam("P_BEN_NAME", Types.VARCHAR),
                outParam("P_BEN_IBAN", Types.VARCHAR),
                outParam("P_AGREEMENT", Types.VARCHAR),
                outParam("P_LOCATION", Types.VARCHAR),
                outParam("P_VALUE_DATE", Types.DATE),
                outParam("P_FROM_CONTRACT", Types.VARCHAR),
                outParam("P_LOYALTY_BONUS", Types.VARCHAR),
                outParam("P_START_AMOUNT", Types.VARCHAR),
                outParam("P_START_CCY", Types.VARCHAR),
                outParam("P_CURRENT_AMOUNT", Types.VARCHAR),
                outParam("P_CURRENT_CCY", Types.VARCHAR),
                outParam("P_REPLENISHMENT_AMOUNT", Types.VARCHAR),
                outParam("P_REPLENISHMENT_CCY", Types.VARCHAR),
                outParam("P_START_DATE", Types.DATE),
                outParam("P_TERM_DATE", Types.DATE),
                outParam("P_ITC", Types.VARCHAR),
                outParam("P_ITB", Types.VARCHAR),
                outParam("P_REJECTOR", Types.VARCHAR),
                outParam("P_REJECT_DATE", Types.DATE),
                outParam("P_TYPE_ID", Types.NUMERIC)
        );

        Map<String, Object> inputParams = Map.of("P_ID", docId);

        Map<String, Object> result = executeProcedureWithOutputs("dr", inParams, outParams, inputParams);

        Map<String, Object> response = new HashMap<>();
        response.put("id", docId);
        response.put("classId", result.get("P_CLASS_ID"));
        response.put("globusNo", result.get("P_GLOBUS_NO"));
        response.put("userName", result.get("P_USER_NAME"));
        response.put("userId", result.get("P_USER_ID"));
        response.put("officerName", result.get("P_OFFICER_NAME"));
        response.put("custName", result.get("P_CUST_NAME"));
        response.put("custAccount", result.get("P_CUST_ACCOUNT"));
        response.put("rate", result.get("P_RATE"));
        response.put("product", result.get("P_PRODUCT"));
        response.put("frequency", result.get("P_FREQUENCY"));
        response.put("benName", result.get("P_BEN_NAME"));
        response.put("benIban", result.get("P_BEN_IBAN"));
        response.put("agreement", result.get("P_AGREEMENT"));
        response.put("location", result.get("P_LOCATION"));
        response.put("valueDate", result.get("P_VALUE_DATE"));
        response.put("fromContract", result.get("P_FROM_CONTRACT"));
        response.put("loyaltyBonus", result.get("P_LOYALTY_BONUS"));
        response.put("startAmount", result.get("P_START_AMOUNT"));
        response.put("startCcy", result.get("P_START_CCY"));
        response.put("currentAmount", result.get("P_CURRENT_AMOUNT"));
        response.put("currentCcy", result.get("P_CURRENT_CCY"));
        response.put("replenishmentAmount", result.get("P_REPLENISHMENT_AMOUNT"));
        response.put("replenishmentCcy", result.get("P_REPLENISHMENT_CCY"));
        response.put("startDate", result.get("P_START_DATE"));
        response.put("termDate", result.get("P_TERM_DATE"));
        response.put("infoToCustomer", result.get("P_ITC"));
        response.put("infoToBank", result.get("P_ITB"));
        response.put("rejector", result.get("P_REJECTOR"));
        response.put("rejectDate", result.get("P_REJECT_DATE"));
        response.put("typeId", result.get("P_TYPE_ID"));

        return response;
    }
}

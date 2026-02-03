package com.digibo.core.service.impl;

import com.digibo.core.service.DDService;
import com.digibo.core.service.base.BaseService;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.*;

/**
 * DDServiceImpl - Real implementation of DDService
 * Calls BODD Oracle package procedures
 */
@Service
@Profile("!mock")
public class DDServiceImpl extends BaseService implements DDService {

    public DDServiceImpl() {
        super("BODD");
    }

    @Override
    public List<Map<String, Object>> find(String custId, String custName, String userLogin,
                                           Long officerId, String pType, String docId,
                                           String statuses, Date createdFrom, Date createdTill) {
        logger.debug("Calling BODD.find() with filters");

        List<SqlParameter> params = List.of(
                inParam("P_CUST_ID", Types.VARCHAR),
                inParam("P_CUST_NAME", Types.VARCHAR),
                inParam("P_USER_LOGIN", Types.VARCHAR),
                inParam("P_OFFICER_ID", Types.NUMERIC),
                inParam("P_TYPE", Types.VARCHAR),
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
        inputParams.put("P_TYPE", pType);
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
    public Map<String, Object> dd(String ddId) {
        logger.debug("Calling BODD.dd({})", ddId);

        List<SqlParameter> inParams = List.of(
                inParam("P_ID", Types.VARCHAR)
        );

        List<SqlOutParameter> outParams = List.of(
                outParam("P_USER_NAME", Types.VARCHAR),
                outParam("P_USER_ID", Types.VARCHAR),
                outParam("P_OFFICER_NAME", Types.VARCHAR),
                outParam("P_CUST_NAME", Types.VARCHAR),
                outParam("P_CUST_ACCOUNT", Types.VARCHAR),
                outParam("P_GLOBUS_NO", Types.VARCHAR),
                outParam("P_AGREEMENT", Types.VARCHAR),
                outParam("P_CONTRACT_ID", Types.VARCHAR),
                outParam("P_FIRST_DATE", Types.DATE),
                outParam("P_LAST_DATE", Types.DATE),
                outParam("P_BEN_NAME", Types.VARCHAR),
                outParam("P_UT_PHONE_NO", Types.VARCHAR),
                outParam("P_AMOUNT_LIMIT", Types.VARCHAR),
                outParam("P_AB_CODE", Types.VARCHAR),
                outParam("P_AB_NAME", Types.VARCHAR),
                outParam("P_AB_SURNAME", Types.VARCHAR),
                outParam("P_AB_ACNT", Types.VARCHAR),
                outParam("P_AB_ID", Types.VARCHAR),
                outParam("P_GIRO_NR", Types.VARCHAR),
                outParam("P_LEGAL_ADDR", Types.VARCHAR),
                outParam("P_CONTACT_ADDR", Types.VARCHAR),
                outParam("P_EMAIL", Types.VARCHAR),
                outParam("P_LOCATION", Types.VARCHAR),
                outParam("P_REJECTOR", Types.VARCHAR),
                outParam("P_REJECT_DATE", Types.DATE),
                outParam("P_ITC", Types.VARCHAR),
                outParam("P_ITB", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_ID", ddId);

        Map<String, Object> result = executeProcedureWithOutputs("dd", inParams, outParams, inputParams);

        Map<String, Object> response = new HashMap<>();
        response.put("id", ddId);
        response.put("userName", result.get("P_USER_NAME"));
        response.put("userId", result.get("P_USER_ID"));
        response.put("officerName", result.get("P_OFFICER_NAME"));
        response.put("custName", result.get("P_CUST_NAME"));
        response.put("custAccount", result.get("P_CUST_ACCOUNT"));
        response.put("globusNo", result.get("P_GLOBUS_NO"));
        response.put("agreement", result.get("P_AGREEMENT"));
        response.put("contractId", result.get("P_CONTRACT_ID"));
        response.put("firstDate", result.get("P_FIRST_DATE"));
        response.put("lastDate", result.get("P_LAST_DATE"));
        response.put("benName", result.get("P_BEN_NAME"));
        response.put("utPhoneNo", result.get("P_UT_PHONE_NO"));
        response.put("amountLimit", result.get("P_AMOUNT_LIMIT"));
        response.put("abCode", result.get("P_AB_CODE"));
        response.put("abName", result.get("P_AB_NAME"));
        response.put("abSurname", result.get("P_AB_SURNAME"));
        response.put("abAcnt", result.get("P_AB_ACNT"));
        response.put("abId", result.get("P_AB_ID"));
        response.put("giroNr", result.get("P_GIRO_NR"));
        response.put("legalAddr", result.get("P_LEGAL_ADDR"));
        response.put("contactAddr", result.get("P_CONTACT_ADDR"));
        response.put("email", result.get("P_EMAIL"));
        response.put("location", result.get("P_LOCATION"));
        response.put("rejector", result.get("P_REJECTOR"));
        response.put("rejectDate", result.get("P_REJECT_DATE"));
        response.put("itc", result.get("P_ITC"));
        response.put("itb", result.get("P_ITB"));

        return response;
    }
}

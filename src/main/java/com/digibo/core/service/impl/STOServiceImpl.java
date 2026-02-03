package com.digibo.core.service.impl;

import com.digibo.core.service.STOService;
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
 * STOServiceImpl - Real implementation of STOService
 * Calls BOSTO Oracle package procedures
 */
@Service
@Profile("!mock")
public class STOServiceImpl extends BaseService implements STOService {

    public STOServiceImpl() {
        super("BOSTO");
    }

    @Override
    public List<Map<String, Object>> find(String custId, String custName, String userLogin, Long officerId,
                                           String pType, String docId, String statuses,
                                           Date createdFrom, Date createdTill) {
        logger.debug("Calling BOSTO.find({}, {}, {}, {}, {}, {}, {}, {}, {})",
                custId, custName, userLogin, officerId, pType, docId, statuses, createdFrom, createdTill);

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
    public Map<String, Object> sto(String stoId) {
        logger.debug("Calling BOSTO.sto({})", stoId);

        List<SqlParameter> inParams = List.of(
                inParam("P_ID", Types.VARCHAR)
        );

        List<SqlOutParameter> outParams = List.of(
                outParam("P_GLOBUS_NO", Types.VARCHAR),
                outParam("P_USER_NAME", Types.VARCHAR),
                outParam("P_USER_ID", Types.VARCHAR),
                outParam("P_OFFICER_NAME", Types.VARCHAR),
                outParam("P_CUST_NAME", Types.VARCHAR),
                outParam("P_CUST_ACCOUNT", Types.VARCHAR),
                outParam("P_BEN_ACNT", Types.VARCHAR),
                outParam("P_BEN_TYPE", Types.VARCHAR),
                outParam("P_BEN_NAME", Types.VARCHAR),
                outParam("P_BEN_ID", Types.VARCHAR),
                outParam("P_BEN_RES", Types.VARCHAR),
                outParam("P_BEN_BANK_NAME", Types.VARCHAR),
                outParam("P_BEN_BANK_BRANCH", Types.VARCHAR),
                outParam("P_BEN_BANK_SWIFT", Types.VARCHAR),
                outParam("P_BEN_BANK_OTHER_CODE", Types.VARCHAR),
                outParam("P_DETAILS", Types.VARCHAR),
                outParam("P_CONTRACT_ID", Types.VARCHAR),
                outParam("P_AGREEMENT", Types.VARCHAR),
                outParam("P_FIRST_DATE", Types.DATE),
                outParam("P_LAST_DATE", Types.DATE),
                outParam("P_NEXT_DATE", Types.DATE),
                outParam("P_FREQUENCY", Types.VARCHAR),
                outParam("P_BAL_MIN", Types.VARCHAR),
                outParam("P_BAL_MAX", Types.VARCHAR),
                outParam("P_CR_MIN", Types.VARCHAR),
                outParam("P_REVOLVING", Types.VARCHAR),
                outParam("P_LOCATION", Types.VARCHAR),
                outParam("P_REJECTOR", Types.VARCHAR),
                outParam("P_REJECT_DATE", Types.DATE),
                outParam("P_ABONENT_CODE", Types.VARCHAR),
                outParam("P_ITC", Types.VARCHAR),
                outParam("P_ITB", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_ID", stoId);

        Map<String, Object> result = executeProcedureWithOutputs("sto", inParams, outParams, inputParams);

        Map<String, Object> stoDoc = new HashMap<>();
        stoDoc.put("id", stoId);
        stoDoc.put("globusNo", result.get("P_GLOBUS_NO"));
        stoDoc.put("userName", result.get("P_USER_NAME"));
        stoDoc.put("userId", result.get("P_USER_ID"));
        stoDoc.put("officerName", result.get("P_OFFICER_NAME"));
        stoDoc.put("custName", result.get("P_CUST_NAME"));
        stoDoc.put("custAccount", result.get("P_CUST_ACCOUNT"));
        stoDoc.put("benAcnt", result.get("P_BEN_ACNT"));
        stoDoc.put("benType", result.get("P_BEN_TYPE"));
        stoDoc.put("benName", result.get("P_BEN_NAME"));
        stoDoc.put("benId", result.get("P_BEN_ID"));
        stoDoc.put("benRes", result.get("P_BEN_RES"));
        stoDoc.put("benBankName", result.get("P_BEN_BANK_NAME"));
        stoDoc.put("benBankBranch", result.get("P_BEN_BANK_BRANCH"));
        stoDoc.put("benBankSwift", result.get("P_BEN_BANK_SWIFT"));
        stoDoc.put("benBankOtherCode", result.get("P_BEN_BANK_OTHER_CODE"));
        stoDoc.put("details", result.get("P_DETAILS"));
        stoDoc.put("contractId", result.get("P_CONTRACT_ID"));
        stoDoc.put("agreement", result.get("P_AGREEMENT"));
        stoDoc.put("firstDate", result.get("P_FIRST_DATE"));
        stoDoc.put("lastDate", result.get("P_LAST_DATE"));
        stoDoc.put("nextDate", result.get("P_NEXT_DATE"));
        stoDoc.put("frequency", result.get("P_FREQUENCY"));
        stoDoc.put("balMin", result.get("P_BAL_MIN"));
        stoDoc.put("balMax", result.get("P_BAL_MAX"));
        stoDoc.put("crMin", result.get("P_CR_MIN"));
        stoDoc.put("revolving", result.get("P_REVOLVING"));
        stoDoc.put("location", result.get("P_LOCATION"));
        stoDoc.put("rejector", result.get("P_REJECTOR"));
        stoDoc.put("rejectDate", result.get("P_REJECT_DATE"));
        stoDoc.put("abonentCode", result.get("P_ABONENT_CODE"));
        stoDoc.put("itc", result.get("P_ITC"));
        stoDoc.put("itb", result.get("P_ITB"));

        return stoDoc;
    }
}

package com.digibo.core.service.impl;

import com.digibo.core.service.PamoService;
import com.digibo.core.service.base.BaseService;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.*;

/**
 * PamoServiceImpl - Real implementation of PamoService
 * Calls BOPAMO Oracle package procedures
 */
@Service
@Profile("!mock")
public class PamoServiceImpl extends BaseService implements PamoService {

    public PamoServiceImpl() {
        super("BOPAMO");
    }

    @Override
    public List<Map<String, Object>> find(String custId, String custName, String userLogin,
                                           String docClass, String pIsin, String docId,
                                           String statuses, Date createdFrom, Date createdTill) {
        logger.debug("Calling BOPAMO.find()");

        List<SqlParameter> params = List.of(
                inParam("P_CUST_ID", Types.VARCHAR),
                inParam("P_CUST_NAME", Types.VARCHAR),
                inParam("P_USER_LOGIN", Types.VARCHAR),
                inParam("P_DOC_CLASS", Types.VARCHAR),
                inParam("P_ISIN", Types.VARCHAR),
                inParam("P_DOC_ID", Types.VARCHAR),
                inParam("P_STATUSES", Types.VARCHAR),
                inParam("P_CREATED_FROM", Types.DATE),
                inParam("P_CREATED_TILL", Types.DATE)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_CUST_ID", custId);
        inputParams.put("P_CUST_NAME", custName);
        inputParams.put("P_USER_LOGIN", userLogin);
        inputParams.put("P_DOC_CLASS", docClass);
        inputParams.put("P_ISIN", pIsin);
        inputParams.put("P_DOC_ID", docId);
        inputParams.put("P_STATUSES", statuses);
        inputParams.put("P_CREATED_FROM", createdFrom);
        inputParams.put("P_CREATED_TILL", createdTill);

        return executeCursorProcedure("find", params, inputParams, "P_CURSOR",
                (rs, rowNum) -> mapRow(rs));
    }

    @Override
    public List<Map<String, Object>> findMy(String docClass) {
        logger.debug("Calling BOPAMO.find_my({})", docClass);

        List<SqlParameter> params = List.of(
                inParam("P_DOC_CLASS", Types.VARCHAR)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_DOC_CLASS", docClass);

        return executeCursorProcedure("find_my", params, inputParams, "P_CURSOR",
                (rs, rowNum) -> mapRow(rs));
    }

    @Override
    public Map<String, Object> pamo(String pamoId) {
        logger.debug("Calling BOPAMO.pamo({})", pamoId);

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
                outParam("P_CREDIT_AMNT", Types.VARCHAR),
                outParam("P_TEXT", Types.VARCHAR),
                outParam("P_LOCATION", Types.VARCHAR),
                outParam("P_ITC", Types.VARCHAR),
                outParam("P_ITB", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_ID", pamoId);

        Map<String, Object> outputs = executeProcedureWithOutputs("pamo", inParams, outParams, inputParams);

        Map<String, Object> result = new HashMap<>();
        result.put("id", pamoId);
        result.put("docNo", outputs.get("P_DOC_NO"));
        result.put("userName", outputs.get("P_USER_NAME"));
        result.put("userId", outputs.get("P_USER_ID"));
        result.put("officerName", outputs.get("P_OFFICER_NAME"));
        result.put("custName", outputs.get("P_CUST_NAME"));
        result.put("custAccount", outputs.get("P_CUST_ACCOUNT"));
        result.put("globusNo", outputs.get("P_GLOBUS_NO"));
        result.put("portfolioId", outputs.get("P_PORTFOLIO_ID"));
        result.put("operation", outputs.get("P_OPERATION"));
        result.put("isinCode", outputs.get("P_ISIN_CODE"));
        result.put("fundName", outputs.get("P_FUND_NAME"));
        result.put("investVolume", outputs.get("P_INVEST_VOLUME"));
        result.put("creditAmnt", outputs.get("P_CREDIT_AMNT"));
        result.put("text", outputs.get("P_TEXT"));
        result.put("location", outputs.get("P_LOCATION"));
        result.put("itc", outputs.get("P_ITC"));
        result.put("itb", outputs.get("P_ITB"));

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

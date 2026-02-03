package com.digibo.core.service.impl;

import com.digibo.core.service.FindCustomersService;
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
 * FindCustomersServiceImpl - Real implementation of FindCustomersService
 * Calls BOFindCustomers Oracle package procedures
 */
@Service
@Profile("!mock")
public class FindCustomersServiceImpl extends BaseService implements FindCustomersService {

    public FindCustomersServiceImpl() {
        super("BOFindCustomers");
    }

    @Override
    public List<Map<String, Object>> findCustomers(String custId, String custName, String legalId, String licence) {
        logger.debug("Calling BOFindCustomers.find_customers()");

        List<SqlParameter> params = List.of(
                inParam("P_CUST_ID", Types.VARCHAR),
                inParam("P_CUST_NAME", Types.VARCHAR),
                inParam("P_LEGAL_ID", Types.VARCHAR),
                inParam("P_LICENCE", Types.VARCHAR)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_CUST_ID", custId);
        inputParams.put("P_CUST_NAME", custName);
        inputParams.put("P_LEGAL_ID", legalId);
        inputParams.put("P_LICENCE", licence);

        List<Map<String, Object>> rows = executeCursorProcedure("find_customers", params, inputParams, "P_CURSOR",
                (rs, rowNum) -> {
                    Map<String, Object> row = new HashMap<>();
                    int colCount = rs.getMetaData().getColumnCount();
                    for (int i = 1; i <= colCount; i++) {
                        String colName = rs.getMetaData().getColumnName(i);
                        row.put(colName, rs.getObject(i));
                    }
                    return row;
                });

        logger.debug("find_customers returned {} rows", rows.size());
        return rows;
    }

    @Override
    public Map<String, Object> loadCustomerById(Long customerId) {
        logger.debug("Calling BOFindCustomers.load_customer_by_id({})", customerId);

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
                outParam("P_TYPE", Types.VARCHAR),
                outParam("P_HAS_AGREEMENT_IN_GLOBUS", Types.NUMERIC)
        );

        Map<String, Object> inputParams = Map.of("P_ID", customerId);

        Map<String, Object> outputs = executeProcedureWithOutputs("load_customer_by_id", inParams, outParams, inputParams);

        Map<String, Object> result = new HashMap<>();
        result.put("id", customerId);
        result.put("name", outputs.get("P_NAME"));
        result.put("issuerCountry", outputs.get("P_ISSUER_COUNTRY"));
        result.put("personalId", outputs.get("P_PERSONAL_ID"));
        result.put("passportNo", outputs.get("P_PASSPORT_NO"));
        result.put("street", outputs.get("P_STREET"));
        result.put("city", outputs.get("P_CITY"));
        result.put("country", outputs.get("P_COUNTRY"));
        result.put("zip", outputs.get("P_ZIP"));
        result.put("phone", outputs.get("P_PHONE"));
        result.put("mobile", outputs.get("P_MOBILE"));
        result.put("fax", outputs.get("P_FAX"));
        result.put("email", outputs.get("P_EMAIL"));
        result.put("apart", outputs.get("P_APART"));
        result.put("house", outputs.get("P_HOUSE"));
        result.put("stdQ", toInteger(outputs.get("P_STD_Q")));
        result.put("specQ", outputs.get("P_SPEC_Q"));
        result.put("answer", outputs.get("P_ANSWER"));
        result.put("regDate", outputs.get("P_REG_DATE"));
        result.put("changeDate", outputs.get("P_CHANGE_DATE"));
        result.put("changeOfficerId", outputs.get("P_CHANGE_OFFICER_ID"));
        result.put("changeLogin", outputs.get("P_CHANGE_LOGIN"));
        result.put("type", outputs.get("P_TYPE"));
        result.put("hasAgreementInGlobus", toInteger(outputs.get("P_HAS_AGREEMENT_IN_GLOBUS")));

        return result;
    }

    private Integer toInteger(Object value) {
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).intValue();
        return Integer.parseInt(value.toString());
    }
}

package com.digibo.core.service.impl;

import com.digibo.core.service.OTSEService;
import com.digibo.core.service.base.BaseService;
import oracle.jdbc.OracleTypes;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.*;

/**
 * OTSEServiceImpl - Real implementation of OTSEService
 * Calls BOOTSE Oracle package procedures
 */
@Service
@Profile("!mock")
public class OTSEServiceImpl extends BaseService implements OTSEService {

    public OTSEServiceImpl() {
        super("BOOTSE");
    }

    @Override
    public List<Map<String, Object>> find(String custId, String custName, String userLogin,
                                           String personalId, String docId) {
        logger.debug("Calling BOOTSE.find()");

        List<SqlParameter> params = List.of(
                inParam("P_CUST_ID", Types.VARCHAR),
                inParam("P_CUST_NAME", Types.VARCHAR),
                inParam("P_USER_LOGIN", Types.VARCHAR),
                inParam("P_PERSONAL_ID", Types.VARCHAR),
                inParam("P_DOC_ID", Types.VARCHAR)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_CUST_ID", custId);
        inputParams.put("P_CUST_NAME", custName);
        inputParams.put("P_USER_LOGIN", userLogin);
        inputParams.put("P_PERSONAL_ID", personalId);
        inputParams.put("P_DOC_ID", docId);

        return executeCursorProcedure("find", params, inputParams, "P_CURSOR",
                (rs, rowNum) -> mapRow(rs));
    }

    @Override
    public List<Map<String, Object>> findNew() {
        logger.debug("Calling BOOTSE.find_new()");

        List<SqlParameter> params = List.of();
        Map<String, Object> inputParams = Map.of();

        return executeCursorProcedure("find_new", params, inputParams, "P_CURSOR",
                (rs, rowNum) -> mapRow(rs));
    }

    @Override
    public Map<String, Object> getCustomer(String customerId) {
        logger.debug("Calling BOOTSE.get_customer({})", customerId);

        // This function returns a cursor and has an output parameter
        String sql = "{ ? = call " + packageName + ".get_customer(?, ?) }";

        try (var conn = dataSource.getConnection();
             var cs = conn.prepareCall(sql)) {

            cs.registerOutParameter(1, OracleTypes.CURSOR);
            cs.setString(2, customerId);
            cs.registerOutParameter(3, Types.NUMERIC); // P_RV

            cs.execute();

            Integer resultCode = cs.getInt(3);

            List<Map<String, Object>> rows = new ArrayList<>();
            try (ResultSet rs = (ResultSet) cs.getObject(1)) {
                if (rs != null) {
                    while (rs.next()) {
                        rows.add(mapRow(rs));
                    }
                }
            }

            Map<String, Object> result = new HashMap<>();
            result.put("resultCode", resultCode);
            result.put("customer", rows.isEmpty() ? null : rows.get(0));

            return result;

        } catch (Exception e) {
            logger.error("Error calling BOOTSE.get_customer: {}", e.getMessage());
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> bind(String wocId, String custId, String userId, String docId) {
        logger.debug("Calling BOOTSE.bind({}, {}, {}, {})", wocId, custId, userId, docId);

        List<SqlParameter> params = List.of(
                inParam("P_WOC_ID", Types.VARCHAR),
                inParam("P_CUST_ID", Types.VARCHAR),
                inParam("P_USER_ID", Types.VARCHAR),
                inParam("P_DOC_ID", Types.VARCHAR)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_WOC_ID", wocId);
        inputParams.put("P_CUST_ID", custId);
        inputParams.put("P_USER_ID", userId);
        inputParams.put("P_DOC_ID", docId);

        executeVoidProcedure("bind", params, inputParams);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("wocId", wocId);
        result.put("custId", custId);
        result.put("userId", userId);
        result.put("docId", docId);
        return result;
    }

    @Override
    public Map<String, Object> setWocStatus(String wocId, Integer status, Integer subStatus) {
        logger.debug("Calling BOOTSE.set_woc_status({}, {}, {})", wocId, status, subStatus);

        List<SqlParameter> params = List.of(
                inParam("P_WOC_ID", Types.VARCHAR),
                inParam("P_STATUS", Types.NUMERIC),
                inParam("P_SUB_STATUS", Types.NUMERIC)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_WOC_ID", wocId);
        inputParams.put("P_STATUS", status);
        inputParams.put("P_SUB_STATUS", subStatus);

        executeVoidProcedure("set_woc_status", params, inputParams);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("wocId", wocId);
        result.put("status", status);
        result.put("subStatus", subStatus);
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

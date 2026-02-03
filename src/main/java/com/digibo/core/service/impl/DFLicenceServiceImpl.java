package com.digibo.core.service.impl;

import com.digibo.core.service.DFLicenceService;
import com.digibo.core.service.base.BaseService;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DFLicenceServiceImpl - Real implementation of DFLicenceService
 * Calls BODFLicence Oracle package procedures
 */
@Service
@Profile("!mock")
public class DFLicenceServiceImpl extends BaseService implements DFLicenceService {

    public DFLicenceServiceImpl() {
        super("BODFLicence");
    }

    @Override
    public List<Map<String, Object>> getLicences(Integer pCount) {
        logger.debug("Calling BODFLicence.get_licences({})", pCount);

        List<SqlParameter> params = List.of(
                inParam("P_COUNT", Types.NUMERIC)
        );

        Map<String, Object> inputParams = Map.of("P_COUNT", pCount != null ? pCount : 10);

        return executeCursorProcedure("get_licences", params, inputParams, "P_CURSOR",
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
    public Map<String, Object> newLicense(String pId) {
        logger.debug("Calling BODFLicence.new_license({})", pId);

        List<SqlParameter> params = List.of(
                inParam("P_ID", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_ID", pId);

        executeVoidProcedure("new_license", params, inputParams);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("licenseId", pId);
        response.put("message", "License created successfully");

        return response;
    }

    @Override
    public Map<String, Object> printLicence(String pId) {
        logger.debug("Calling BODFLicence.print_licence({})", pId);

        List<SqlParameter> params = List.of(
                inParam("P_ID", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_ID", pId);

        executeVoidProcedure("print_licence", params, inputParams);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("licenseId", pId);
        response.put("message", "License marked as printed successfully");

        return response;
    }
}

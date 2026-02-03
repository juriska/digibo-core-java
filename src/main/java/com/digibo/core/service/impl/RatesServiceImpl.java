package com.digibo.core.service.impl;

import com.digibo.core.service.RatesService;
import com.digibo.core.service.base.BaseService;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.*;

/**
 * RatesServiceImpl - Real implementation of RatesService
 * Calls BOrates Oracle package procedures
 */
@Service
@Profile("!mock")
public class RatesServiceImpl extends BaseService implements RatesService {

    public RatesServiceImpl() {
        super("BOrates");
    }

    @Override
    public List<Map<String, Object>> loadCurrencyRates(String filter, String dao) {
        logger.debug("Calling BOrates.loadCurrencyRates({}, {})", filter, dao);

        List<SqlParameter> params = List.of(
                inParam("P_FILTER", Types.VARCHAR),
                inParam("P_DAO", Types.VARCHAR)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_FILTER", filter);
        inputParams.put("P_DAO", dao);

        return executeCursorProcedure("loadCurrencyRates", params, inputParams, "P_CURSOR",
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

package com.digibo.core.mapper;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Generic RowMapper that maps any ResultSet to a Map<String, Object>
 * Preserves column names as keys and values as Objects
 */
public class GenericRowMapper implements RowMapper<Map<String, Object>> {

    private static final GenericRowMapper INSTANCE = new GenericRowMapper();

    public static GenericRowMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
        Map<String, Object> row = new HashMap<>();
        int columnCount = rs.getMetaData().getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            String columnName = rs.getMetaData().getColumnName(i);
            Object value = rs.getObject(i);
            row.put(columnName, value);
        }

        return row;
    }
}

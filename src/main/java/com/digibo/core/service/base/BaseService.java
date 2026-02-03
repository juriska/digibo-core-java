package com.digibo.core.service.base;

import com.digibo.core.exception.DatabaseException;
import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

/**
 * Base service class providing Oracle PL/SQL procedure execution capabilities.
 * Mirrors the Node.js BaseService functionality with methods for:
 * - executeProcedure: Execute procedure with output parameters
 * - executeCursorProcedure: Execute procedure returning cursor results
 * - executeScalarFunction: Execute function returning single value
 * - executeVoidProcedure: Execute procedure with no return value
 */
public abstract class BaseService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected final String packageName;

    @Autowired
    protected DataSource dataSource;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    protected BaseService(String packageName) {
        this.packageName = packageName;
    }

    /**
     * Execute a procedure with input and output parameters.
     * Returns the output parameters as a Map.
     */
    protected Map<String, Object> executeProcedure(String procedureName,
                                                    List<SqlParameter> parameters,
                                                    Map<String, Object> inputParams) {
        try {
            SimpleJdbcCall call = new SimpleJdbcCall(dataSource)
                    .withCatalogName(packageName)
                    .withProcedureName(procedureName)
                    .withoutProcedureColumnMetaDataAccess()
                    .declareParameters(parameters.toArray(new SqlParameter[0]));

            MapSqlParameterSource paramSource = new MapSqlParameterSource(inputParams);
            return call.execute(paramSource);

        } catch (DataAccessException e) {
            logger.error("Database error executing {}.{}: {}", packageName, procedureName, e.getMessage());
            throw new DatabaseException(
                    "Database error: " + e.getMessage(),
                    packageName,
                    procedureName,
                    e
            );
        }
    }

    /**
     * Execute a procedure that returns results via a cursor output parameter.
     * Returns a list of mapped objects.
     */
    protected <T> List<T> executeCursorProcedure(String procedureName,
                                                  List<SqlParameter> inParameters,
                                                  Map<String, Object> inputParams,
                                                  String cursorParamName,
                                                  RowMapper<T> rowMapper) {
        String sql = buildProcedureCall(procedureName, inParameters.size() + 1);

        try (Connection conn = dataSource.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            int paramIndex = 1;

            // Set input parameters
            for (SqlParameter param : inParameters) {
                Object value = inputParams.get(param.getName());
                setParameter(cs, paramIndex++, value, param.getSqlType());
            }

            // Register cursor output parameter
            cs.registerOutParameter(paramIndex, OracleTypes.CURSOR);

            cs.execute();

            // Process cursor results
            List<T> results = new ArrayList<>();
            try (ResultSet rs = (ResultSet) cs.getObject(paramIndex)) {
                if (rs != null) {
                    while (rs.next()) {
                        results.add(rowMapper.mapRow(rs, results.size()));
                    }
                }
            }

            return results;

        } catch (SQLException e) {
            logger.error("Database error executing {}.{}: {}", packageName, procedureName, e.getMessage());
            throw new DatabaseException(
                    "Database error: " + e.getMessage(),
                    packageName,
                    procedureName,
                    e
            );
        }
    }

    /**
     * Execute a procedure with cursor and additional output parameters.
     * Returns both the cursor results and other output values.
     */
    protected <T> CursorResult<T> executeCursorProcedureWithOutputs(String procedureName,
                                                                     List<SqlParameter> inParameters,
                                                                     List<SqlOutParameter> outParameters,
                                                                     Map<String, Object> inputParams,
                                                                     String cursorParamName,
                                                                     RowMapper<T> rowMapper) {
        int totalParams = inParameters.size() + outParameters.size() + 1; // +1 for cursor
        String sql = buildProcedureCall(procedureName, totalParams);

        try (Connection conn = dataSource.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            int paramIndex = 1;

            // Set input parameters
            for (SqlParameter param : inParameters) {
                Object value = inputParams.get(param.getName());
                setParameter(cs, paramIndex++, value, param.getSqlType());
            }

            // Register cursor output parameter
            int cursorIndex = paramIndex++;
            cs.registerOutParameter(cursorIndex, OracleTypes.CURSOR);

            // Register other output parameters
            Map<String, Integer> outParamIndexes = new HashMap<>();
            for (SqlOutParameter outParam : outParameters) {
                outParamIndexes.put(outParam.getName(), paramIndex);
                cs.registerOutParameter(paramIndex++, outParam.getSqlType());
            }

            cs.execute();

            // Process cursor results
            List<T> results = new ArrayList<>();
            try (ResultSet rs = (ResultSet) cs.getObject(cursorIndex)) {
                if (rs != null) {
                    while (rs.next()) {
                        results.add(rowMapper.mapRow(rs, results.size()));
                    }
                }
            }

            // Get other output values
            Map<String, Object> outputValues = new HashMap<>();
            for (SqlOutParameter outParam : outParameters) {
                int idx = outParamIndexes.get(outParam.getName());
                outputValues.put(outParam.getName(), cs.getObject(idx));
            }

            return new CursorResult<>(results, outputValues);

        } catch (SQLException e) {
            logger.error("Database error executing {}.{}: {}", packageName, procedureName, e.getMessage());
            throw new DatabaseException(
                    "Database error: " + e.getMessage(),
                    packageName,
                    procedureName,
                    e
            );
        }
    }

    /**
     * Execute a scalar function that returns a single value.
     */
    @SuppressWarnings("unchecked")
    protected <T> T executeScalarFunction(String functionName,
                                           List<SqlParameter> parameters,
                                           Map<String, Object> inputParams,
                                           int returnType) {
        String sql = buildFunctionCall(functionName, parameters.size());

        try (Connection conn = dataSource.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            // Register return value
            cs.registerOutParameter(1, returnType);

            // Set input parameters
            int paramIndex = 2;
            for (SqlParameter param : parameters) {
                Object value = inputParams.get(param.getName());
                setParameter(cs, paramIndex++, value, param.getSqlType());
            }

            cs.execute();

            Object result = cs.getObject(1);
            return (T) convertResult(result, returnType);

        } catch (SQLException e) {
            logger.error("Database error executing {}.{}: {}", packageName, functionName, e.getMessage());
            throw new DatabaseException(
                    "Database error: " + e.getMessage(),
                    packageName,
                    functionName,
                    e
            );
        }
    }

    /**
     * Execute a procedure that doesn't return any value (void procedure).
     * Typically used for INSERT, UPDATE, DELETE operations.
     */
    protected void executeVoidProcedure(String procedureName,
                                         List<SqlParameter> parameters,
                                         Map<String, Object> inputParams) {
        String sql = buildProcedureCall(procedureName, parameters.size());

        try (Connection conn = dataSource.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            int paramIndex = 1;
            for (SqlParameter param : parameters) {
                Object value = inputParams.get(param.getName());
                setParameter(cs, paramIndex++, value, param.getSqlType());
            }

            cs.execute();

        } catch (SQLException e) {
            logger.error("Database error executing {}.{}: {}", packageName, procedureName, e.getMessage());
            throw new DatabaseException(
                    "Database error: " + e.getMessage(),
                    packageName,
                    procedureName,
                    e
            );
        }
    }

    /**
     * Execute a procedure with multiple output parameters (no cursor).
     * Returns all output parameter values as a Map.
     */
    protected Map<String, Object> executeProcedureWithOutputs(String procedureName,
                                                               List<SqlParameter> inParameters,
                                                               List<SqlOutParameter> outParameters,
                                                               Map<String, Object> inputParams) {
        int totalParams = inParameters.size() + outParameters.size();
        String sql = buildProcedureCall(procedureName, totalParams);

        try (Connection conn = dataSource.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            int paramIndex = 1;

            // Set input parameters
            for (SqlParameter param : inParameters) {
                Object value = inputParams.get(param.getName());
                setParameter(cs, paramIndex++, value, param.getSqlType());
            }

            // Register output parameters
            Map<String, Integer> outParamIndexes = new HashMap<>();
            for (SqlOutParameter outParam : outParameters) {
                outParamIndexes.put(outParam.getName(), paramIndex);
                cs.registerOutParameter(paramIndex++, outParam.getSqlType());
            }

            cs.execute();

            // Get output values
            Map<String, Object> outputValues = new HashMap<>();
            for (SqlOutParameter outParam : outParameters) {
                int idx = outParamIndexes.get(outParam.getName());
                outputValues.put(outParam.getName(), cs.getObject(idx));
            }

            return outputValues;

        } catch (SQLException e) {
            logger.error("Database error executing {}.{}: {}", packageName, procedureName, e.getMessage());
            throw new DatabaseException(
                    "Database error: " + e.getMessage(),
                    packageName,
                    procedureName,
                    e
            );
        }
    }

    /**
     * Execute raw SQL query with parameters.
     */
    protected <T> List<T> executeQuery(String sql, Map<String, Object> params, RowMapper<T> rowMapper) {
        try {
            return jdbcTemplate.query(sql, rowMapper, params.values().toArray());
        } catch (DataAccessException e) {
            logger.error("Database error executing query: {}", e.getMessage());
            throw new DatabaseException(
                    "Database error: " + e.getMessage(),
                    packageName,
                    "query",
                    e
            );
        }
    }

    // Helper methods

    private String buildProcedureCall(String procedureName, int paramCount) {
        StringBuilder sb = new StringBuilder("{ call ");
        sb.append(packageName).append(".").append(procedureName).append("(");
        for (int i = 0; i < paramCount; i++) {
            if (i > 0) sb.append(", ");
            sb.append("?");
        }
        sb.append(") }");
        return sb.toString();
    }

    private String buildFunctionCall(String functionName, int paramCount) {
        StringBuilder sb = new StringBuilder("{ ? = call ");
        sb.append(packageName).append(".").append(functionName).append("(");
        for (int i = 0; i < paramCount; i++) {
            if (i > 0) sb.append(", ");
            sb.append("?");
        }
        sb.append(") }");
        return sb.toString();
    }

    private void setParameter(CallableStatement cs, int index, Object value, int sqlType) throws SQLException {
        if (value == null) {
            cs.setNull(index, sqlType);
        } else {
            switch (sqlType) {
                case Types.VARCHAR, Types.CHAR, Types.LONGVARCHAR -> cs.setString(index, value.toString());
                case Types.NUMERIC, Types.DECIMAL -> {
                    if (value instanceof BigDecimal bd) {
                        cs.setBigDecimal(index, bd);
                    } else if (value instanceof Number num) {
                        cs.setBigDecimal(index, BigDecimal.valueOf(num.doubleValue()));
                    } else {
                        cs.setBigDecimal(index, new BigDecimal(value.toString()));
                    }
                }
                case Types.INTEGER -> {
                    if (value instanceof Number num) {
                        cs.setInt(index, num.intValue());
                    } else {
                        cs.setInt(index, Integer.parseInt(value.toString()));
                    }
                }
                case Types.BIGINT -> {
                    if (value instanceof Number num) {
                        cs.setLong(index, num.longValue());
                    } else {
                        cs.setLong(index, Long.parseLong(value.toString()));
                    }
                }
                case Types.DOUBLE, Types.FLOAT -> {
                    if (value instanceof Number num) {
                        cs.setDouble(index, num.doubleValue());
                    } else {
                        cs.setDouble(index, Double.parseDouble(value.toString()));
                    }
                }
                case Types.DATE -> {
                    if (value instanceof java.sql.Date date) {
                        cs.setDate(index, date);
                    } else if (value instanceof java.util.Date date) {
                        cs.setDate(index, new java.sql.Date(date.getTime()));
                    }
                }
                case Types.TIMESTAMP -> {
                    if (value instanceof Timestamp ts) {
                        cs.setTimestamp(index, ts);
                    } else if (value instanceof java.util.Date date) {
                        cs.setTimestamp(index, new Timestamp(date.getTime()));
                    }
                }
                default -> cs.setObject(index, value, sqlType);
            }
        }
    }

    private Object convertResult(Object result, int sqlType) {
        if (result == null) {
            return null;
        }

        return switch (sqlType) {
            case Types.INTEGER -> {
                if (result instanceof BigDecimal bd) {
                    yield bd.intValue();
                } else if (result instanceof Number num) {
                    yield num.intValue();
                }
                yield result;
            }
            case Types.BIGINT -> {
                if (result instanceof BigDecimal bd) {
                    yield bd.longValue();
                } else if (result instanceof Number num) {
                    yield num.longValue();
                }
                yield result;
            }
            case Types.VARCHAR, Types.CHAR -> result.toString();
            default -> result;
        };
    }

    /**
     * Result container for cursor procedures with additional output parameters.
     */
    public static class CursorResult<T> {
        private final List<T> rows;
        private final Map<String, Object> outputs;

        public CursorResult(List<T> rows, Map<String, Object> outputs) {
            this.rows = rows;
            this.outputs = outputs;
        }

        public List<T> getRows() {
            return rows;
        }

        public Map<String, Object> getOutputs() {
            return outputs;
        }

        @SuppressWarnings("unchecked")
        public <V> V getOutput(String name) {
            return (V) outputs.get(name);
        }
    }

    // Utility methods for creating parameters

    protected SqlParameter inParam(String name, int sqlType) {
        return new SqlParameter(name, sqlType);
    }

    protected SqlOutParameter outParam(String name, int sqlType) {
        return new SqlOutParameter(name, sqlType);
    }

    protected SqlOutParameter cursorParam(String name) {
        return new SqlOutParameter(name, OracleTypes.CURSOR);
    }
}

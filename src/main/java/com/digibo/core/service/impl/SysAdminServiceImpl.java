package com.digibo.core.service.impl;

import com.digibo.core.service.SysAdminService;
import com.digibo.core.service.base.BaseService;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SysAdminServiceImpl - Real implementation of SysAdminService
 * Calls BOSysAdmin Oracle package procedures
 */
@Service
@Profile("!mock")
public class SysAdminServiceImpl extends BaseService implements SysAdminService {

    public SysAdminServiceImpl() {
        super("BOSysAdmin");
    }

    @Override
    public List<Map<String, Object>> getReplacers() {
        logger.debug("Calling BOSysAdmin.get_replacers()");

        return executeCursorProcedure("get_replacers", List.of(), Map.of(), "P_CURSOR",
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
    public List<Map<String, Object>> getOfficers(String login, String name) {
        logger.debug("Calling BOSysAdmin.get_officers({}, {})", login, name);

        List<SqlParameter> params = List.of(
                inParam("P_LOGIN", Types.VARCHAR),
                inParam("P_NAME", Types.VARCHAR)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_LOGIN", login);
        inputParams.put("P_NAME", name);

        return executeCursorProcedure("get_officers", params, inputParams, "P_CURSOR",
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
    public List<Map<String, Object>> getDeptList(Long officerId) {
        logger.debug("Calling BOSysAdmin.get_dept_list({})", officerId);

        List<SqlParameter> params = List.of(
                inParam("P_OFFICER", Types.NUMERIC)
        );

        Map<String, Object> inputParams = Map.of("P_OFFICER", officerId);

        return executeCursorProcedure("get_dept_list", params, inputParams, "P_CURSOR",
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
    public List<Map<String, Object>> officerReplaces(Long officerId) {
        logger.debug("Calling BOSysAdmin.officer_replaces({})", officerId);

        List<SqlParameter> params = List.of(
                inParam("P_ID", Types.NUMERIC)
        );

        Map<String, Object> inputParams = Map.of("P_ID", officerId);

        return executeCursorProcedure("officer_replaces", params, inputParams, "P_CURSOR",
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
    public List<Map<String, Object>> getLogged() {
        logger.debug("Calling BOSysAdmin.get_logged()");

        return executeCursorProcedure("get_logged", List.of(), Map.of(), "P_CURSOR",
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
    public Map<String, Object> loadOfficer(Long officerId) {
        logger.debug("Calling BOSysAdmin.load_officer({})", officerId);

        List<SqlParameter> inParams = List.of(
                inParam("P_ID", Types.NUMERIC)
        );

        List<SqlOutParameter> outParams = List.of(
                outParam("P_NAME", Types.VARCHAR),
                outParam("P_REP_ID", Types.NUMERIC),
                outParam("P_LOGIN", Types.VARCHAR),
                outParam("P_PERSONAL_ID", Types.VARCHAR),
                outParam("P_DEPT_ID", Types.VARCHAR),
                outParam("P_PHONE", Types.VARCHAR),
                outParam("P_MOBILE", Types.VARCHAR),
                outParam("P_EMAIL", Types.VARCHAR),
                outParam("P_REG_DATE", Types.DATE),
                outParam("P_PARENT_DEPT_ID", Types.VARCHAR),
                outParam("P_AVAIL_PKGS", Types.VARCHAR),
                outParam("P_DEF_FOR_COUNTRY", Types.VARCHAR),
                outParam("P_SKYPE_NAME", Types.VARCHAR),
                outParam("P_IS_LDAP_USER", Types.NUMERIC)
        );

        Map<String, Object> inputParams = Map.of("P_ID", officerId);

        // Execute with two cursors (P_HISTORY and P_ACTUAL)
        CursorResult<Map<String, Object>> historyResult = executeCursorProcedureWithOutputs(
                "load_officer", inParams, outParams, inputParams, "P_HISTORY",
                (rs, rowNum) -> {
                    Map<String, Object> row = new HashMap<>();
                    int colCount = rs.getMetaData().getColumnCount();
                    for (int i = 1; i <= colCount; i++) {
                        String colName = rs.getMetaData().getColumnName(i);
                        row.put(colName, rs.getObject(i));
                    }
                    return row;
                });

        Map<String, Object> officer = new HashMap<>();
        officer.put("id", officerId);
        officer.put("name", historyResult.getOutput("P_NAME"));
        officer.put("repId", toInteger(historyResult.getOutput("P_REP_ID")));
        officer.put("login", historyResult.getOutput("P_LOGIN"));
        officer.put("personalId", historyResult.getOutput("P_PERSONAL_ID"));
        officer.put("deptId", historyResult.getOutput("P_DEPT_ID"));
        officer.put("phone", historyResult.getOutput("P_PHONE"));
        officer.put("mobile", historyResult.getOutput("P_MOBILE"));
        officer.put("email", historyResult.getOutput("P_EMAIL"));
        officer.put("regDate", historyResult.getOutput("P_REG_DATE"));
        officer.put("parentDeptId", historyResult.getOutput("P_PARENT_DEPT_ID"));
        officer.put("availPkgs", historyResult.getOutput("P_AVAIL_PKGS"));
        officer.put("defForCountry", historyResult.getOutput("P_DEF_FOR_COUNTRY"));
        officer.put("history", historyResult.getRows());
        officer.put("skypeName", historyResult.getOutput("P_SKYPE_NAME"));
        officer.put("isLdapUser", toInteger(historyResult.getOutput("P_IS_LDAP_USER")));

        return officer;
    }

    @Override
    public void updateOfficer(Map<String, Object> officerData) {
        logger.debug("Calling BOSysAdmin.update_officer({})", officerData.get("id"));

        List<SqlParameter> params = List.of(
                inParam("P_ID", Types.NUMERIC),
                inParam("P_NAME", Types.VARCHAR),
                inParam("P_REP_ID", Types.NUMERIC),
                inParam("P_LOGIN", Types.VARCHAR),
                inParam("P_PERSONAL_ID", Types.VARCHAR),
                inParam("P_DEPT_ID", Types.VARCHAR),
                inParam("P_PHONE", Types.VARCHAR),
                inParam("P_MOBILE", Types.VARCHAR),
                inParam("P_EMAIL", Types.VARCHAR),
                inParam("P_PARENT_DEPT_ID", Types.VARCHAR),
                inParam("P_DEF_FOR_COUNTRY", Types.VARCHAR),
                inParam("P_SKYPE_NAME", Types.VARCHAR),
                inParam("P_IS_LDAP_USER", Types.NUMERIC)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_ID", officerData.get("id"));
        inputParams.put("P_NAME", officerData.get("name"));
        inputParams.put("P_REP_ID", officerData.get("repId"));
        inputParams.put("P_LOGIN", officerData.get("login"));
        inputParams.put("P_PERSONAL_ID", officerData.get("personalId"));
        inputParams.put("P_DEPT_ID", officerData.get("deptId"));
        inputParams.put("P_PHONE", officerData.get("phone"));
        inputParams.put("P_MOBILE", officerData.get("mobile"));
        inputParams.put("P_EMAIL", officerData.get("email"));
        inputParams.put("P_PARENT_DEPT_ID", officerData.get("parentDeptId"));
        inputParams.put("P_DEF_FOR_COUNTRY", officerData.get("defForCountry"));
        inputParams.put("P_SKYPE_NAME", officerData.get("skypeName"));
        inputParams.put("P_IS_LDAP_USER", officerData.get("isLdapUser"));

        executeVoidProcedure("update_officer", params, inputParams);
    }

    @Override
    public void replaceOfficer(Long officerId, Long repId) {
        logger.debug("Calling BOSysAdmin.replace_officer({}, {})", officerId, repId);

        List<SqlParameter> params = List.of(
                inParam("P_ID", Types.NUMERIC),
                inParam("P_REP_ID", Types.NUMERIC)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_ID", officerId);
        inputParams.put("P_REP_ID", repId);

        executeVoidProcedure("replace_officer", params, inputParams);
    }

    @Override
    public void updateRoles(Long officerId, String login, String roles) {
        logger.debug("Calling BOSysAdmin.update_roles({}, {}, {})", officerId, login, roles);

        List<SqlParameter> params = List.of(
                inParam("P_ID", Types.NUMERIC),
                inParam("P_LOGIN", Types.VARCHAR),
                inParam("P_ROLES", Types.VARCHAR)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_ID", officerId);
        inputParams.put("P_LOGIN", login);
        inputParams.put("P_ROLES", roles);

        executeVoidProcedure("update_roles", params, inputParams);
    }

    private Integer toInteger(Object value) {
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).intValue();
        return Integer.parseInt(value.toString());
    }
}

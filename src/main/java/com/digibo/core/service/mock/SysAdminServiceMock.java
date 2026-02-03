package com.digibo.core.service.mock;

import com.digibo.core.service.SysAdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * SysAdminServiceMock - Mock implementation of SysAdminService
 * Returns test data without database calls
 */
@Service
@Profile("mock")
public class SysAdminServiceMock implements SysAdminService {

    private static final Logger logger = LoggerFactory.getLogger(SysAdminServiceMock.class);

    @Override
    public List<Map<String, Object>> getReplacers() {
        logger.debug("[MOCK] getReplacers()");

        List<Map<String, Object>> replacers = new ArrayList<>();

        Map<String, Object> rep1 = new HashMap<>();
        rep1.put("OFFICER_ID", 1L);
        rep1.put("OFFICER_NAME", "Officer Smith");
        rep1.put("REPLACER_ID", 2L);
        rep1.put("REPLACER_NAME", "Officer Jones");
        rep1.put("STATUS", "ACTIVE");
        replacers.add(rep1);

        Map<String, Object> rep2 = new HashMap<>();
        rep2.put("OFFICER_ID", 3L);
        rep2.put("OFFICER_NAME", "Officer Brown");
        rep2.put("REPLACER_ID", null);
        rep2.put("REPLACER_NAME", null);
        rep2.put("STATUS", "INACTIVE");
        replacers.add(rep2);

        return replacers;
    }

    @Override
    public List<Map<String, Object>> getOfficers(String login, String name) {
        logger.debug("[MOCK] getOfficers({}, {})", login, name);

        List<Map<String, Object>> officers = new ArrayList<>();

        Map<String, Object> off1 = new HashMap<>();
        off1.put("OFFICER_ID", 1L);
        off1.put("LOGIN", "smith");
        off1.put("NAME", "Officer Smith");
        off1.put("DEPT", "IT Department");
        off1.put("EMAIL", "smith@example.com");
        officers.add(off1);

        Map<String, Object> off2 = new HashMap<>();
        off2.put("OFFICER_ID", 2L);
        off2.put("LOGIN", "jones");
        off2.put("NAME", "Officer Jones");
        off2.put("DEPT", "Finance Department");
        off2.put("EMAIL", "jones@example.com");
        officers.add(off2);

        return officers;
    }

    @Override
    public List<Map<String, Object>> getDeptList(Long officerId) {
        logger.debug("[MOCK] getDeptList({})", officerId);

        List<Map<String, Object>> depts = new ArrayList<>();

        Map<String, Object> dept1 = new HashMap<>();
        dept1.put("DEPT_ID", "DEPT001");
        dept1.put("DEPT_NAME", "IT Department");
        dept1.put("PARENT_ID", null);
        depts.add(dept1);

        Map<String, Object> dept2 = new HashMap<>();
        dept2.put("DEPT_ID", "DEPT002");
        dept2.put("DEPT_NAME", "Finance Department");
        dept2.put("PARENT_ID", null);
        depts.add(dept2);

        Map<String, Object> dept3 = new HashMap<>();
        dept3.put("DEPT_ID", "DEPT003");
        dept3.put("DEPT_NAME", "Development Team");
        dept3.put("PARENT_ID", "DEPT001");
        depts.add(dept3);

        return depts;
    }

    @Override
    public List<Map<String, Object>> officerReplaces(Long officerId) {
        logger.debug("[MOCK] officerReplaces({})", officerId);

        List<Map<String, Object>> replaces = new ArrayList<>();

        Map<String, Object> rep1 = new HashMap<>();
        rep1.put("REPLACED_OFFICER_ID", 5L);
        rep1.put("REPLACED_OFFICER_NAME", "Officer Williams");
        rep1.put("FROM_DATE", new Date(System.currentTimeMillis() - 7L * 24 * 60 * 60 * 1000));
        rep1.put("TO_DATE", new Date(System.currentTimeMillis() + 7L * 24 * 60 * 60 * 1000));
        replaces.add(rep1);

        return replaces;
    }

    @Override
    public List<Map<String, Object>> getLogged() {
        logger.debug("[MOCK] getLogged()");

        List<Map<String, Object>> logged = new ArrayList<>();

        Map<String, Object> log1 = new HashMap<>();
        log1.put("OFFICER_ID", 1L);
        log1.put("LOGIN", "smith");
        log1.put("NAME", "Officer Smith");
        log1.put("LOGIN_TIME", new Date());
        log1.put("IP_ADDRESS", "192.168.1.100");
        logged.add(log1);

        Map<String, Object> log2 = new HashMap<>();
        log2.put("OFFICER_ID", 2L);
        log2.put("LOGIN", "jones");
        log2.put("NAME", "Officer Jones");
        log2.put("LOGIN_TIME", new Date(System.currentTimeMillis() - 3600000));
        log2.put("IP_ADDRESS", "192.168.1.101");
        logged.add(log2);

        return logged;
    }

    @Override
    public Map<String, Object> loadOfficer(Long officerId) {
        logger.debug("[MOCK] loadOfficer({})", officerId);

        List<Map<String, Object>> history = new ArrayList<>();
        Map<String, Object> hist1 = new HashMap<>();
        hist1.put("ACTION", "LOGIN");
        hist1.put("DATE", new Date());
        hist1.put("DETAILS", "Logged in from 192.168.1.100");
        history.add(hist1);

        List<Map<String, Object>> actual = new ArrayList<>();
        Map<String, Object> act1 = new HashMap<>();
        act1.put("ROLE", "ADMIN");
        act1.put("ASSIGNED_DATE", new Date());
        actual.add(act1);

        Map<String, Object> officer = new HashMap<>();
        officer.put("id", officerId);
        officer.put("name", "Officer Smith");
        officer.put("repId", 2);
        officer.put("login", "smith");
        officer.put("personalId", "PID12345");
        officer.put("deptId", "DEPT001");
        officer.put("phone", "+1-555-1234");
        officer.put("mobile", "+1-555-5678");
        officer.put("email", "smith@example.com");
        officer.put("regDate", new Date(System.currentTimeMillis() - 365L * 24 * 60 * 60 * 1000));
        officer.put("parentDeptId", null);
        officer.put("availPkgs", "PKG1,PKG2,PKG3");
        officer.put("defForCountry", "LV");
        officer.put("history", history);
        officer.put("actual", actual);
        officer.put("skypeName", "smith.skype");
        officer.put("isLdapUser", 1);

        return officer;
    }

    @Override
    public void updateOfficer(Map<String, Object> officerData) {
        logger.debug("[MOCK] updateOfficer({})", officerData.get("id"));
        // Mock implementation - no action needed
    }

    @Override
    public void replaceOfficer(Long officerId, Long repId) {
        logger.debug("[MOCK] replaceOfficer({}, {})", officerId, repId);
        // Mock implementation - no action needed
    }

    @Override
    public void updateRoles(Long officerId, String login, String roles) {
        logger.debug("[MOCK] updateRoles({}, {}, {})", officerId, login, roles);
        // Mock implementation - no action needed
    }
}

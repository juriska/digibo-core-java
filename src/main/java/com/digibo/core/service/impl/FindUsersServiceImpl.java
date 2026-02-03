package com.digibo.core.service.impl;

import com.digibo.core.service.FindUsersService;
import com.digibo.core.service.base.BaseService;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FindUsersServiceImpl - Real implementation of FindUsersService
 * Calls BOFindUsers Oracle package procedures
 */
@Service
@Profile("!mock")
public class FindUsersServiceImpl extends BaseService implements FindUsersService {

    public FindUsersServiceImpl() {
        super("BOFindUsers");
    }

    @Override
    public List<Map<String, Object>> findUsers(
            String userId,
            String globusUserId,
            String userName,
            String personalId,
            Long officerId,
            String phone,
            String fax,
            String email,
            String channelId,
            String login,
            String cDevNum,
            Long channel,
            String custId,
            String custResidence,
            String custType,
            Date dateFrom,
            Date dateTill,
            Integer status
    ) {
        logger.debug("Calling BOFindUsers.find_users()");

        List<SqlParameter> params = List.of(
                inParam("P_USER_ID", Types.VARCHAR),
                inParam("P_GLOBUS_USER_ID", Types.VARCHAR),
                inParam("P_USER_NAME", Types.VARCHAR),
                inParam("P_PERSONAL_ID", Types.VARCHAR),
                inParam("P_OFFICER_ID", Types.NUMERIC),
                inParam("P_PHONE", Types.VARCHAR),
                inParam("P_FAX", Types.VARCHAR),
                inParam("P_EMAIL", Types.VARCHAR),
                inParam("P_CHANNEL_ID", Types.VARCHAR),
                inParam("P_LOGIN", Types.VARCHAR),
                inParam("P_CDEV_NUM", Types.VARCHAR),
                inParam("P_CHANNEL", Types.NUMERIC),
                inParam("P_CUST_ID", Types.VARCHAR),
                inParam("P_CUST_RESIDENCE", Types.VARCHAR),
                inParam("P_CUST_TYPE", Types.VARCHAR),
                inParam("P_DATE_FROM", Types.DATE),
                inParam("P_DATE_TILL", Types.DATE),
                inParam("P_STATUS", Types.NUMERIC)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_USER_ID", userId);
        inputParams.put("P_GLOBUS_USER_ID", globusUserId);
        inputParams.put("P_USER_NAME", userName);
        inputParams.put("P_PERSONAL_ID", personalId);
        inputParams.put("P_OFFICER_ID", officerId);
        inputParams.put("P_PHONE", phone);
        inputParams.put("P_FAX", fax);
        inputParams.put("P_EMAIL", email);
        inputParams.put("P_CHANNEL_ID", channelId);
        inputParams.put("P_LOGIN", login);
        inputParams.put("P_CDEV_NUM", cDevNum);
        inputParams.put("P_CHANNEL", channel);
        inputParams.put("P_CUST_ID", custId);
        inputParams.put("P_CUST_RESIDENCE", custResidence);
        inputParams.put("P_CUST_TYPE", custType);
        inputParams.put("P_DATE_FROM", dateFrom);
        inputParams.put("P_DATE_TILL", dateTill);
        inputParams.put("P_STATUS", status);

        List<Map<String, Object>> rows = executeCursorProcedure("find_users", params, inputParams, "P_CURSOR",
                (rs, rowNum) -> {
                    Map<String, Object> row = new HashMap<>();
                    int colCount = rs.getMetaData().getColumnCount();
                    for (int i = 1; i <= colCount; i++) {
                        String colName = rs.getMetaData().getColumnName(i);
                        row.put(colName, rs.getObject(i));
                    }
                    return row;
                });

        logger.debug("find_users returned {} rows", rows.size());
        return rows;
    }
}

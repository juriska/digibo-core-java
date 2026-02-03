package com.digibo.core.service.impl;

import com.digibo.core.service.VSAAService;
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
 * VSAAServiceImpl - Real implementation of VSAAService
 * Calls BOVSAA Oracle package procedures
 */
@Service
@Profile("!mock")
public class VSAAServiceImpl extends BaseService implements VSAAService {

    public VSAAServiceImpl() {
        super("BOVSAA");
    }

    @Override
    public List<Map<String, Object>> find(String userName, String legalId, Long officerId,
                                           String docId, String statuses,
                                           Date createdFrom, Date createdTill) {
        logger.debug("Calling BOVSAA.find({}, {}, {}, {}, {}, {}, {})",
                userName, legalId, officerId, docId, statuses, createdFrom, createdTill);

        List<SqlParameter> params = List.of(
                inParam("P_USER_NAME", Types.VARCHAR),
                inParam("P_LEGAL_ID", Types.VARCHAR),
                inParam("P_OFFICER_ID", Types.NUMERIC),
                inParam("P_DOC_ID", Types.VARCHAR),
                inParam("P_STATUSES", Types.VARCHAR),
                inParam("P_CREATED_FROM", Types.DATE),
                inParam("P_CREATED_TILL", Types.DATE)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_USER_NAME", userName);
        inputParams.put("P_LEGAL_ID", legalId);
        inputParams.put("P_OFFICER_ID", officerId);
        inputParams.put("P_DOC_ID", docId);
        inputParams.put("P_STATUSES", statuses);
        inputParams.put("P_CREATED_FROM", createdFrom);
        inputParams.put("P_CREATED_TILL", createdTill);

        return executeCursorProcedure("find", params, inputParams, "P_CURSOR",
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
    public Map<String, Object> vsaa(String docId) {
        logger.debug("Calling BOVSAA.vsaa({})", docId);

        List<SqlParameter> inParams = List.of(
                inParam("P_ID", Types.VARCHAR)
        );

        List<SqlOutParameter> outParams = List.of(
                outParam("P_PERSONAL_ID", Types.VARCHAR),
                outParam("P_SIGN_DATE", Types.DATE),
                outParam("P_LAST_UPDATE_DATE", Types.DATE),
                outParam("P_ITC", Types.VARCHAR),
                outParam("P_STATE", Types.VARCHAR),
                outParam("P_STREET", Types.VARCHAR),
                outParam("P_EMAIL", Types.VARCHAR),
                outParam("P_POSTAL_CODE", Types.VARCHAR),
                outParam("P_PHONE", Types.VARCHAR),
                outParam("P_RECEIVING_TYPE", Types.VARCHAR),
                outParam("P_OFFICER", Types.VARCHAR),
                outParam("P_LOCATION", Types.VARCHAR)
        );

        Map<String, Object> inputParams = Map.of("P_ID", docId);

        Map<String, Object> result = executeProcedureWithOutputs("vsaa", inParams, outParams, inputParams);

        Map<String, Object> vsaaDoc = new HashMap<>();
        vsaaDoc.put("id", docId);
        vsaaDoc.put("personalId", result.get("P_PERSONAL_ID"));
        vsaaDoc.put("signDate", result.get("P_SIGN_DATE"));
        vsaaDoc.put("lastUpdateDate", result.get("P_LAST_UPDATE_DATE"));
        vsaaDoc.put("infoToCustomer", result.get("P_ITC"));
        vsaaDoc.put("state", result.get("P_STATE"));
        vsaaDoc.put("street", result.get("P_STREET"));
        vsaaDoc.put("email", result.get("P_EMAIL"));
        vsaaDoc.put("postalCode", result.get("P_POSTAL_CODE"));
        vsaaDoc.put("phone", result.get("P_PHONE"));
        vsaaDoc.put("receivingType", result.get("P_RECEIVING_TYPE"));
        vsaaDoc.put("officer", result.get("P_OFFICER"));
        vsaaDoc.put("location", result.get("P_LOCATION"));

        return vsaaDoc;
    }
}

package com.digibo.core.service.impl;

import com.digibo.core.service.FaxEditService;
import com.digibo.core.service.base.BaseService;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FaxEditServiceImpl - Real implementation of FaxEditService
 * Calls BOFaxEdit Oracle package procedures
 */
@Service
@Profile("!mock")
public class FaxEditServiceImpl extends BaseService implements FaxEditService {

    public FaxEditServiceImpl() {
        super("BOFaxEdit");
    }

    @Override
    public Map<String, Object> addDocument(
            String faxId,
            Integer docClass,
            Long officerId,
            String custId,
            String fromAccount,
            BigDecimal amnt,
            String ccy,
            String partner,
            String note,
            String subj,
            Integer docStatus,
            byte[] dTif
    ) {
        logger.debug("Calling BOFaxEdit.add_document({})", faxId);

        List<SqlParameter> params = List.of(
                inParam("P_FAX_ID", Types.VARCHAR),
                inParam("P_DOC_CLASS", Types.NUMERIC),
                inParam("P_OFFICER_ID", Types.NUMERIC),
                inParam("P_CUST_ID", Types.VARCHAR),
                inParam("P_FROM_ACCOUNT", Types.VARCHAR),
                inParam("P_AMNT", Types.NUMERIC),
                inParam("P_CCY", Types.VARCHAR),
                inParam("P_PARTNER", Types.VARCHAR),
                inParam("P_NOTE", Types.VARCHAR),
                inParam("P_SUBJ", Types.VARCHAR),
                inParam("P_DOC_STATUS", Types.NUMERIC),
                inParam("P_DTIF", Types.BLOB)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_FAX_ID", faxId);
        inputParams.put("P_DOC_CLASS", docClass);
        inputParams.put("P_OFFICER_ID", officerId);
        inputParams.put("P_CUST_ID", custId);
        inputParams.put("P_FROM_ACCOUNT", fromAccount);
        inputParams.put("P_AMNT", amnt);
        inputParams.put("P_CCY", ccy);
        inputParams.put("P_PARTNER", partner);
        inputParams.put("P_NOTE", note);
        inputParams.put("P_SUBJ", subj);
        inputParams.put("P_DOC_STATUS", docStatus);
        inputParams.put("P_DTIF", dTif);

        executeVoidProcedure("add_document", params, inputParams);

        logger.debug("add_document completed successfully");

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("faxId", faxId);
        result.put("message", "Document added successfully");
        return result;
    }
}

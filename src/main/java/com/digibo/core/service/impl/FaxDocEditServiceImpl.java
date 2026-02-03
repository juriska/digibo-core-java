package com.digibo.core.service.impl;

import com.digibo.core.service.FaxDocEditService;
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
 * FaxDocEditServiceImpl - Real implementation of FaxDocEditService
 * Calls BOFaxDocEdit Oracle package procedures
 */
@Service
@Profile("!mock")
public class FaxDocEditServiceImpl extends BaseService implements FaxDocEditService {

    public FaxDocEditServiceImpl() {
        super("BOFaxDocEdit");
    }

    @Override
    public Map<String, Object> saveDocument(
            String docId,
            Long officerId,
            String custId,
            String fromAccount,
            BigDecimal amnt,
            String ccy,
            String partner,
            String note,
            String subj,
            Integer docStatus
    ) {
        logger.debug("Calling BOFaxDocEdit.save_document({})", docId);

        List<SqlParameter> params = List.of(
                inParam("P_DOC_ID", Types.VARCHAR),
                inParam("P_OFFICER_ID", Types.NUMERIC),
                inParam("P_CUST_ID", Types.VARCHAR),
                inParam("P_FROM_ACCOUNT", Types.VARCHAR),
                inParam("P_AMNT", Types.NUMERIC),
                inParam("P_CCY", Types.VARCHAR),
                inParam("P_PARTNER", Types.VARCHAR),
                inParam("P_NOTE", Types.VARCHAR),
                inParam("P_SUBJ", Types.VARCHAR),
                inParam("P_DOC_STATUS", Types.NUMERIC)
        );

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("P_DOC_ID", docId);
        inputParams.put("P_OFFICER_ID", officerId);
        inputParams.put("P_CUST_ID", custId);
        inputParams.put("P_FROM_ACCOUNT", fromAccount);
        inputParams.put("P_AMNT", amnt);
        inputParams.put("P_CCY", ccy);
        inputParams.put("P_PARTNER", partner);
        inputParams.put("P_NOTE", note);
        inputParams.put("P_SUBJ", subj);
        inputParams.put("P_DOC_STATUS", docStatus);

        executeVoidProcedure("save_document", params, inputParams);

        logger.debug("Document {} saved successfully", docId);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("documentId", docId);
        result.put("message", "Document saved successfully");
        return result;
    }
}

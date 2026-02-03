package com.digibo.core.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class PaymentSearchRequest {
    // Remitter
    private String custId;
    private String custName;
    private String userLogin;
    private Long officerId;

    // Payment
    private String benName;
    private String fromContract;
    private String fromLocation;
    private String pmtDetails;
    private String amountFrom;
    private String amountTill;
    private String currencies;
    private String pmtClass;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date effectFrom;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date effectTill;

    // System
    private String paymentId;
    private String channels;
    private String statuses;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdFrom;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdTill;
}

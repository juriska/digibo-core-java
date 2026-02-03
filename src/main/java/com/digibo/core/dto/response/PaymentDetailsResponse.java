package com.digibo.core.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentDetailsResponse {
    private String userName;
    private String userId;
    private String officerName;
    private String goldManager;
    private String custName;
    private String custAccount;
    private String benName;
    private String benId;
    private String benRes;
    private String benCity;
    private String benStreet;
    private String benAcnt;
    private String benType;
    private String ordName;
    private String ordId;
    private String ordRes;
    private String ordAcnt;
    private String benBankName;
    private String benBankBranch;
    private String benBankSwiftCode;
    private String benBankOtherCode;
    private String benBankAddr;
    private String imBankName;
    private String imBankAcnt;
    private String imBankSwiftCode;
    private String imBankOtherCode;
    private String imBankAddr;
    private String paymentDetails;
    private String itb;
    private String itc;
    private String epc;
    private String itd;
    private String exchangeRate;
    private String comType;
    private Integer typeId;
    private String eCheque;
    private Date eExpiry;
    private Date signTime;
    private String signRSA;
    private String templateName;
    private String globusFt;
    private Date bookingDate;
    private Date execDate;
    private String taxPayerId;
    private Integer isTaxDoc;
    private Integer isUtPayment;
    private String utTarifType;
    private String utTarifPrice;
    private String utTarifAmount;
    private String utOverAmount;
    private String utPenaltyType;
    private Integer utPenaltyDays;
    private String utPenaltyAmnt;
    private Date utBookingDate;
    private Date utDateStart;
    private Date utDateEnd;
    private String utVolumeStart;
    private String utVolumeEnd;
    private String utQuantity;
    private String utCorpCustCode;
    private String utCorpCustBranch;
    private String utBillNumber;
    private String utPhoneNumber;
    private String abonentCode;
    private String abonentName;
    private String abonentSurname;
    private String abonentAccount;
    private String abonentLegalId;
    private String location;
    private Long savingAccChargeId;
    private String rejector;
    private Date rejectDate;
    private String chargesAccount;
    private Date salaryPaymentDate;
    private Integer sector;
    private String segment;
    private String benMobile;
}

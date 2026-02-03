package com.digibo.core.dto.request;

public class DRSearchRequest {
    private String custId;
    private String custName;
    private String userLogin;
    private Integer officerId;
    private Integer pClassId;
    private String pTerm;
    private String amountFrom;
    private String amountTill;
    private String currencies;
    private String docId;
    private String statuses;
    private String createdFrom;
    private String createdTill;

    public String getCustId() { return custId; }
    public void setCustId(String custId) { this.custId = custId; }

    public String getCustName() { return custName; }
    public void setCustName(String custName) { this.custName = custName; }

    public String getUserLogin() { return userLogin; }
    public void setUserLogin(String userLogin) { this.userLogin = userLogin; }

    public Integer getOfficerId() { return officerId; }
    public void setOfficerId(Integer officerId) { this.officerId = officerId; }

    public Integer getPClassId() { return pClassId; }
    public void setPClassId(Integer pClassId) { this.pClassId = pClassId; }

    public String getPTerm() { return pTerm; }
    public void setPTerm(String pTerm) { this.pTerm = pTerm; }

    public String getAmountFrom() { return amountFrom; }
    public void setAmountFrom(String amountFrom) { this.amountFrom = amountFrom; }

    public String getAmountTill() { return amountTill; }
    public void setAmountTill(String amountTill) { this.amountTill = amountTill; }

    public String getCurrencies() { return currencies; }
    public void setCurrencies(String currencies) { this.currencies = currencies; }

    public String getDocId() { return docId; }
    public void setDocId(String docId) { this.docId = docId; }

    public String getStatuses() { return statuses; }
    public void setStatuses(String statuses) { this.statuses = statuses; }

    public String getCreatedFrom() { return createdFrom; }
    public void setCreatedFrom(String createdFrom) { this.createdFrom = createdFrom; }

    public String getCreatedTill() { return createdTill; }
    public void setCreatedTill(String createdTill) { this.createdTill = createdTill; }
}

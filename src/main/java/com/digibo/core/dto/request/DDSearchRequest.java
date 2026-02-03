package com.digibo.core.dto.request;

public class DDSearchRequest {
    private String custId;
    private String custName;
    private String userLogin;
    private Integer officerId;
    private String pType;
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

    public String getPType() { return pType; }
    public void setPType(String pType) { this.pType = pType; }

    public String getDocId() { return docId; }
    public void setDocId(String docId) { this.docId = docId; }

    public String getStatuses() { return statuses; }
    public void setStatuses(String statuses) { this.statuses = statuses; }

    public String getCreatedFrom() { return createdFrom; }
    public void setCreatedFrom(String createdFrom) { this.createdFrom = createdFrom; }

    public String getCreatedTill() { return createdTill; }
    public void setCreatedTill(String createdTill) { this.createdTill = createdTill; }
}

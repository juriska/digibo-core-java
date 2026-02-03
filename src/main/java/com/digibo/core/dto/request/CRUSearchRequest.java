package com.digibo.core.dto.request;

public class CRUSearchRequest {
    private String custId;
    private String docId;
    private String statuses;
    private String createdFrom;
    private String createdTill;

    public String getCustId() { return custId; }
    public void setCustId(String custId) { this.custId = custId; }

    public String getDocId() { return docId; }
    public void setDocId(String docId) { this.docId = docId; }

    public String getStatuses() { return statuses; }
    public void setStatuses(String statuses) { this.statuses = statuses; }

    public String getCreatedFrom() { return createdFrom; }
    public void setCreatedFrom(String createdFrom) { this.createdFrom = createdFrom; }

    public String getCreatedTill() { return createdTill; }
    public void setCreatedTill(String createdTill) { this.createdTill = createdTill; }
}

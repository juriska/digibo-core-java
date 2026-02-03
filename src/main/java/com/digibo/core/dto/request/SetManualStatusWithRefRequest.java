package com.digibo.core.dto.request;

public class SetManualStatusWithRefRequest {
    private String reason;
    private String newStatus;
    private String messageId;
    private String bankReference;

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getNewStatus() { return newStatus; }
    public void setNewStatus(String newStatus) { this.newStatus = newStatus; }

    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }

    public String getBankReference() { return bankReference; }
    public void setBankReference(String bankReference) { this.bankReference = bankReference; }
}

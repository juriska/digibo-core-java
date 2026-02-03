package com.digibo.core.dto.request;

public class SetManualStatusRequest {
    private String reason;
    private String newStatus;
    private String messageId;

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getNewStatus() { return newStatus; }
    public void setNewStatus(String newStatus) { this.newStatus = newStatus; }

    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }
}

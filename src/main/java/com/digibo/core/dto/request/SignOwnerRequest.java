package com.digibo.core.dto.request;

public class SignOwnerRequest {
    private String certificateId;
    private String signatureDate;

    public String getCertificateId() { return certificateId; }
    public void setCertificateId(String certificateId) { this.certificateId = certificateId; }

    public String getSignatureDate() { return signatureDate; }
    public void setSignatureDate(String signatureDate) { this.signatureDate = signatureDate; }
}

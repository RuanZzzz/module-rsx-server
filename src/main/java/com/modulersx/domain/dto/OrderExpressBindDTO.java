package com.modulersx.domain.dto;

public class OrderExpressBindDTO {

    private String expressCompanyCode;
    private String expressCompanyName;
    private String trackingNo;
    private String receiverPhoneSuffix;

    public String getExpressCompanyCode() {
        return expressCompanyCode;
    }

    public void setExpressCompanyCode(String expressCompanyCode) {
        this.expressCompanyCode = expressCompanyCode;
    }

    public String getExpressCompanyName() {
        return expressCompanyName;
    }

    public void setExpressCompanyName(String expressCompanyName) {
        this.expressCompanyName = expressCompanyName;
    }

    public String getTrackingNo() {
        return trackingNo;
    }

    public void setTrackingNo(String trackingNo) {
        this.trackingNo = trackingNo;
    }

    public String getReceiverPhoneSuffix() {
        return receiverPhoneSuffix;
    }

    public void setReceiverPhoneSuffix(String receiverPhoneSuffix) {
        this.receiverPhoneSuffix = receiverPhoneSuffix;
    }
}

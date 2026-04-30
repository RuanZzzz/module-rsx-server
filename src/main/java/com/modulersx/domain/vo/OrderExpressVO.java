package com.modulersx.domain.vo;

import java.time.LocalDateTime;

public class OrderExpressVO {

    private String expressCompanyCode;
    private String expressCompanyName;
    private String trackingNo;
    private String receiverPhoneSuffix;
    private String latestStatus;
    private String latestLocation;
    private LocalDateTime latestTraceTime;

    public OrderExpressVO() {
    }

    public OrderExpressVO(
            String expressCompanyCode,
            String expressCompanyName,
            String trackingNo,
            String receiverPhoneSuffix,
            String latestStatus,
            String latestLocation,
            LocalDateTime latestTraceTime) {
        this.expressCompanyCode = expressCompanyCode;
        this.expressCompanyName = expressCompanyName;
        this.trackingNo = trackingNo;
        this.receiverPhoneSuffix = receiverPhoneSuffix;
        this.latestStatus = latestStatus;
        this.latestLocation = latestLocation;
        this.latestTraceTime = latestTraceTime;
    }

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

    public String getLatestStatus() {
        return latestStatus;
    }

    public void setLatestStatus(String latestStatus) {
        this.latestStatus = latestStatus;
    }

    public String getLatestLocation() {
        return latestLocation;
    }

    public void setLatestLocation(String latestLocation) {
        this.latestLocation = latestLocation;
    }

    public LocalDateTime getLatestTraceTime() {
        return latestTraceTime;
    }

    public void setLatestTraceTime(LocalDateTime latestTraceTime) {
        this.latestTraceTime = latestTraceTime;
    }
}

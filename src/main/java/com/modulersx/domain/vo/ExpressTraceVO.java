package com.modulersx.domain.vo;

import java.util.List;

public class ExpressTraceVO {

    private String provider;
    private String expressCompanyCode;
    private String expressCompanyName;
    private String trackingNo;
    private String latestLocation;
    private String latestStatus;
    private List<ExpressTraceItemVO> traces;

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
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

    public String getLatestLocation() {
        return latestLocation;
    }

    public void setLatestLocation(String latestLocation) {
        this.latestLocation = latestLocation;
    }

    public String getLatestStatus() {
        return latestStatus;
    }

    public void setLatestStatus(String latestStatus) {
        this.latestStatus = latestStatus;
    }

    public List<ExpressTraceItemVO> getTraces() {
        return traces;
    }

    public void setTraces(List<ExpressTraceItemVO> traces) {
        this.traces = traces;
    }
}

package com.modulersx.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName("biz_order_express")
public class OrderExpressPO {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("order_id")
    private Long orderId;

    @TableField("express_company_code")
    private String expressCompanyCode;

    @TableField("express_company_name")
    private String expressCompanyName;

    @TableField("tracking_no")
    private String trackingNo;

    @TableField("receiver_phone_suffix")
    private String receiverPhoneSuffix;

    @TableField("latest_status")
    private String latestStatus;

    @TableField("latest_location")
    private String latestLocation;

    @TableField("latest_trace_time")
    private LocalDateTime latestTraceTime;

    @TableField("raw_response")
    private String rawResponse;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
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

    public String getRawResponse() {
        return rawResponse;
    }

    public void setRawResponse(String rawResponse) {
        this.rawResponse = rawResponse;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

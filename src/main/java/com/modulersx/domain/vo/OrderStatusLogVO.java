package com.modulersx.domain.vo;

import java.time.LocalDateTime;

public class OrderStatusLogVO {

    private String fromStatus;
    private String toStatus;
    private String operator;
    private String remark;
    private LocalDateTime createdAt;

    public OrderStatusLogVO() {
    }

    public OrderStatusLogVO(String fromStatus, String toStatus, String operator, String remark, LocalDateTime createdAt) {
        this.fromStatus = fromStatus;
        this.toStatus = toStatus;
        this.operator = operator;
        this.remark = remark;
        this.createdAt = createdAt;
    }

    public String getFromStatus() {
        return fromStatus;
    }

    public void setFromStatus(String fromStatus) {
        this.fromStatus = fromStatus;
    }

    public String getToStatus() {
        return toStatus;
    }

    public void setToStatus(String toStatus) {
        this.toStatus = toStatus;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

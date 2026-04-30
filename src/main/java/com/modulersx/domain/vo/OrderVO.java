package com.modulersx.domain.vo;

import java.time.LocalDateTime;
import java.util.List;

public class OrderVO {

    private Long id;
    private String orderNo;
    private String customerName;
    private String productName;
    private Integer quantity;
    private String status;
    private String remark;
    private OrderExpressVO express;
    private List<OrderStatusLogVO> statusLogs;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public OrderExpressVO getExpress() {
        return express;
    }

    public void setExpress(OrderExpressVO express) {
        this.express = express;
    }

    public List<OrderStatusLogVO> getStatusLogs() {
        return statusLogs;
    }

    public void setStatusLogs(List<OrderStatusLogVO> statusLogs) {
        this.statusLogs = statusLogs;
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

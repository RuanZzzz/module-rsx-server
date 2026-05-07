package com.modulersx.domain.dto;

import java.math.BigDecimal;
import java.util.Map;

public class WindowDesignSaveDTO {

    private String name;
    private String templateCode;
    private String customerName;
    private Map<String, BigDecimal> params;
    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Map<String, BigDecimal> getParams() {
        return params;
    }

    public void setParams(Map<String, BigDecimal> params) {
        this.params = params;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

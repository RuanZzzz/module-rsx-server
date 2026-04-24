package com.modulersx.domain.vo;

public class ModuleVO {

    private String code;
    private String name;
    private String type;
    private String status;
    private String remark;

    public ModuleVO() {
    }

    public ModuleVO(String code, String name, String type, String status, String remark) {
        this.code = code;
        this.name = name;
        this.type = type;
        this.status = status;
        this.remark = remark;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
}

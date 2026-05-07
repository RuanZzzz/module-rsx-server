package com.modulersx.domain.vo;

import java.math.BigDecimal;

public class WindowGlassPieceVO {

    private String name;
    private String shapeType;
    private String shapeDataJson;
    private BigDecimal area;
    private Integer sortOrder;

    public WindowGlassPieceVO() {
    }

    public WindowGlassPieceVO(String name, String shapeType, String shapeDataJson, BigDecimal area, Integer sortOrder) {
        this.name = name;
        this.shapeType = shapeType;
        this.shapeDataJson = shapeDataJson;
        this.area = area;
        this.sortOrder = sortOrder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShapeType() {
        return shapeType;
    }

    public void setShapeType(String shapeType) {
        this.shapeType = shapeType;
    }

    public String getShapeDataJson() {
        return shapeDataJson;
    }

    public void setShapeDataJson(String shapeDataJson) {
        this.shapeDataJson = shapeDataJson;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
}

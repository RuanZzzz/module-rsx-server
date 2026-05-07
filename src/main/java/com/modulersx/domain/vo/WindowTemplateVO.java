package com.modulersx.domain.vo;

public class WindowTemplateVO {

    private Long id;
    private String code;
    private String name;
    private String shapeType;
    private String description;
    private String schemaJson;
    private String status;

    public WindowTemplateVO() {
    }

    public WindowTemplateVO(Long id, String code, String name, String shapeType, String description, String schemaJson, String status) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.shapeType = shapeType;
        this.description = description;
        this.schemaJson = schemaJson;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getShapeType() {
        return shapeType;
    }

    public void setShapeType(String shapeType) {
        this.shapeType = shapeType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSchemaJson() {
        return schemaJson;
    }

    public void setSchemaJson(String schemaJson) {
        this.schemaJson = schemaJson;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

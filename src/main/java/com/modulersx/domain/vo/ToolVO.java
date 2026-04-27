package com.modulersx.domain.vo;

public class ToolVO {

    private Long id;
    private String name;
    private String category;
    private String url;
    private String description;
    private String status;

    public ToolVO() {
    }

    public ToolVO(Long id, String name, String category, String url, String description, String status) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.url = url;
        this.description = description;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

package com.modulersx.domain.vo;

public class FileResourceVO {

    private Long id;
    private String originalName;
    private String url;
    private String contentType;
    private Long size;

    public FileResourceVO() {
    }

    public FileResourceVO(Long id, String originalName, String url, String contentType, Long size) {
        this.id = id;
        this.originalName = originalName;
        this.url = url;
        this.contentType = contentType;
        this.size = size;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}

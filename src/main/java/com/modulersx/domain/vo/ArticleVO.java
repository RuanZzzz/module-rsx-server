package com.modulersx.domain.vo;

public class ArticleVO {

    private Long id;
    private String title;
    private String summary;
    private String content;
    private String status;

    public ArticleVO() {
    }

    public ArticleVO(Long id, String title, String summary, String content, String status) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.content = content;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

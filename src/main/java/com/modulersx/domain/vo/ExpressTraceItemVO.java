package com.modulersx.domain.vo;

public class ExpressTraceItemVO {

    private String time;
    private String location;
    private String description;

    public ExpressTraceItemVO() {
    }

    public ExpressTraceItemVO(String time, String location, String description) {
        this.time = time;
        this.location = location;
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

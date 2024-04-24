package com.hcmus.manage_employee.models;

public class AllowanceModel {
    String url;
    String name;
    String duration;

    public AllowanceModel(String url, String name, String duration) {
        this.url = url;
        this.name = name;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getDuration() {
        return duration;
    }
}

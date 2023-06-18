package edu.northeastern.mainactivity.entity;

import java.io.Serializable;

public class Link implements Serializable {
    private String title;
    private String url;
    private String description;

    public Link(String title, String url, String description) {
        this.title = title;
        this.url = url;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }
}

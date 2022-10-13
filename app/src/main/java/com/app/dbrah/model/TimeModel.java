package com.app.dbrah.model;

import java.io.Serializable;

public class TimeModel implements Serializable {
    private String id;
    private String title;
    private long from;
    private long to;
    private String created_at;
    private String updated_at;

    public TimeModel(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public long getFrom() {
        return from;
    }

    public long getTo() {
        return to;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
}

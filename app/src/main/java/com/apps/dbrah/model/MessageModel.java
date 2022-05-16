package com.apps.dbrah.model;

import java.io.Serializable;

public class MessageModel implements Serializable {
    private String  id;
    private String  provider_id;
    private String user_id;
    private String order_id;
    private String from_type;
    private String message;
    private String file;
    private String type;
    private String created_at;
    private String updated_at;

    public MessageModel(String id, String provider_id, String user_id, String order_id, String from_type, String message, String file, String type, String created_at, String updated_at) {
        this.id = id;
        this.provider_id = provider_id;
        this.user_id = user_id;
        this.order_id = order_id;
        this.from_type = from_type;
        this.message = message;
        this.file = file;
        this.type = type;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getId() {
        return id;
    }

    public String getProvider_id() {
        return provider_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getFrom_type() {
        return from_type;
    }

    public String getMessage() {
        return message;
    }

    public String getFile() {
        return file;
    }

    public String getType() {
        return type;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
}

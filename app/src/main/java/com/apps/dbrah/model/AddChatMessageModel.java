package com.apps.dbrah.model;

import java.io.Serializable;

public class AddChatMessageModel implements Serializable {
    private String order_id;
    private String user_id;
    private String provider_id;
    private String type;
    private String message ="";
    private String image;

    public AddChatMessageModel(String order_id, String user_id, String provider_id, String type, String message, String image) {
        this.order_id = order_id;
        this.user_id = user_id;
        this.provider_id = provider_id;
        this.type = type;
        this.message = message;
        this.image = image;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(String provider_id) {
        this.provider_id = provider_id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public String getImage() {
        return image;
    }
}

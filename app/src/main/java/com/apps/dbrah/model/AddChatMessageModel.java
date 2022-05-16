package com.apps.dbrah.model;

import java.io.Serializable;

public class AddChatMessageModel implements Serializable {

    private String type;
    private String text = "";
    private String image;
    private String order_id;
    private String provider_id;
    private String user_id;
    private String representative_id;

    public AddChatMessageModel(String type, String text, String image, String order_id, String provider_id, String user_id, String representative_id) {
        this.type = type;
        this.text = text;
        this.image = image;
        this.order_id = order_id;
        this.provider_id = provider_id;
        this.user_id = user_id;
        this.representative_id = representative_id;
    }

    public String getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public String getImage() {
        return image;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getProvider_id() {
        return provider_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getRepresentative_id() {
        return representative_id;
    }
}

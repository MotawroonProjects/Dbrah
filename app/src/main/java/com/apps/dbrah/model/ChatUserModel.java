package com.apps.dbrah.model;

import java.io.Serializable;

public class ChatUserModel implements Serializable {
    private String provider_id;
    private String user_id;
    private String representative_id;
    private String user_name;
    private String user_phone;
    private String user_image;
    private String order_id;


    public ChatUserModel(String provider_id, String user_id, String representative_id, String user_name, String user_phone, String user_image, String order_id) {
        this.provider_id = provider_id;
        this.user_id = user_id;
        this.representative_id = representative_id;
        this.user_name = user_name;
        this.user_phone = user_phone;
        this.user_image = user_image;
        this.order_id = order_id;
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

    public String getUser_name() {
        return user_name;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public String getUser_image() {
        return user_image;
    }

    public String getOrder_id() {
        return order_id;
    }
}

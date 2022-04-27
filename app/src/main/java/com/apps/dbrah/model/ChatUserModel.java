package com.apps.dbrah.model;

import java.io.Serializable;

public class ChatUserModel implements Serializable {
    private String provider_id;
    private String provider_name;
    private String provider_phone;
    private String provider_image;
    private String order_id;


    public ChatUserModel(String provider_id, String provider_name, String provider_phone, String provider_image, String order_id) {
        this.provider_id = provider_id;
        this.provider_name = provider_name;
        this.provider_phone = provider_phone;
        this.provider_image = provider_image;
        this.order_id = order_id;
    }

    public String getProvider_id() {
        return provider_id;
    }

    public String getProvider_name() {
        return provider_name;
    }

    public String getProvider_phone() {
        return provider_phone;
    }

    public String getProvider_image() {
        return provider_image;
    }

    public String getOrder_id() {
        return order_id;
    }
}

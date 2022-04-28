package com.apps.dbrah.model;

import java.io.Serializable;

public class NotificationModel implements Serializable {
    private String id;
    private String title;
    private String body;
    private String user_id;
    private String provider_id;
    private String order_id;
    private String status;
    private String created_at;
    private String updated_at;
    private UserModel.Data user;
    private ProviderModel provider;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getProvider_id() {
        return provider_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getStatus() {
        return status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public UserModel.Data getUser() {
        return user;
    }

    public ProviderModel getProvider() {
        return provider;
    }
}

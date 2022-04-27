package com.apps.dbrah.model;

import java.io.Serializable;

public class MessageModel implements Serializable {
    private String id;
    private String order_id;
    private String provider_id;
    private String user_id;
    private String type;
    private String message;
    private String from_type;
    private String file;
    private UserModel.Data user;
    private ProviderModel provider;
    private String created_at;

    public MessageModel() {
    }

    public MessageModel(String id, String order_id, String provider_id, String user_id, String type, String message, String from_type, String file, UserModel.Data user, ProviderModel provider,String created_at) {
        this.id = id;
        this.order_id = order_id;
        this.provider_id = provider_id;
        this.user_id = user_id;
        this.type = type;
        this.message = message;
        this.from_type = from_type;
        this.file = file;
        this.user = user;
        this.provider = provider;
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(String provider_id) {
        this.provider_id = provider_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFrom_type() {
        return from_type;
    }

    public void setFrom_type(String from_type) {
        this.from_type = from_type;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public UserModel.Data getUser() {
        return user;
    }

    public void setUser(UserModel.Data user) {
        this.user = user;
    }

    public ProviderModel getProvider() {
        return provider;
    }

    public void setProvider(ProviderModel provider) {
        this.provider = provider;
    }

    public String getCreated_at() {
        return created_at;
    }
}

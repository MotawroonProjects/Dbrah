package com.apps.dbrah.model;

import java.io.Serializable;

public class AddressModel implements Serializable {
    private String id;
    private String user_id;
    private String title;
    private String admin_name;
    private String phone;
    private String address;
    private String time_id;
    private String latitude;
    private String longitude;
    private String is_default;
    private String created_at;
    private String updated_at;
    private String from;
    private String to;

    public String getId() {
        return id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getTitle() {
        return title;
    }

    public String getAdmin_name() {
        return admin_name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getTime_id() {
        return time_id;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getIs_default() {
        return is_default;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}

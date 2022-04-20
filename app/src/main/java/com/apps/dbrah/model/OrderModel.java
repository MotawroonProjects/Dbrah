package com.apps.dbrah.model;

import java.io.Serializable;

public class OrderModel implements Serializable {
    private String id;
    private String user_id;
    private String address_id;
    private String category_id;
    private String provider_id;
    private String note;
    private String pin;
    private String status;
    private String total_price;
    private String delivered_time;
    private String created_at;
    private String updated_at;
    private CategoryModel category;
    private AddressModel address;

    public String getId() {
        return id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getAddress_id() {
        return address_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public String getProvider_id() {
        return provider_id;
    }

    public String getNote() {
        return note;
    }

    public String getPin() {
        return pin;
    }

    public String getStatus() {
        return status;
    }

    public String getTotal_price() {
        return total_price;
    }

    public String getDelivered_time() {
        return delivered_time;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public CategoryModel getCategory() {
        return category;
    }

    public AddressModel getAddress() {
        return address;
    }
}

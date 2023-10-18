package com.app.dbrah.model.cart_models;

import com.app.dbrah.model.CategoryModel;
import com.app.dbrah.model.ProductModel;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CartSingleModel implements Serializable {
    private String delivery_date_time_id;
    private String delivery_date_time;
    private String time;
    private String date;
    private String user_id;
    private String address_id;
    private String note = "";
    @SerializedName("details")
    private List<CartModel.CartObject> cartList;

    public CartSingleModel() {
        cartList = new ArrayList<>();
    }

    public void addItem(CartModel.CartObject cartObject){
        this.cartList.add(0,cartObject);
    }
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<CartModel.CartObject> getCartList() {
        return cartList;
    }

    public void setCartList(List<CartModel.CartObject> cartList) {
        this.cartList = cartList;
    }

    public String getDelivery_date_time_id() {
        return delivery_date_time_id;
    }

    public String getDelivery_date_time() {
        return delivery_date_time;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public void setDelivery_date_time_id(String delivery_date_time_id) {
        this.delivery_date_time_id = delivery_date_time_id;
    }

    public void setDelivery_date_time(String delivery_date_time) {
        this.delivery_date_time = delivery_date_time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

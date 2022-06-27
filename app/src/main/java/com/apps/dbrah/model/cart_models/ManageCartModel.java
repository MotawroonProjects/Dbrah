package com.apps.dbrah.model.cart_models;

import android.content.Context;
import android.util.Log;

import com.apps.dbrah.model.OrderModel;
import com.apps.dbrah.model.ProductModel;
import com.apps.dbrah.preferences.Preferences;

import java.io.Serializable;
import java.util.List;

public class ManageCartModel implements Serializable {
    public static ManageCartModel instance = null;
    private CartModel cartModel;

    private ManageCartModel() {
    }

    public static synchronized ManageCartModel newInstance() {
        if (instance == null) {
            instance = new ManageCartModel();
        }

        return instance;
    }

    public void add(ProductModel productModel, Context context) {
        Preferences preferences = Preferences.getInstance();

        cartModel = preferences.getCart(context);
        if (cartModel == null) {
            cartModel = new CartModel();
        }
        cartModel.addProduct(productModel);
        preferences.create_update_cart(context, cartModel);
    }

    public void addAddress(String address_id, Context context) {
        Preferences preferences = Preferences.getInstance();

        cartModel = preferences.getCart(context);
        if (cartModel == null) {
            cartModel = new CartModel();
        }
        cartModel.setAddress_id(address_id);
        preferences.create_update_cart(context, cartModel);
    }

    public void addUser(String user_id, Context context) {
        Preferences preferences = Preferences.getInstance();

        cartModel = preferences.getCart(context);
        if (cartModel == null) {
            cartModel = new CartModel();
        }
        cartModel.setUser_id(user_id);
        preferences.create_update_cart(context, cartModel);
    }

    public void delete(ProductModel productModel, Context context) {
        Preferences preferences = Preferences.getInstance();

        cartModel = preferences.getCart(context);
        if (cartModel == null) {
            cartModel = new CartModel();
        }
        cartModel.removeProduct(productModel);
        preferences.create_update_cart(context, cartModel);

    }

    public void deleteMainCategory(int pos, Context context) {
        Preferences preferences = Preferences.getInstance();

        cartModel = preferences.getCart(context);
        if (cartModel == null) {
            cartModel = new CartModel();
        }
        cartModel.removeCategory(pos);
        preferences.create_update_cart(context, cartModel);

    }

    public int getItemsCount(Context context){
        int count = 0;
        for (CartModel.CartObject object:getCartList(context)){
            count += object.getProducts().size();
        }

        return count;
    }

    public List<CartModel.CartObject> getCartList(Context context) {
        Preferences preferences = Preferences.getInstance();

        cartModel = preferences.getCart(context);
        if (cartModel == null) {
            cartModel = new CartModel();

        }

        return cartModel.getCartList();
    }

    public int getProductAmount(String product_id, Context context) {
        Preferences preferences = Preferences.getInstance();
        cartModel = preferences.getCart(context);
        if (cartModel != null) {
            return cartModel.getProductAmount(product_id);
        } else {
            return 0;
        }

    }

    public void resend(OrderModel orderModel, Context context) {
        Preferences preferences = Preferences.getInstance();

        cartModel = preferences.getCart(context);
        if (cartModel == null) {
            cartModel = new CartModel();
        }
        cartModel.reOrder(orderModel);
        preferences.create_update_cart(context, cartModel);

    }


    public CartModel getCartModel() {
        if (cartModel != null) {
            return cartModel;
        }
        return null;
    }

    public void clear(Context context) {
        instance = null;
        cartModel = null;
        Preferences preferences = Preferences.getInstance();
        preferences.create_update_cart(context, cartModel);
    }

}

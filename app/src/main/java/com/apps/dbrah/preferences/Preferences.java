package com.apps.dbrah.preferences;

import android.content.Context;
import android.content.SharedPreferences;


import com.apps.dbrah.model.ChatUserModel;
import com.apps.dbrah.model.UserModel;
import com.apps.dbrah.model.UserSettingsModel;
import com.apps.dbrah.model.cart_models.CartModel;
import com.google.gson.Gson;

public class Preferences {

    private static Preferences instance = null;

    private Preferences() {
    }

    public static Preferences getInstance() {
        if (instance == null) {
            instance = new Preferences();
        }
        return instance;
    }

    public UserModel getUserData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String user_data = preferences.getString("user_data", "");
        UserModel userModel = gson.fromJson(user_data, UserModel.class);
        return userModel;
    }

    public void createUpdateUserData(Context context, UserModel userModel) {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String user_data = gson.toJson(userModel);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_data", user_data);
        editor.apply();

    }

    public void clearUserData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

    }

    public void create_update_user_settings(Context context, UserSettingsModel model) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("settings_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String data = new Gson().toJson(model);
        editor.putString("settings", data);
        editor.apply();


    }

    public UserSettingsModel getUserSettings(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("settings_pref", Context.MODE_PRIVATE);
        UserSettingsModel model = new Gson().fromJson(preferences.getString("settings", ""), UserSettingsModel.class);
        return model;

    }


    public void create_update_cart(Context context, CartModel cartModel) {
        SharedPreferences preferences = context.getSharedPreferences("cart", Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String user_data = gson.toJson(cartModel);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("cart_data", user_data);
        editor.apply();


    }

    public CartModel getCart(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("cart", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String user_data = preferences.getString("cart_data", "");
        return gson.fromJson(user_data, CartModel.class);

    }


    public void create_update_room(Context context, ChatUserModel model) {
        SharedPreferences preferences = context.getSharedPreferences("room", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String data = new Gson().toJson(model);
        editor.putString("order_id", data);
        editor.apply();


    }

    public ChatUserModel getRoomId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("room", Context.MODE_PRIVATE);
        ChatUserModel model = new Gson().fromJson(preferences.getString("order_id",""),ChatUserModel.class);

        return model;
    }

    public void clearRoomId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("room", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

}

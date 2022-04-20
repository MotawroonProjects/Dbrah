package com.apps.dbrah.model.cart_models;

import com.apps.dbrah.model.StatusResponse;

import java.io.Serializable;
import java.util.List;

public class CartResponse extends StatusResponse implements Serializable {
    private List<Integer> categories_id;

    public List<Integer> getCategories_id() {
        return categories_id;
    }
}

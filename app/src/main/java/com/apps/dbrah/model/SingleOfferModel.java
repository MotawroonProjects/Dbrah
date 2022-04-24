package com.apps.dbrah.model;

import java.io.Serializable;

public class SingleOfferModel extends StatusResponse implements Serializable {
    private OrderModel.Offers data;

    public OrderModel.Offers getData() {
        return data;
    }
}

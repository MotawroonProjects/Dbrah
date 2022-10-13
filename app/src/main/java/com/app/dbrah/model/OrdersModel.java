package com.app.dbrah.model;

import java.io.Serializable;
import java.util.List;

public class OrdersModel extends StatusResponse implements Serializable {
    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data implements Serializable {
        private List<OrderModel> new_orders;
        private List<OrderModel> old;

        public List<OrderModel> getNew_orders() {
            return new_orders;
        }

        public List<OrderModel> getOld() {
            return old;
        }
    }
}

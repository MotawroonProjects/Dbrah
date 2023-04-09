package com.app.dbrah.model;

import java.io.Serializable;
import java.util.List;

public class PayDataModel extends StatusResponse implements Serializable {
   Data data;

    public Data getData() {
        return data;
    }

    public class Data implements Serializable{
       private String payment_url;

        public String getPayment_url() {
            return payment_url;
        }
    }
}

package com.apps.dbrah.model;

import java.io.Serializable;
import java.util.List;

public class SliderDataModel extends StatusResponse implements Serializable {
    private List<SliderModel> data;


    public List<SliderModel> getData() {
        return data;
    }

    public static class SliderModel implements Serializable{
        private String id;
        private String product_id;
        private String image;
        private String end_at;
        private String created_at;
        private String updated_at;

        public String getImage() {
            return image;
        }
    }
}

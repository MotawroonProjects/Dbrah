package com.apps.dbrah.model;

import java.io.Serializable;
import java.util.List;

public class RecentProductDataModel extends StatusResponse implements Serializable {
    private List<RecentProductModel> data;

    public List<RecentProductModel> getData() {
        return data;
    }

    public static class RecentProductModel implements Serializable{
        private String id;
        private String main_image;
        private String title_ar;
        private String title_en;

        public String getId() {
            return id;
        }

        public String getMain_image() {
            return main_image;
        }

        public String getTitle_ar() {
            return title_ar;
        }

        public String getTitle_en() {
            return title_en;
        }
    }
}

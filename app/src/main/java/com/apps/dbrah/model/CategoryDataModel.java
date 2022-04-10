package com.apps.dbrah.model;

import java.io.Serializable;
import java.util.List;

public class CategoryDataModel extends StatusResponse implements Serializable {
    private List<CategoryModel> data;

    public List<CategoryModel> getData() {
        return data;
    }

    public static class CategoryModel implements Serializable {
        private String id;
        private String title_ar;
        private String title_en;
        private String image;
        private String level;
        private String created_at;
        private String updated_at;
        private boolean selected;

        public String getId() {
            return id;
        }

        public String getTitle_ar() {
            return title_ar;
        }

        public String getTitle_en() {
            return title_en;
        }

        public String getImage() {
            return image;
        }

        public String getLevel() {
            return level;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }
}

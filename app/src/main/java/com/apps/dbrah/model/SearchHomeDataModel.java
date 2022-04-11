package com.apps.dbrah.model;

import java.io.Serializable;
import java.util.List;

public class SearchHomeDataModel extends StatusResponse implements Serializable {
    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data implements Serializable{
        private List<CategoryModel> categories;
        private List<ProductModel> products;

        public List<CategoryModel> getCategories() {
            return categories;
        }

        public List<ProductModel> getProducts() {
            return products;
        }
    }
}

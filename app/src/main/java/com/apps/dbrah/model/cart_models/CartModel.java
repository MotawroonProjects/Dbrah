package com.apps.dbrah.model.cart_models;

import android.content.Context;
import android.util.Log;

import com.apps.dbrah.model.CategoryModel;
import com.apps.dbrah.model.OrderModel;
import com.apps.dbrah.model.ProductModel;
import com.apps.dbrah.preferences.Preferences;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CartModel implements Serializable {
    private String user_id;
    private String address_id;
    private String note = "";

    @SerializedName("details")
    private List<CartObject> cartList;

    public List<CartObject> getCartList() {
        return cartList;
    }

    public CartModel() {
        cartList = new ArrayList<>();
    }

    public void setCartList(List<CartObject> cartList) {
        this.cartList = cartList;
    }

    public static class CartObject implements Serializable {
        private String category_id;
        private CategoryModel categoryModel;
        private List<ProductModel> products;

        public CartObject() {
            products = new ArrayList<>();
        }

        public CartObject(CategoryModel categoryModel, List<ProductModel> products) {
            this.categoryModel = categoryModel;
            this.products = products;
        }

        public CategoryModel getCategoryModel() {
            return categoryModel;
        }

        public void setCategoryModel(CategoryModel categoryModel) {
            this.categoryModel = categoryModel;
        }

        public String getCategory_id() {
            return category_id;
        }

        public void setCategory_id(String category_id) {
            this.category_id = category_id;
        }

        public List<ProductModel> getProducts() {
            return products;
        }

        public void setProducts(List<ProductModel> products) {
            this.products = products;
        }
    }

    public void addProduct(ProductModel model) {

        CategoryModel categoryModel = model.getCategoryModel();
        int categoryPos = isCategoryExist(categoryModel.getId());
        if (categoryPos != -1) {
            CartObject cartObject = cartList.get(categoryPos);
            List<ProductModel> products = cartObject.getProducts();
            int productPos = isProductExistInCategory(model.getId(), products);

            if (productPos != -1) {
                products.set(productPos, model);
            } else {
                products.add(model);


            }

            cartObject.setProducts(products);
            cartList.set(categoryPos, cartObject);

        } else {
            List<ProductModel> products = new ArrayList<>();
            products.add(model);
            CartObject cartObject = new CartObject(categoryModel, products);
            cartObject.setCategory_id(categoryModel.getId());
            cartList.add(cartObject);
        }
    }

    public void removeProduct(ProductModel model) {
        CategoryModel categoryModel = model.getCategoryModel();
        int categoryPos = isCategoryExist(categoryModel.getId());
        CartObject cartObject = cartList.get(categoryPos);
        List<ProductModel> products = cartObject.getProducts();
        int productPos = isProductExistInCategory(model.getId(), products);
        products.remove(productPos);
        if (products.size() > 0) {
            cartObject.setProducts(products);
            cartList.set(categoryPos, cartObject);
        } else {
            cartList.remove(categoryPos);
        }

    }

    public void removeCategory(int pos) {
        try {
            cartList.remove(pos);
        } catch (Exception e) {

        }

    }

    public void reOrder(OrderModel orderModel) {

        if (orderModel.getAccepted_offer() != null) {
            CartObject cartObject = new CartObject();
            cartObject.setCategory_id(orderModel.getCategory_id());
            List<ProductModel> list = new ArrayList<>();
            for (OrderModel.OfferDetail offerDetail : orderModel.getAccepted_offer().getOffer_details()) {
                ProductModel productModel = offerDetail.getProduct();
                productModel.setAmount(Integer.parseInt(offerDetail.getQty()));
                list.add(productModel);
            }
            cartObject.setCategoryModel(orderModel.getCategory());
            cartObject.setProducts(list);


            cartList.add(cartObject);
        }
    }

    private int isCategoryExist(String category_id) {
        for (int index = 0; index < cartList.size(); index++) {
            CartObject cartObject = cartList.get(index);
            if (cartObject.getCategoryModel().getId().equals(category_id)) {
                return index;
            }
        }
        return -1;
    }

    private int isProductExistInCategory(String product_id, List<ProductModel> list) {
        for (int index = 0; index < list.size(); index++) {
            ProductModel productModel = list.get(index);
            if (productModel.getId().equals(product_id)) {
                return index;
            }
        }
        return -1;
    }

    public int getProductAmount(String product_id) {
        for (CartObject cartObject : cartList) {
            List<ProductModel> products = cartObject.getProducts();
            for (ProductModel productModel : products) {
                if (productModel.getId().equals(product_id)) {
                    return productModel.getAmount();
                }
            }

        }
        return 0;
    }

    public CartObject getSingleOrderByPos(int pos) {
        try {
            return cartList.get(pos);
        } catch (Exception e) {

        }

        return null;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

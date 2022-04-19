package com.apps.dbrah.mvvm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.apps.dbrah.model.AddressModel;
import com.apps.dbrah.model.CategoryModel;
import com.apps.dbrah.model.ProductAmount;
import com.apps.dbrah.model.ProductModel;

import java.util.List;

public class GeneralMvvm extends AndroidViewModel {
    private MutableLiveData<Integer> actionHomeNavigator;
    private MutableLiveData<Integer> actionFragmentHomeNavigator;
    private MutableLiveData<Boolean> actionHomeBackNavigator;
    private MutableLiveData<ProductAmount> product_amount;
    private MutableLiveData<Integer> category_pos;
    private MutableLiveData<List<CategoryModel>> categoryList;
    private MutableLiveData<Boolean> onCartRefreshed;
    private MutableLiveData<ProductModel> onCartItemUpdated;
    private MutableLiveData<AddressModel> onAddressSelectedForUpdate;
    private MutableLiveData<AddressModel> onAddressSelectedForOrder;
    private MutableLiveData<AddressModel> onAddressAdded;

    public GeneralMvvm(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Integer> onHomeNavigate() {
        if (actionHomeNavigator == null) {
            actionHomeNavigator = new MutableLiveData<>();
        }
        return actionHomeNavigator;
    }

    public MutableLiveData<Integer> getActionFragmentHomeNavigator() {
        if (actionFragmentHomeNavigator == null) {
            actionFragmentHomeNavigator = new MutableLiveData<>();
        }
        return actionFragmentHomeNavigator;
    }

    public MutableLiveData<Boolean> onHomeBackNavigate() {
        if (actionHomeBackNavigator == null) {
            actionHomeBackNavigator = new MutableLiveData<>();
        }
        return actionHomeBackNavigator;
    }

    public MutableLiveData<ProductAmount> getProductAmount() {
        if (product_amount == null) {
            product_amount = new MutableLiveData<>();
        }
        return product_amount;
    }

    public MutableLiveData<Integer> getCategory_pos() {
        if (category_pos == null) {
            category_pos = new MutableLiveData<>();
            category_pos.setValue(-1);
        }
        return category_pos;
    }

    public MutableLiveData<List<CategoryModel>> getCategoryList() {
        if (categoryList == null) {
            categoryList = new MutableLiveData<>();
        }
        return categoryList;
    }

    public MutableLiveData<Boolean> getOnCartRefreshed() {
        if (onCartRefreshed == null) {
            onCartRefreshed = new MutableLiveData<>();
        }
        return onCartRefreshed;
    }

    public MutableLiveData<ProductModel> getOnCartItemUpdated() {
        if (onCartItemUpdated == null) {
            onCartItemUpdated = new MutableLiveData<>();
        }
        return onCartItemUpdated;
    }

    public MutableLiveData<AddressModel> getOnAddressSelectedForUpdate() {
        if (onAddressSelectedForUpdate == null) {
            onAddressSelectedForUpdate = new MutableLiveData<>();
        }
        return onAddressSelectedForUpdate;
    }

    public MutableLiveData<AddressModel> getOnAddressSelectedForOrder() {
        if (onAddressSelectedForOrder == null) {
            onAddressSelectedForOrder = new MutableLiveData<>();
        }
        return onAddressSelectedForOrder;
    }

    public MutableLiveData<AddressModel> getOnAddressAdded() {
        if (onAddressAdded == null) {
            onAddressAdded = new MutableLiveData<>();
        }
        return onAddressAdded;
    }
    


}

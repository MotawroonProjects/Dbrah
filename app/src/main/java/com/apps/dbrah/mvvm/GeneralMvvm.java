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
    private MutableLiveData<Boolean> onCartMyListRefreshed;

    private MutableLiveData<ProductModel> onCartItemUpdated;
    //////////////////////////////////////////////////////////////////
    private MutableLiveData<AddressModel> onAddressSelectedForOrder;
    private MutableLiveData<AddressModel> onAddressAdded;
    private MutableLiveData<AddressModel> onAddressUpdated;
    private MutableLiveData<String> addAddressFragmentAction;
    private MutableLiveData<String> myAddressFragmentAction;
    /////////////////////////////////////////////////////////////////
    private MutableLiveData<ProductModel> onProductItemUpdated;


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

    public MutableLiveData<Boolean> getOnCartMyListRefreshed() {
        if (onCartMyListRefreshed == null) {
            onCartMyListRefreshed = new MutableLiveData<>();
        }
        return onCartMyListRefreshed;
    }

    public MutableLiveData<ProductModel> getOnCartItemUpdated()
    {
        if (onCartItemUpdated == null) {
            onCartItemUpdated = new MutableLiveData<>();
        }
        return onCartItemUpdated;
    }

    public MutableLiveData<ProductModel> getOnProductItemUpdated()
    {
        if (onProductItemUpdated == null) {
            onProductItemUpdated = new MutableLiveData<>();
        }
        return onProductItemUpdated;
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

    public MutableLiveData<AddressModel> getOnAddressUpdated() {
        if (onAddressUpdated == null) {
            onAddressUpdated = new MutableLiveData<>();
        }
        return onAddressUpdated;
    }

    public MutableLiveData<String> getAddAddressFragmentAction() {
        if (addAddressFragmentAction == null) {
            addAddressFragmentAction = new MutableLiveData<>();
        }
        return addAddressFragmentAction;
    }


    public MutableLiveData<String> getMyAddressFragmentAction() {
        if (myAddressFragmentAction == null) {
            myAddressFragmentAction = new MutableLiveData<>();
        }
        return myAddressFragmentAction;
    }


}

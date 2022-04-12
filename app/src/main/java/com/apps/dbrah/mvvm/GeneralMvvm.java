package com.apps.dbrah.mvvm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.apps.dbrah.model.CategoryModel;

import java.util.List;

public class GeneralMvvm extends AndroidViewModel {
    private MutableLiveData<Integer> actionHomeNavigator;
    private MutableLiveData<Boolean> actionHomeBackNavigator;
    private MutableLiveData<String> Product_id;
    private MutableLiveData<Integer> category_pos;
    private MutableLiveData<List<CategoryModel>> categoryList;

    public GeneralMvvm(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Integer> onHomeNavigate() {
        if (actionHomeNavigator == null) {
            actionHomeNavigator = new MutableLiveData<>();
        }
        return actionHomeNavigator;
    }

    public MutableLiveData<Boolean> onHomeBackNavigate() {
        if (actionHomeBackNavigator == null) {
            actionHomeBackNavigator = new MutableLiveData<>();
        }
        return actionHomeBackNavigator;
    }

    public MutableLiveData<String> getProduct_id() {
        if (Product_id == null) {
            Product_id = new MutableLiveData<>();
        }
        return Product_id;
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
}

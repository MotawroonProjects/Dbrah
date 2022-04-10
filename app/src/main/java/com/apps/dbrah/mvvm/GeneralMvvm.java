package com.apps.dbrah.mvvm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class GeneralMvvm extends AndroidViewModel {
    private MutableLiveData<Integer> actionHomeNavigator;
    private MutableLiveData<Boolean> actionHomeBackNavigator;
    private MutableLiveData<String> Product_id;
    private MutableLiveData<String> cat_id;


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
    public MutableLiveData<String> getCat_id() {
        if (cat_id == null) {
            cat_id = new MutableLiveData<>();
        }
        return cat_id;
    }
}

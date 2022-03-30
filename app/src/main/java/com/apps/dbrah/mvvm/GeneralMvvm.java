package com.apps.dbrah.mvvm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class GeneralMvvm extends AndroidViewModel {
    private MutableLiveData<Integer> actionHomeNavigator;
    private MutableLiveData<Boolean> actionHomeBackNavigator;


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




}

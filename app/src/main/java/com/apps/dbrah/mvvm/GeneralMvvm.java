package com.apps.dbrah.mvvm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class GeneralMvvm extends AndroidViewModel {
    private MutableLiveData<Integer> actionNotificationNavigator;
    private MutableLiveData<Boolean> actionMarketBackNavigator;
    private MutableLiveData<Integer> actionMarketForwardNavigator;
    private MutableLiveData<Boolean> actionMarketBottomNavSearchShow;

    public GeneralMvvm(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Integer> onNotificationNavigate() {
        if (actionNotificationNavigator == null) {
            actionNotificationNavigator = new MutableLiveData<>();
        }
        return actionNotificationNavigator;
    }

    public MutableLiveData<Boolean> onMarketFragmentBackNavigate() {
        if (actionMarketBackNavigator == null) {
            actionMarketBackNavigator = new MutableLiveData<>();
        }
        return actionMarketBackNavigator;
    }

    public MutableLiveData<Integer> onMarketFragmentForwardNavigate() {
        if (actionMarketForwardNavigator == null) {
            actionMarketForwardNavigator = new MutableLiveData<>();
        }
        return actionMarketForwardNavigator;
    }

    public MutableLiveData<Boolean> getActionMarketBottomNavSearchShow() {
        if (actionMarketBottomNavSearchShow == null) {
            actionMarketBottomNavSearchShow = new MutableLiveData<>();
        }
        return actionMarketBottomNavSearchShow;
    }


}

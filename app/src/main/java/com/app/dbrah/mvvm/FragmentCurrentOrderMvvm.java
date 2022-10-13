package com.app.dbrah.mvvm;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.app.dbrah.R;
import com.app.dbrah.model.OrderModel;
import com.app.dbrah.model.OrdersModel;
import com.app.dbrah.model.UserModel;
import com.app.dbrah.model.cart_models.CartModel;
import com.app.dbrah.model.cart_models.CartResponse;
import com.app.dbrah.model.cart_models.CartSingleModel;
import com.app.dbrah.remote.Api;
import com.app.dbrah.share.Common;
import com.app.dbrah.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class FragmentCurrentOrderMvvm extends AndroidViewModel {

    private CompositeDisposable disposable = new CompositeDisposable();
    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<List<OrderModel>> onDataSuccess;

    public FragmentCurrentOrderMvvm(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<OrderModel>> getOnDataSuccess() {
        if (onDataSuccess == null) {
            onDataSuccess = new MutableLiveData<>();
        }
        return onDataSuccess;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        if (isLoading == null) {
            isLoading = new MutableLiveData<>();
        }
        return isLoading;
    }


    public void getOrders(UserModel userModel,String type) {
        if (userModel == null) {
            getIsLoading().setValue(false);
            getOnDataSuccess().setValue(new ArrayList<>());
            return;
        }
        getIsLoading().setValue(true);
        Api.getService(Tags.base_url)
                .getOrders(userModel.getData().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<OrdersModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<OrdersModel> response) {
                        getIsLoading().setValue(false);
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body().getStatus() == 200) {
                                    if (type.equals("new")){
                                        getOnDataSuccess().setValue(response.body().getData().getNew_orders());

                                    }else {
                                        getOnDataSuccess().setValue(response.body().getData().getOld());

                                    }
                                }
                            }

                        } else {
                            try {
                                Log.e("error", response.errorBody().string() + "__" + response.code());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("error", e.getMessage());
                        getIsLoading().setValue(false);
                    }
                });
    }


}

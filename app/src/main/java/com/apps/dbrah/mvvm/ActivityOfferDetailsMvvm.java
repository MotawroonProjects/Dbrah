package com.apps.dbrah.mvvm;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.apps.dbrah.R;
import com.apps.dbrah.model.OrderModel;
import com.apps.dbrah.model.SingleOfferModel;
import com.apps.dbrah.model.SingleOrderDataModel;
import com.apps.dbrah.model.StatusResponse;
import com.apps.dbrah.remote.Api;
import com.apps.dbrah.share.Common;
import com.apps.dbrah.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ActivityOfferDetailsMvvm extends AndroidViewModel {

    private CompositeDisposable disposable = new CompositeDisposable();
    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<OrderModel.Offers> onDataSuccess;
    private MutableLiveData<String> onStatusSuccess;

    public ActivityOfferDetailsMvvm(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<OrderModel.Offers> getOnDataSuccess() {
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
    public MutableLiveData<String> getOnOrderStatusSuccess() {
        if (onStatusSuccess == null) {
            onStatusSuccess = new MutableLiveData<>();
        }
        return onStatusSuccess;
    }
    public void getOfferDetails(String offer_id) {
        getIsLoading().setValue(true);
        Api.getService(Tags.base_url)
                .getOfferDetails(offer_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<SingleOfferModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<SingleOfferModel> response) {
                        getIsLoading().setValue(false);
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body().getStatus() == 200) {
                                    getOnDataSuccess().setValue(response.body().getData());
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

    public void acceptRefuseOffer(String offer_id, String status, Context context) {
        ProgressDialog dialog = Common.createProgressDialog(context, context.getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Api.getService(Tags.base_url).acceptRefuseOffer(offer_id, status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<StatusResponse>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body().getStatus() == 200) {
                                    if (status.equals("accepted")) {
                                        getOnOrderStatusSuccess().setValue("accepted");
                                    } else {
                                        getOnOrderStatusSuccess().setValue("rejected");
                                    }

                                }
                            }
                        } else {

                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("error", e.getMessage());
                        dialog.dismiss();
                    }
                });
    }


}

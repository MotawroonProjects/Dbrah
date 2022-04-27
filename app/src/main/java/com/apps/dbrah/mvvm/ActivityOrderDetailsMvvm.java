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
import com.apps.dbrah.model.OrdersModel;
import com.apps.dbrah.model.SingleOrderDataModel;
import com.apps.dbrah.model.StatusResponse;
import com.apps.dbrah.model.UserModel;
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

public class ActivityOrderDetailsMvvm extends AndroidViewModel {

    private CompositeDisposable disposable = new CompositeDisposable();
    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<OrderModel> onDataSuccess;
    private MutableLiveData<Boolean> onOrderCanceled;


    public ActivityOrderDetailsMvvm(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<OrderModel> getOnDataSuccess() {
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

    public MutableLiveData<Boolean> getOnOrderCanceled() {
        if (onOrderCanceled == null) {
            onOrderCanceled = new MutableLiveData<>();
        }
        return onOrderCanceled;
    }

    public void getOrderDetails(String order_id) {
        getIsLoading().setValue(true);
        Api.getService(Tags.base_url)
                .getOrderDetails(order_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<SingleOrderDataModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<SingleOrderDataModel> response) {
                        getIsLoading().setValue(false);
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body().getStatus() == 200) {
                                    prepareData(response.body().getData());
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

    private void prepareData(OrderModel data) {
        List<OrderModel.Offers> offersList = new ArrayList<>();
        for (OrderModel.Offers offers : data.getOffers()) {
            for (OrderModel.OfferDetail offerDetail : offers.getOffer_details()) {
                if (offerDetail.getType().equals("not_found")) {
                    offers.setNotFound(true);
                }
                if (offerDetail.getType().equals("other")) {
                    offers.setOther(true);
                }
                if (offerDetail.getType().equals("price")) {
                    offers.setPrice(true);
                }
                if (offerDetail.getType().equals("less")) {
                    offers.setLess(true);
                }


            }
            offersList.add(offers);

        }

        data.setOffers(offersList);
        getOnDataSuccess().setValue(data);

    }

    public void acceptCancelOrder(String order_id, Context context) {
        ProgressDialog dialog = Common.createProgressDialog(context, context.getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Api.getService(Tags.base_url).updateOrderStatus(order_id, "cancel")
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
                                    getOnOrderCanceled().setValue(true);
                                }
                            }
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

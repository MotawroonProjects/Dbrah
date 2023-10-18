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
import com.app.dbrah.model.TimeDataModel;
import com.app.dbrah.model.TimeModel;
import com.app.dbrah.model.cart_models.CartModel;
import com.app.dbrah.model.cart_models.CartResponse;
import com.app.dbrah.model.cart_models.CartSingleModel;
import com.app.dbrah.remote.Api;
import com.app.dbrah.share.Common;
import com.app.dbrah.tags.Tags;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class FragmentCartMvvm extends AndroidViewModel {
    private MutableLiveData<List<TimeModel>> onDataSuccess;

    private CompositeDisposable disposable = new CompositeDisposable();
    private MutableLiveData<CartModel> onAllOrderSentSuccess;
    private MutableLiveData<CartSingleModel> onSingleOrderSentSuccess;
    private MutableLiveData<List<Integer>> onOrdersNotSent;
    private MutableLiveData<List<Integer>> onSingleOrdersNotSent;

    public FragmentCartMvvm(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<CartModel> getOnAllOrderSentSuccess() {
        if (onAllOrderSentSuccess == null) {
            onAllOrderSentSuccess = new MutableLiveData<>();
        }
        return onAllOrderSentSuccess;
    }

    public MutableLiveData<CartSingleModel> getOnSingleOrderSentSuccess() {
        if (onSingleOrderSentSuccess == null) {
            onSingleOrderSentSuccess = new MutableLiveData<>();
        }
        return onSingleOrderSentSuccess;
    }

    public MutableLiveData<List<Integer>> getOnOrdersNotSent() {
        if (onOrdersNotSent == null) {
            onOrdersNotSent = new MutableLiveData<>();
        }
        return onOrdersNotSent;
    }

    public MutableLiveData<List<Integer>> getOnSingleOrdersNotSent() {
        if (onSingleOrdersNotSent == null) {
            onSingleOrdersNotSent = new MutableLiveData<>();
        }
        return onSingleOrdersNotSent;
    }

    public void sendAllOrder(Context context, CartModel cartModel) {
        ProgressDialog dialog = Common.createProgressDialog(context, context.getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .sendAllOrder(cartModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<CartResponse>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<CartResponse> response) {
                        dialog.dismiss();

                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body().getStatus() == 200) {
                                    getOnAllOrderSentSuccess().setValue(cartModel);
                                } else if (response.body().getStatus() == 406) {
                                    Toast.makeText(context, R.string.user_not_found, Toast.LENGTH_SHORT).show();
                                } else if (response.body().getStatus() == 408) {
                                    Toast.makeText(context, R.string.no_prov_aval, Toast.LENGTH_LONG).show();
                                    getOnOrdersNotSent().setValue(response.body().getCategories_id());
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
                        dialog.dismiss();

                    }
                });
    }

    public void sendSingleOrder(Context context, CartSingleModel cartModel) {
        String data = new Gson().toJson(cartModel);
        Log.e("data",data);
        ProgressDialog dialog = Common.createProgressDialog(context, context.getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .sendSingleOrder(cartModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<CartResponse>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<CartResponse> response) {
                        dialog.dismiss();

                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                Log.e("code",response.body().getStatus()+"");
                                if (response.body().getStatus() == 200) {
                                    getOnSingleOrderSentSuccess().setValue(cartModel);
                                } else if (response.body().getStatus() == 406) {
                                    Toast.makeText(context, R.string.user_not_found, Toast.LENGTH_SHORT).show();
                                } else if (response.body().getStatus() == 408) {
                                    Toast.makeText(context, R.string.no_prov_aval, Toast.LENGTH_LONG).show();
                                    getOnSingleOrdersNotSent().setValue(response.body().getCategories_id());
                                }else {
                                    Log.e("error",response.body().getMessage().toString());
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
                        Log.e("error",e.getMessage());
                        dialog.dismiss();

                    }
                });
    }
    public MutableLiveData<List<TimeModel>> getOnDataSuccess() {
        if (onDataSuccess == null) {
            onDataSuccess = new MutableLiveData<>();
        }
        return onDataSuccess;
    }
    public void getTime(Context context) {

      //  getIsLoading().setValue(true);
        Api.getService(Tags.base_url).getTime()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<TimeDataModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<TimeDataModel> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getData() != null && response.body().getStatus() == 200) {
                              //  getIsLoading().setValue(false);
                                List<TimeModel> list = response.body().getData();
                                list.add(0, new TimeModel(context.getString(R.string.ch_time)));
                                onDataSuccess.setValue(list);
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("error", e.toString());
                    }
                });
    }


}

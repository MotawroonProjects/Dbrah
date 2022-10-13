package com.app.dbrah.mvvm;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.app.dbrah.model.ProductModel;
import com.app.dbrah.model.SingleProductModel;
import com.app.dbrah.model.StatusResponse;
import com.app.dbrah.model.UserModel;
import com.app.dbrah.remote.Api;
import com.app.dbrah.tags.Tags;


import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class FragmentProductDetailsMvvm extends AndroidViewModel {

    private MutableLiveData<Boolean> isDataLoading;
    private MutableLiveData<ProductModel> onDataSuccess;
    private MutableLiveData<ProductModel> onFavUnFavSuccess;

    private CompositeDisposable disposable = new CompositeDisposable();

    public MutableLiveData<Boolean> getIsDataLoading() {
        if (isDataLoading == null) {
            isDataLoading = new MutableLiveData<>();
        }
        return isDataLoading;
    }

    public MutableLiveData<ProductModel> getOnDataSuccess() {
        if (onDataSuccess == null) {
            onDataSuccess = new MutableLiveData<>();
        }
        return onDataSuccess;
    }

    public FragmentProductDetailsMvvm(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<ProductModel> getOnFavUnFavSuccess() {
        if (onFavUnFavSuccess == null) {
            onFavUnFavSuccess = new MutableLiveData<>();
        }
        return onFavUnFavSuccess;
    }

    public void getSingleProduct(String id, UserModel userModel) {
        String user_id = null;
        if (userModel != null) {
            user_id = userModel.getData().getId();
        }
        getIsDataLoading().setValue(true);
        Api.getService(Tags.base_url).getSingleProduct(user_id, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<SingleProductModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<SingleProductModel> response) {
                        getIsDataLoading().postValue(false);
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                onDataSuccess.setValue(response.body().getData());
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getIsDataLoading().setValue(false);
                        Log.e("err", e.toString());

                    }
                });
    }

    public void favUnFav(UserModel userModel, ProductModel model) {
        if (userModel == null) {
            if (model.getIs_list().equals("true")) {
                model.setIs_list("false");
            } else {
                model.setIs_list("true");

            }
            getOnFavUnFavSuccess().setValue(model);
            return;
        }

        Api.getService(Tags.base_url)
                .favUnFav(userModel.getData().getId(), model.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<StatusResponse>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<StatusResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                Log.e("ddd", response.body().getStatus() + "" + response.body().getMessage().toString());
                            } else {
                                if (model.getIs_list().equals("true")) {
                                    model.setIs_list("false");
                                } else {
                                    model.setIs_list("true");

                                }
                                getOnFavUnFavSuccess().setValue(model);
                            }
                        } else {
                            if (model.getIs_list().equals("true")) {
                                model.setIs_list("false");
                            } else {
                                model.setIs_list("true");

                            }
                            getOnFavUnFavSuccess().setValue(model);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("error", e.toString());
                        if (model.getIs_list().equals("true")) {
                            model.setIs_list("false");
                        } else {
                            model.setIs_list("true");

                        }
                        getOnFavUnFavSuccess().setValue(model);
                    }
                });
    }


}

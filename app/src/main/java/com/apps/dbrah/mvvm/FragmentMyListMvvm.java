package com.apps.dbrah.mvvm;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.apps.dbrah.model.ProductModel;
import com.apps.dbrah.model.RecentProductDataModel;
import com.apps.dbrah.model.StatusResponse;
import com.apps.dbrah.model.UserModel;
import com.apps.dbrah.model.cart_models.ManageCartModel;
import com.apps.dbrah.remote.Api;
import com.apps.dbrah.tags.Tags;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class FragmentMyListMvvm extends AndroidViewModel {
    private ManageCartModel manageCartModel;
    private MutableLiveData<List<ProductModel>> onDataSuccess;
    private MutableLiveData<ProductModel> onFavUnFavSuccess;

    private CompositeDisposable disposable = new CompositeDisposable();
    private MutableLiveData<Boolean> isLoading;


    public FragmentMyListMvvm(@NonNull Application application) {
        super(application);
        manageCartModel = ManageCartModel.newInstance();
    }


    public MutableLiveData<Boolean> getIsLoading() {
        if (isLoading == null) {
            isLoading = new MutableLiveData<>();
        }
        return isLoading;
    }


    public MutableLiveData<List<ProductModel>> getOnDataSuccess() {
        if (onDataSuccess == null) {
            onDataSuccess = new MutableLiveData<>();
        }
        return onDataSuccess;
    }

    public MutableLiveData<ProductModel> getOnFavUnFavSuccess() {
        if (onFavUnFavSuccess == null) {
            onFavUnFavSuccess = new MutableLiveData<>();
        }
        return onFavUnFavSuccess;
    }


    public void getMyList(Context context, UserModel userModel) {
        String user_id = null;
        if (userModel != null) {
            user_id = userModel.getData().getId();
        }
        getIsLoading().setValue(true);
        Api.getService(Tags.base_url)
                .getMyList(user_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<RecentProductDataModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<RecentProductDataModel> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getData() != null && response.body().getStatus() == 200) {
                                prepareData(response.body().getData(), context);
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("error", e.toString());
                    }
                });
    }

    private void prepareData(List<ProductModel> data, Context context) {
        for (int index = 0; index < data.size(); index++) {
            ProductModel productModel = data.get(index);
            productModel.setAmount(manageCartModel.getProductAmount(productModel.getId(), context));
            data.set(index, productModel);
        }
        getIsLoading().setValue(false);

        getOnDataSuccess().setValue(data);
    }


    public void favUnFav(UserModel userModel, ProductModel model, int pos) {

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

                                if (getOnDataSuccess().getValue() != null) {
                                    getOnDataSuccess().getValue().remove(pos);
                                    getOnFavUnFavSuccess().setValue(model);

                                }

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

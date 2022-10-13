package com.app.dbrah.mvvm;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.app.dbrah.model.AddressModel;
import com.app.dbrah.model.AddressesDataModel;
import com.app.dbrah.model.CategoryDataModel;
import com.app.dbrah.model.CategoryModel;
import com.app.dbrah.model.ProductModel;
import com.app.dbrah.model.RecentProductDataModel;
import com.app.dbrah.model.cart_models.ManageCartModel;
import com.app.dbrah.remote.Api;
import com.app.dbrah.tags.Tags;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class FragmentMyAddressesMvvm extends AndroidViewModel {

    private MutableLiveData<Boolean> isLoading;

    private MutableLiveData<List<AddressModel>> onDataSuccess;


    private CompositeDisposable disposable = new CompositeDisposable();


    public FragmentMyAddressesMvvm(@NonNull Application application) {
        super(application);

    }


    public MutableLiveData<Boolean> getIsLoading() {
        if (isLoading == null) {
            isLoading = new MutableLiveData<>();
        }
        return isLoading;
    }

    public MutableLiveData<List<AddressModel>> getOnDataSuccess() {
        if (onDataSuccess == null) {
            onDataSuccess = new MutableLiveData<>();
        }
        return onDataSuccess;
    }


    public void getAddresses(String user_id)
    {
        if (user_id==null){
            getIsLoading().setValue(false);
            getOnDataSuccess().setValue(new ArrayList<>());

            return;
        }
        getIsLoading().setValue(true);

        Api.getService(Tags.base_url).getMyAddresses(user_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<AddressesDataModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<AddressesDataModel> response) {
                        getIsLoading().setValue(false);
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getData() != null && response.body().getStatus() == 200) {
                                getOnDataSuccess().setValue(response.body().getData());
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

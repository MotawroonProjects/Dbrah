package com.apps.dbrah.mvvm;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.apps.dbrah.model.ProductModel;
import com.apps.dbrah.model.RecentProductDataModel;
import com.apps.dbrah.model.SearchHomeDataModel;
import com.apps.dbrah.remote.Api;
import com.apps.dbrah.tags.Tags;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class FragmentSearchMvvm extends AndroidViewModel {

    private CompositeDisposable disposable = new CompositeDisposable();
    private MutableLiveData<Boolean> isLoading;

    private MutableLiveData<SearchHomeDataModel.Data> onDataSuccess;

    public FragmentSearchMvvm(@NonNull Application application) {
        super(application);
    }



    public MutableLiveData<Boolean> getIsLoading() {
        if (isLoading == null) {
            isLoading = new MutableLiveData<>();
        }
        return isLoading;
    }






    public MutableLiveData<SearchHomeDataModel.Data> getOnDataSuccess() {
        if (onDataSuccess ==null){
            onDataSuccess =new MutableLiveData<>();
        }
        return onDataSuccess;
    }



    public void search(String query){

        getIsLoading().setValue(true);
        Api.getService(Tags.base_url).searchProduct(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<SearchHomeDataModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<SearchHomeDataModel> response) {
                        if (response.isSuccessful() && response.body()!=null){
                            if (response.body().getData()!=null && response.body().getStatus()==200){
                                getIsLoading().setValue(false);
                                onDataSuccess.setValue(response.body().getData());
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("error",e.toString());
                    }
                });
    }

}

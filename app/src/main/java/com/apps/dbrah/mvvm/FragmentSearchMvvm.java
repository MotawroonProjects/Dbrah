package com.apps.dbrah.mvvm;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.apps.dbrah.model.CategoryDataModel;
import com.apps.dbrah.model.ProductModel;
import com.apps.dbrah.model.RecentProductDataModel;
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

    private Context context;


    private CompositeDisposable disposable = new CompositeDisposable();
    private MutableLiveData<Boolean> isLoadingLiveData;
    private MutableLiveData<List<ProductModel>> listMutableLiveData;

    public FragmentSearchMvvm(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }



    public MutableLiveData<Boolean> getIsLoadingLiveData() {
        if (isLoadingLiveData == null) {
            isLoadingLiveData = new MutableLiveData<>();
        }
        return isLoadingLiveData;
    }



    public MutableLiveData<List<ProductModel>> getListMutableLiveData() {
        if (listMutableLiveData==null){
            listMutableLiveData=new MutableLiveData<>();
        }
        return listMutableLiveData;
    }



    public void searchProduct(String query){
        isLoadingLiveData.setValue(true);
        Api.getService(Tags.base_url).searchProduct(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<RecentProductDataModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<RecentProductDataModel> response) {
                        isLoadingLiveData.postValue(false);
                        if (response.isSuccessful() && response.body()!=null){
                            if (response.body().getData()!=null && response.body().getStatus()==200){
                                listMutableLiveData.postValue(response.body().getData());
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        isLoadingLiveData.setValue(false);
                        Log.e("error",e.toString());
                    }
                });
    }

}

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
import com.apps.dbrah.model.SliderDataModel;
import com.apps.dbrah.remote.Api;
import com.apps.dbrah.tags.Tags;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class FragmentMarketMvvm extends AndroidViewModel {

    private Context context;
    private MutableLiveData<SliderDataModel> sliderDataModelMutableLiveData;
    private MutableLiveData<List<CategoryDataModel.CategoryModel>> categoryModelLiveData;
    private MutableLiveData<List<ProductModel>> recentModelLiveData;
    private CompositeDisposable disposable = new CompositeDisposable();
    private MutableLiveData<Boolean> isLoadingLiveData;
    public FragmentMarketMvvm(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public MutableLiveData<SliderDataModel> getSliderDataModelMutableLiveData() {
        if (sliderDataModelMutableLiveData == null) {
            sliderDataModelMutableLiveData = new MutableLiveData<>();
        }
        return sliderDataModelMutableLiveData;
    }

    public MutableLiveData<Boolean> getIsLoadingLiveData() {
        if (isLoadingLiveData == null) {
            isLoadingLiveData = new MutableLiveData<>();
        }
        return isLoadingLiveData;
    }

    public MutableLiveData<List<CategoryDataModel.CategoryModel>> getCategoryModelLiveData() {
        if (categoryModelLiveData==null){
            categoryModelLiveData=new MutableLiveData<>();
        }
        return categoryModelLiveData;
    }

    public MutableLiveData<List<ProductModel>> getRecentModelLiveData() {
        if (recentModelLiveData==null){
            recentModelLiveData=new MutableLiveData<>();
        }
        return recentModelLiveData;
    }

    public void getSlider(){
        isLoadingLiveData.setValue(true);
        Api.getService(Tags.base_url).getSlider()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<SliderDataModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<SliderDataModel> response) {
                        isLoadingLiveData.postValue(false);
                        if (response.isSuccessful()&& response.body()!=null){
                            if (response.body().getStatus()==200){
                                sliderDataModelMutableLiveData.postValue(response.body());
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

    public void getCategory(){
        isLoadingLiveData.setValue(true);
        Api.getService(Tags.base_url).getCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<CategoryDataModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<CategoryDataModel> response) {
                        isLoadingLiveData.postValue(false);

                        if (response.isSuccessful() && response.body()!=null){
                            if (response.body().getData()!=null && response.body().getStatus()==200){
                                categoryModelLiveData.postValue(response.body().getData());
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
    public void getRecentProduct(){
        isLoadingLiveData.setValue(true);
        Api.getService(Tags.base_url).getRecentProduct()
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
                                recentModelLiveData.postValue(response.body().getData());
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

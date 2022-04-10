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

public class FragmentProductsMvvm extends AndroidViewModel {

    private Context context;
    private MutableLiveData<List<CategoryDataModel.CategoryModel>> categoryModelLiveData;
    private MutableLiveData<List<CategoryDataModel.CategoryModel>> subcategoryModelLiveData;

    private CompositeDisposable disposable = new CompositeDisposable();
    private MutableLiveData<Boolean> isLoadingLiveData;
    private MutableLiveData<List<ProductModel>> listMutableLiveData;

    public FragmentProductsMvvm(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
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

    public MutableLiveData<List<ProductModel>> getListMutableLiveData() {
        if (listMutableLiveData==null){
            listMutableLiveData=new MutableLiveData<>();
        }
        return listMutableLiveData;
    }

    public MutableLiveData<List<CategoryDataModel.CategoryModel>> getSubCategoryModelLiveData() {
        if (subcategoryModelLiveData==null){
            subcategoryModelLiveData=new MutableLiveData<>();
        }
        return subcategoryModelLiveData;
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
    public void getSubCategory(String cat_id){
        isLoadingLiveData.setValue(true);
        Api.getService(Tags.base_url).getSubCategory(cat_id)
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
                                subcategoryModelLiveData.postValue(response.body().getData());
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
    public void searchProduct(String cat_id,String sub_id,String query){
        isLoadingLiveData.setValue(true);
        Api.getService(Tags.base_url).searchByCatProduct(cat_id,sub_id,query)
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

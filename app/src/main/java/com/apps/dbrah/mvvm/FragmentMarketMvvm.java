package com.apps.dbrah.mvvm;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.apps.dbrah.model.CategoryDataModel;
import com.apps.dbrah.model.CategoryModel;
import com.apps.dbrah.model.ProductModel;
import com.apps.dbrah.model.RecentProductDataModel;
import com.apps.dbrah.model.SliderDataModel;
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

public class FragmentMarketMvvm extends AndroidViewModel {
    private ManageCartModel manageCartModel;
    private MutableLiveData<List<SliderDataModel.SliderModel>> onSliderDataSuccess;
    private MutableLiveData<List<CategoryModel>> onCategoryDataSuccess;
    private MutableLiveData<List<ProductModel>> onRecentProductDataModel;
    private MutableLiveData<List<ProductModel>> onMostProductDataModel;
    private MutableLiveData<ProductModel> onFavUnFavSuccess;

    private CompositeDisposable disposable = new CompositeDisposable();
    private MutableLiveData<Boolean> isLoadingSlider;
    private MutableLiveData<Boolean> isLoadingCategory;
    private MutableLiveData<Boolean> isLoadingMostSaleProduct;
    private MutableLiveData<Boolean> isLoadingRecentProduct;

    public FragmentMarketMvvm(@NonNull Application application) {
        super(application);
        manageCartModel = ManageCartModel.newInstance();
    }

    public MutableLiveData<List<SliderDataModel.SliderModel>> getOnSliderDataSuccess() {
        if (onSliderDataSuccess == null) {
            onSliderDataSuccess = new MutableLiveData<>();
        }
        return onSliderDataSuccess;
    }

    public MutableLiveData<Boolean> getIsLoadingSlider() {
        if (isLoadingSlider == null) {
            isLoadingSlider = new MutableLiveData<>();
        }
        return isLoadingSlider;
    }

    public MutableLiveData<Boolean> getIsLoadingCategory() {
        if (isLoadingCategory == null) {
            isLoadingCategory = new MutableLiveData<>();
        }
        return isLoadingCategory;
    }

    public MutableLiveData<Boolean> getIsLoadingMostSaleProduct() {
        if (isLoadingMostSaleProduct == null) {
            isLoadingMostSaleProduct = new MutableLiveData<>();
        }
        return isLoadingMostSaleProduct;
    }

    public MutableLiveData<Boolean> getIsLoadingRecentProduct() {
        if (isLoadingRecentProduct == null) {
            isLoadingRecentProduct = new MutableLiveData<>();
        }
        return isLoadingRecentProduct;
    }

    public MutableLiveData<List<CategoryModel>> getOnCategoryDataSuccess() {
        if (onCategoryDataSuccess == null) {
            onCategoryDataSuccess = new MutableLiveData<>();
        }
        return onCategoryDataSuccess;
    }

    public MutableLiveData<List<ProductModel>> getOnRecentProductDataModel() {
        if (onRecentProductDataModel == null) {
            onRecentProductDataModel = new MutableLiveData<>();
        }
        return onRecentProductDataModel;
    }

    public MutableLiveData<List<ProductModel>> getOnMostProductDataModel() {
        if (onMostProductDataModel == null) {
            onMostProductDataModel = new MutableLiveData<>();
        }
        return onMostProductDataModel;
    }

    public MutableLiveData<ProductModel> getOnFavUnFavSuccess() {
        if (onFavUnFavSuccess == null) {
            onFavUnFavSuccess = new MutableLiveData<>();
        }
        return onFavUnFavSuccess;
    }

    public void getSlider() {
        getIsLoadingSlider().setValue(true);
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
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                getIsLoadingSlider().setValue(false);

                                getOnSliderDataSuccess().setValue(response.body().getData());
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("error", e.toString());
                    }
                });

    }

    public void getCategory() {
        getIsLoadingCategory().setValue(true);
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

                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getData() != null && response.body().getStatus() == 200) {
                                getIsLoadingCategory().setValue(false);

                                getOnCategoryDataSuccess().setValue(response.body().getData());
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("error", e.toString());
                    }
                });
    }

    public void getRecentProduct(Context context,UserModel userModel) {
        String user_id = null;
        if (userModel!=null&&userModel.getData()!=null&&userModel.getData().getId()!=null){
            user_id = userModel.getData().getId();
        }
        getIsLoadingRecentProduct().setValue(true);
        Api.getService(Tags.base_url).getRecentProduct(user_id)
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
                                prepareRecentData(response.body().getData(), context);
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("error", e.toString());
                    }
                });
    }


    public void getMostSaleProduct(Context context,UserModel userModel) {
        String user_id = null;
        if (userModel!=null&&userModel.getData()!=null&&userModel.getData().getId()!=null){
            Log.e("id",userModel.getData().getId()+"");
            user_id = userModel.getData().getId();
        }
        getIsLoadingMostSaleProduct().setValue(true);
        Api.getService(Tags.base_url).getMostSaleProduct(user_id)
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
                                prepareMostSaleData(response.body().getData(), context);
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("error", e.toString());
                    }
                });
    }

    private void prepareRecentData(List<ProductModel> data, Context context) {
        for (int index = 0; index < data.size(); index++) {
            ProductModel productModel = data.get(index);
            productModel.setAmount(manageCartModel.getProductAmount(productModel.getId(), context));
            data.set(index, productModel);
        }
        getIsLoadingRecentProduct().setValue(false);

        getOnRecentProductDataModel().setValue(data);
    }


    private void prepareMostSaleData(List<ProductModel> data, Context context) {
        for (int index = 0; index < data.size(); index++) {
            ProductModel productModel = data.get(index);
            productModel.setAmount(manageCartModel.getProductAmount(productModel.getId(), context));
            data.set(index, productModel);
        }
        getIsLoadingMostSaleProduct().setValue(false);

        getOnMostProductDataModel().setValue(data);
    }

    public void favUnFav(UserModel userModel, ProductModel model) {
        if (userModel == null) {
            if (model.getIs_list().equals("true")){
                model.setIs_list("false");
            }else {
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
                                getOnFavUnFavSuccess().setValue(model);
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

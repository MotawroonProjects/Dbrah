package com.app.dbrah.mvvm;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.app.dbrah.model.CategoryDataModel;
import com.app.dbrah.model.CategoryModel;
import com.app.dbrah.model.ProductModel;
import com.app.dbrah.model.RecentProductDataModel;
import com.app.dbrah.model.StatusResponse;
import com.app.dbrah.model.UserModel;
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

public class FragmentProductsMvvm extends AndroidViewModel {
    private ManageCartModel manageCartModel;

    private MutableLiveData<Boolean> isLoading;

    private MutableLiveData<List<CategoryModel>> onCategoryDataSuccess;

    private MutableLiveData<List<CategoryModel>> onSubCategoryDataSuccess;

    private MutableLiveData<List<ProductModel>> onProductsDataSuccess;

    private MutableLiveData<String> categoryId;

    private MutableLiveData<String> subCategoryId;

    private MutableLiveData<String> query;

    private MutableLiveData<Integer> categoryPos;

    private MutableLiveData<ProductModel> onFavUnFavSuccess;



    private CompositeDisposable disposable = new CompositeDisposable();


    public FragmentProductsMvvm(@NonNull Application application) {
        super(application);
        manageCartModel = ManageCartModel.newInstance();

    }


    public MutableLiveData<Boolean> getIsLoading() {
        if (isLoading == null) {
            isLoading = new MutableLiveData<>();
        }
        return isLoading;
    }

    public MutableLiveData<List<CategoryModel>> getOnCategoryDataSuccess() {
        if (onCategoryDataSuccess == null) {
            onCategoryDataSuccess = new MutableLiveData<>();
        }
        return onCategoryDataSuccess;
    }


    public MutableLiveData<List<CategoryModel>> getOnSubCategoryDataSuccess() {
        if (onSubCategoryDataSuccess == null) {
            onSubCategoryDataSuccess = new MutableLiveData<>();
        }
        return onSubCategoryDataSuccess;
    }


    public MutableLiveData<List<ProductModel>> getOnProductsDataSuccess() {
        if (onProductsDataSuccess == null) {
            onProductsDataSuccess = new MutableLiveData<>();
        }
        return onProductsDataSuccess;
    }

    public MutableLiveData<ProductModel> getOnFavUnFavSuccess() {
        if (onFavUnFavSuccess == null) {
            onFavUnFavSuccess = new MutableLiveData<>();
        }
        return onFavUnFavSuccess;
    }

    public MutableLiveData<Integer> getCategoryPos() {
        if (categoryPos == null) {
            categoryPos = new MutableLiveData<>();
        }
        return categoryPos;
    }

    public MutableLiveData<String> getCategoryId() {
        if (categoryId == null) {
            categoryId = new MutableLiveData<>();
        }
        return categoryId;
    }

    public MutableLiveData<String> getQuery() {
        if (query == null) {
            query = new MutableLiveData<>();
        }
        return query;
    }

    public MutableLiveData<String> getSubCategoryId() {
        if (subCategoryId == null) {
            subCategoryId = new MutableLiveData<>();
        }
        return subCategoryId;
    }

    public void setCategoryId(String categoryId,Context context,UserModel userModel) {
        getCategoryId().setValue(categoryId);
        getSubCategory(categoryId,context,userModel);
    }


    public void getSubCategory(String cat_id,Context context,UserModel userModel) {

        getOnSubCategoryDataSuccess().setValue(new ArrayList<>());
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
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getData() != null && response.body().getStatus() == 200) {
                                List<CategoryModel> list = response.body().getData();

                                if (list.size() > 0) {
                                    CategoryModel model = new CategoryModel(null, "الكل", "All", null, true);
                                    list.add(0, model);
                                }
                                getCategoryId().setValue(cat_id);
                                searchProduct(getQuery().getValue(),context,userModel);
                                getOnSubCategoryDataSuccess().setValue(list);
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        Log.e("error", e.toString());
                    }
                });
    }

    public void searchProduct(String query, Context context, UserModel userModel) {
        String user_id = null;
        if (userModel!=null){
            user_id = userModel.getData().getId();
        }
        getIsLoading().postValue(true);

        Api.getService(Tags.base_url).searchByCatProduct(user_id,getCategoryId().getValue(), getSubCategoryId().getValue(), query)
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

                                prepareProducts(response.body().getData(),context);
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getIsLoading().setValue(false);
                        Log.e("error", e.toString());
                    }
                });
    }

    private void prepareProducts(List<ProductModel> data, Context context) {
        for (int index = 0; index < data.size(); index++) {
            ProductModel productModel = data.get(index);
            productModel.setAmount(manageCartModel.getProductAmount(productModel.getId(),context));
            data.set(index, productModel);
        }
        getIsLoading().setValue(false);
        getOnProductsDataSuccess().setValue(data);
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
                                Log.e("ddd",response.body().getStatus()+""+response.body().getMessage().toString());
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

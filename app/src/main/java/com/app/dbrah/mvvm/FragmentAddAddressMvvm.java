package com.app.dbrah.mvvm;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.app.dbrah.R;
import com.app.dbrah.model.AddAddressModel;
import com.app.dbrah.model.AddressModel;
import com.app.dbrah.model.SearchHomeDataModel;
import com.app.dbrah.model.SingleAddressData;
import com.app.dbrah.model.TimeDataModel;
import com.app.dbrah.model.TimeModel;
import com.app.dbrah.model.UserModel;
import com.app.dbrah.remote.Api;
import com.app.dbrah.share.Common;
import com.app.dbrah.tags.Tags;

import java.io.IOException;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class FragmentAddAddressMvvm extends AndroidViewModel {

    private CompositeDisposable disposable = new CompositeDisposable();
    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<List<TimeModel>> onDataSuccess;
    private MutableLiveData<AddressModel> onAddressAdded;
    private MutableLiveData<AddressModel> onAddressUpdated;

    public FragmentAddAddressMvvm(@NonNull Application application) {
        super(application);
    }


    public MutableLiveData<Boolean> getIsLoading() {
        if (isLoading == null) {
            isLoading = new MutableLiveData<>();
        }
        return isLoading;
    }


    public MutableLiveData<List<TimeModel>> getOnDataSuccess() {
        if (onDataSuccess == null) {
            onDataSuccess = new MutableLiveData<>();
        }
        return onDataSuccess;
    }

    public MutableLiveData<AddressModel> getOnAddressAdded() {
        if (onAddressAdded == null) {
            onAddressAdded = new MutableLiveData<>();
        }
        return onAddressAdded;
    }

    public MutableLiveData<AddressModel> getOnAddressUpdated() {
        if (onAddressUpdated == null) {
            onAddressUpdated = new MutableLiveData<>();
        }
        return onAddressUpdated;
    }


    public void getTime(Context context) {

        getIsLoading().setValue(true);
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
                                getIsLoading().setValue(false);
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

    public void addAddress(UserModel userModel, AddAddressModel model,Context context){
        ProgressDialog dialog = Common.createProgressDialog(context, context.getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url).addAddress(userModel.getData().getId(),model.getTime_id(), model.getTitle(),model.getAdmin_name(), model.getPhone_code(), model.getPhone(),model.getAddress(), model.getLat(), model.getLng())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<SingleAddressData>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<SingleAddressData> response) {
                        dialog.dismiss();

                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body().getStatus() == 200) {
                                    getOnAddressAdded().setValue(response.body().getData());
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

    public void updateAddress(UserModel userModel, AddAddressModel model,Context context){
        ProgressDialog dialog = Common.createProgressDialog(context, context.getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url).editAddress(model.getId(),userModel.getData().getId(),model.getTime_id(), model.getTitle(),model.getAdmin_name(), model.getPhone_code(), model.getPhone(),model.getAddress(), model.getLat(), model.getLng())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<SingleAddressData>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<SingleAddressData> response) {
                        dialog.dismiss();

                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body().getStatus() == 200) {
                                    getOnAddressUpdated().setValue(response.body().getData());
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

}

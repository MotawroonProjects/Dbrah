package com.app.dbrah.mvvm;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.app.dbrah.R;
import com.app.dbrah.model.ProductModel;
import com.app.dbrah.model.RecentProductDataModel;
import com.app.dbrah.model.SettingDataModel;
import com.app.dbrah.model.StatusResponse;
import com.app.dbrah.model.UserModel;
import com.app.dbrah.model.cart_models.ManageCartModel;
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

public class FragmentSettingMvvm extends AndroidViewModel {
    private MutableLiveData<SettingDataModel.Data> onDataSuccess;
    private MutableLiveData<Boolean> onLoggedOutSuccess;

    private CompositeDisposable disposable = new CompositeDisposable();


    public FragmentSettingMvvm(@NonNull Application application) {
        super(application);
    }


    public MutableLiveData<SettingDataModel.Data> getOnDataSuccess() {
        if (onDataSuccess == null) {
            onDataSuccess = new MutableLiveData<>();
        }
        return onDataSuccess;
    }

    public MutableLiveData<Boolean> getOnLoggedOutSuccess() {
        if (onLoggedOutSuccess == null) {
            onLoggedOutSuccess = new MutableLiveData<>();
        }

        return onLoggedOutSuccess;
    }

    public void getSettings(Context context) {
        Api.getService(Tags.base_url)
                .getSettings()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<SettingDataModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<SettingDataModel> response) {
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

    public void logout(UserModel userModel, Context context) {
        if (userModel == null) {
            return;
        }

        if (userModel.getFirebase_token() == null || userModel.getFirebase_token().isEmpty()) {
            return;
        }
        ProgressDialog dialog = Common.createProgressDialog(context, context.getString(R.string.wait));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        Api.getService(Tags.base_url)
                .logout(userModel.getData().getId(), userModel.getFirebase_token())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<StatusResponse>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            Log.e("status", response.body().getStatus() + "");
                            if (response.body() != null) {
                                if (response.body().getStatus() == 200) {
                                    getOnLoggedOutSuccess().setValue(true);

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

                        Log.e("token", e.toString());

                    }
                });

    }


}

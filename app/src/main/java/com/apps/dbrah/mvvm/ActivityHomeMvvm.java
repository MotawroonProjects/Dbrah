package com.apps.dbrah.mvvm;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;


import com.apps.dbrah.R;
import com.apps.dbrah.model.StatusResponse;
import com.apps.dbrah.model.UserModel;
import com.apps.dbrah.remote.Api;
import com.apps.dbrah.share.Common;
import com.apps.dbrah.tags.Tags;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ActivityHomeMvvm extends AndroidViewModel {

    private MutableLiveData<UserModel> onTokenSuccess;

    private CompositeDisposable disposable = new CompositeDisposable();

    public ActivityHomeMvvm(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<UserModel> onTokenSuccess() {
        if (onTokenSuccess == null) {
            onTokenSuccess = new MutableLiveData<>();
        }

        return onTokenSuccess;
    }


    public void updateToken(UserModel userModel) {
        if (userModel == null) {
            return;
        }
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String token = task.getResult();
                Api.getService(Tags.base_url)
                        .updateFireBaseToken(userModel.getData().getId(), token, "android")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<Response<StatusResponse>>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {
                                disposable.add(d);
                            }

                            @Override
                            public void onSuccess(@NonNull Response<StatusResponse> response) {

                                if (response.isSuccessful()) {
                                    if (response.body() != null) {

                                        if (response.body().getStatus() == 200) {
                                            userModel.setFirebase_token(token);
                                            onTokenSuccess().setValue(userModel);
                                            Log.e("token", "updated");

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
                                Log.e("token", e.toString());

                            }
                        });
            }
        });


    }
    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

}

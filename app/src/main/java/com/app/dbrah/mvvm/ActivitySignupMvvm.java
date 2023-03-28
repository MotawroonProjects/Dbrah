package com.app.dbrah.mvvm;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;


import com.app.dbrah.R;
import com.app.dbrah.model.SettingDataModel;
import com.app.dbrah.model.SignUpModel;
import com.app.dbrah.model.StatusResponse;
import com.app.dbrah.model.UserModel;
import com.app.dbrah.remote.Api;
import com.app.dbrah.share.Common;
import com.app.dbrah.tags.Tags;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class ActivitySignupMvvm extends AndroidViewModel {
    public MutableLiveData<UserModel> onUserDataSuccess;
    private MutableLiveData<SettingDataModel.Data> onDataSuccess;
    private MutableLiveData<Boolean> onCodeSend;

    private CompositeDisposable disposable = new CompositeDisposable();

    public ActivitySignupMvvm(@NonNull Application application) {
        super(application);


    }
    public MutableLiveData<SettingDataModel.Data> getOnDataSuccess() {
        if (onDataSuccess == null) {
            onDataSuccess = new MutableLiveData<>();
        }
        return onDataSuccess;
    }
    public MutableLiveData<UserModel> getUserData() {
        if (onUserDataSuccess == null) {
            onUserDataSuccess = new MutableLiveData<>();
        }
        return onUserDataSuccess;
    }

    public MutableLiveData<Boolean> getOnCodeSend() {
        if (onCodeSend == null) {
            onCodeSend = new MutableLiveData<>();
        }
        return onCodeSend;
    }

    public void signUp(SignUpModel model, Context context) {

        ProgressDialog dialog = Common.createProgressDialog(context, context.getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        RequestBody name_part = Common.getRequestBodyText(model.getName());
        RequestBody email_part = Common.getRequestBodyText(model.getEmail());

        RequestBody phone_part = Common.getRequestBodyText(model.getPhone());
        RequestBody phone_code_part = Common.getRequestBodyText(model.getPhone_code());
        RequestBody vat_part = Common.getRequestBodyText(model.getVat());


        MultipartBody.Part image = null;
        if (model.getImage_uri() != null && !model.getImage_uri().isEmpty()) {
            image = Common.getMultiPart(context, Uri.parse(model.getImage_uri()), "image");
        }


        Api.getService(Tags.base_url).signUpwithImage(name_part, phone_code_part, phone_part, email_part, vat_part, image)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<UserModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {

                            if (response.body() != null) {
                                if (response.body().getStatus() == 200) {

                                    getUserData().setValue(response.body());
                                } else if (response.body().getStatus() == 406) {
                                    Toast.makeText(context, R.string.ph_found, Toast.LENGTH_LONG).show();
                                } else if (response.body().getStatus() == 407) {
                                    Toast.makeText(context, R.string.em_found, Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(context, R.string.vat_found, Toast.LENGTH_LONG).show();
                                }
                            }

                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        dialog.dismiss();
                    }
                });
    }
    public void sendCode(SignUpModel model, Context context) {

        ProgressDialog dialog = Common.createProgressDialog(context, context.getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();




        Api.getService(Tags.base_url).sendcode(model.getEmail())
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


                                getOnCodeSend().postValue(true);


                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        dialog.dismiss();
                    }


                });
    }
    public void checkCode(SignUpModel model,String code, Context context) {

        ProgressDialog dialog = Common.createProgressDialog(context, context.getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();




        Api.getService(Tags.base_url).checkCode(model.getEmail(),code)
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

                            if (response.body() != null) {
                                if(response.body().getStatus()==200){
                                    signUp(model,context);
                                }
                                else{
                                    Toast.makeText(context,context.getResources().getString(R.string.incorrect_code),Toast.LENGTH_LONG).show();
                                }
                            }

                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        dialog.dismiss();
                    }


                });
    }

    public void update(SignUpModel model, String user_id, Context context) {
        ProgressDialog dialog = Common.createProgressDialog(context, context.getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        RequestBody id_part = Common.getRequestBodyText(user_id);
        RequestBody name_part = Common.getRequestBodyText(model.getName());
        RequestBody email_part = Common.getRequestBodyText(model.getEmail());

        RequestBody phone_part = Common.getRequestBodyText(model.getPhone());
        RequestBody phone_code_part = Common.getRequestBodyText(model.getPhone_code());
        RequestBody vat_part = Common.getRequestBodyText(model.getVat());


        MultipartBody.Part image = null;
        if (model.getImage_uri() != null && !model.getImage_uri().isEmpty()) {
            image = Common.getMultiPartFromPath(model.getImage_path(), "image");
        }


        Api.getService(Tags.base_url).updateProfile(id_part, email_part, image)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<UserModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Response<UserModel> response) {
                        dialog.dismiss();

                        if (response.isSuccessful()) {


                            if (response.body() != null) {
                                Log.e("image",response.body().getStatus()+"__");

                                if (response.body().getStatus() == 200) {

                                    getUserData().setValue(response.body());
                                } else if (response.body().getStatus() == 406) {
                                    Toast.makeText(context, R.string.ph_found, Toast.LENGTH_LONG).show();
                                } else if (response.body().getStatus() == 407) {
                                    Toast.makeText(context, R.string.em_found, Toast.LENGTH_LONG).show();
                                }else {
                                    Log.e("code",response.body().getStatus()+"");
                                }
                            }

                        }else {
                            try {
                                Log.e("Error",response.code()+"___"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        dialog.dismiss();
                    }
                });
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

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}

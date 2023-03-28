package com.app.dbrah.mvvm;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.app.dbrah.R;
import com.app.dbrah.model.LoginModel;
import com.app.dbrah.model.UserModel;
import com.app.dbrah.remote.Api;
import com.app.dbrah.share.Common;
import com.app.dbrah.tags.Tags;
import com.app.dbrah.uis.activity_login.LoginActivity;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ActivityLoginMvvm extends AndroidViewModel {
    private static final String TAG = "ActivityVerificationMvvm";
    private Context context;
    private FirebaseAuth mAuth;
    private String verificationId;
    private String smsCode;
    private boolean canSend = false;
    private String time;
    private String phone, phone_code;
    public MutableLiveData<String> smscode = new MutableLiveData<>();
    public MutableLiveData<Boolean> canresnd = new MutableLiveData<>();
    public MutableLiveData<String> timereturn = new MutableLiveData<>();
    private MutableLiveData<UserModel> onUserDataSuccess;

    private CompositeDisposable disposable = new CompositeDisposable();

    public ActivityLoginMvvm(@NonNull Application application) {
        super(application);


        context = application.getApplicationContext();
        mAuth = FirebaseAuth.getInstance();
      //  mAuth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);


    }

    public void sendSmsCode(String lang, String phone_code, String phone, LoginActivity activity) {

        startTimer();
        this.phone_code = phone_code;
        this.phone = phone;
        Log.e("Dldldlld",phone);
        mAuth.setLanguageCode(lang);
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                smsCode = phoneAuthCredential.getSmsCode();
                smscode.postValue(smsCode);
                checkValidCode(smsCode, activity);
            }

            @Override
            public void onCodeSent(@NonNull String verification_id, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verification_id, forceResendingToken);
                verificationId = verification_id;
            }


            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.e("dkdkdk", e.toString());
                if (e.getMessage() != null) {
                } else {

                }
            }
        };
        PhoneAuthProvider.getInstance()
                .verifyPhoneNumber(
                        phone_code + phone,
                        120,
                        TimeUnit.SECONDS,
                        activity,
                        mCallBack

                );


    }

    private void startTimer() {
        canSend = false;
        canresnd.postValue(canSend);
        Observable.intervalRange(1, 120, 1, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        long diff = 120 - aLong;
                        int min = ((int) diff / 60);
                        int sec = ((int) diff % 60);
                        time = String.format(Locale.ENGLISH, "%02d:%02d", min, sec);
                        timereturn.postValue(time);


                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        canSend = true;
                        time = "00:00";
                        timereturn.postValue("00:00");

                        canresnd.postValue(true);
                    }
                });

    }


    public void checkValidCode(String code, LoginActivity activity) {
        //login(activity);
       if (verificationId != null) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            mAuth.signInWithCredential(credential)
                    .addOnSuccessListener(authResult -> {
                       // login(activity);
                        if(getUserData().getValue()!=null){
                            getUserData().setValue(getUserData().getValue());
                        }
                        else{
                        getUserData().setValue(new UserModel());}
                    }).addOnFailureListener(e -> {
                if (e.getMessage() != null) {
                } else {

                }
            });
        } else {
           Toast.makeText(context, "wait sms", Toast.LENGTH_SHORT).show();
        }

    }





    public MutableLiveData<UserModel> getUserData() {
        if (onUserDataSuccess == null) {
            onUserDataSuccess = new MutableLiveData<>();
        }
        return onUserDataSuccess;
    }








    public void login(Context context, String phone_code, String phone) {
        ProgressDialog dialog = Common.createProgressDialog(context, context.getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url).login(phone_code, phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<UserModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<UserModel> response) {
                        dialog.dismiss();

                        if (response.isSuccessful()) {
                            Log.e("status", response.body().getStatus() + "");
                            if (response.body() != null) {

                                if (response.body().getStatus() == 200) {

                                    getUserData().setValue(response.body());
                                } else if (response.body().getStatus() == 400) {
                                    getUserData().setValue(null);

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


    public void stopTimer() {
        disposable.clear();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}

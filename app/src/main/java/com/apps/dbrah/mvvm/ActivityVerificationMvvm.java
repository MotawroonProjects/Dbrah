package com.apps.dbrah.mvvm;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.apps.dbrah.model.UserModel;
import com.apps.dbrah.remote.Api;
import com.apps.dbrah.share.Common;
import com.apps.dbrah.tags.Tags;
import com.apps.dbrah.uis.activity_verification_code.VerificationCodeActivity;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
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

public class ActivityVerificationMvvm extends AndroidViewModel {
    private static final String TAG = "ActivityVerificationMvvm";
    private FirebaseAuth mAuth;
    private String verificationId;
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private MutableLiveData<String> onSmsCodeSuccess;
    private MutableLiveData<String> onTimeStarted;
    private MutableLiveData<Boolean> onCanResend;
    private MutableLiveData<UserModel> onUserDataSuccess;
    private MutableLiveData<Boolean> isVerified;

    private CompositeDisposable disposable = new CompositeDisposable();

    public ActivityVerificationMvvm(@NonNull Application application) {
        super(application);
        mAuth = FirebaseAuth.getInstance();


    }

    public MutableLiveData<String> getSmsCode() {
        if (onSmsCodeSuccess == null) {
            onSmsCodeSuccess = new MutableLiveData<>();
        }
        return onSmsCodeSuccess;
    }

    public MutableLiveData<String> getTime() {
        if (onTimeStarted == null) {
            onTimeStarted = new MutableLiveData<>();
        }
        return onTimeStarted;
    }

    public MutableLiveData<Boolean> canResend() {
        if (onCanResend == null) {
            onCanResend = new MutableLiveData<>();
        }
        return onCanResend;
    }

    public MutableLiveData<UserModel> getUserData() {
        if (onUserDataSuccess == null) {
            onUserDataSuccess = new MutableLiveData<>();
        }
        return onUserDataSuccess;
    }

    public MutableLiveData<Boolean> getIsVerified() {
        if (isVerified == null) {
            isVerified = new MutableLiveData<>();
            isVerified.setValue(false);
        }
        return isVerified;
    }

    public void sendSmsCode(String lang, String phone_code, String phone, VerificationCodeActivity activity) {
        Log.e("rr", "rr");
        startTimer();
        //login(activity, phone_code, phone);
        mAuth.setLanguageCode(lang);
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                String smsCode = phoneAuthCredential.getSmsCode();
                getSmsCode().setValue(smsCode);
                checkValidCode(smsCode, phone_code, phone, activity);
            }

            @Override
            public void onCodeSent(@NonNull String verification_id, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verification_id, forceResendingToken);
                verificationId = verification_id;
                Log.e("ver", verification_id + "__");
                ActivityVerificationMvvm.this.forceResendingToken = forceResendingToken;
            }


            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.e("dkdkdk", e.toString());
                if (e.getMessage() != null) {
                } else {

                }
            }
        };

        PhoneAuthOptions options;
        options = PhoneAuthOptions.newBuilder(mAuth)
                .setActivity(activity)
                .setCallbacks(mCallBack)
                .setPhoneNumber(phone_code + phone)
                .setTimeout(120L, TimeUnit.SECONDS)
                .setForceResendingToken(forceResendingToken)
                .build();

        PhoneAuthProvider.verifyPhoneNumber(options);


    }

    private void startTimer() {
        canResend().setValue(false);
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
                        String time = String.format(Locale.ENGLISH, "%02d:%02d", min, sec);
                        getTime().setValue(time);


                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("onErrorVerCode", e.getMessage() + "_");
                    }

                    @Override
                    public void onComplete() {
                        getTime().setValue("00:00");
                        canResend().setValue(true);

                    }
                });

    }


    public void checkValidCode(String code, String phone_code, String phone, VerificationCodeActivity activity) {


        if (verificationId != null) {
            if (getIsVerified().getValue() != null && getIsVerified().getValue()) {
                login(activity, phone_code, phone);
            } else {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
                mAuth.signInWithCredential(credential)
                        .addOnSuccessListener(authResult -> {
                            getIsVerified().setValue(true);
                            login(activity, phone_code, phone);
                        }).addOnFailureListener(e -> {

                    if (e.getMessage() != null) {
                        Log.e("ww", e.getMessage() + "__");
                    } else {

                    }
                });
            }

        } else {
            // Toast.makeText(context, "wait sms", Toast.LENGTH_SHORT).show();
        }

    }

    private void login(Context context, String phone_code, String phone) {
       /* ProgressDialog dialog = Common.createProgressDialog(context, context.getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url).login(phone_code, phone, "yes")
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
                                } else if (response.body().getStatus() == 404) {
                                    getUserData().setValue(null);

                                } else if (response.body().getStatus() == 405) {
                                    Toast.makeText(context, R.string.cont_log, Toast.LENGTH_LONG).show();

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
                });*/
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}

package com.app.dbrah.chat_service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.app.dbrah.model.AddChatMessageModel;
import com.app.dbrah.model.SingleMessageModel;
import com.app.dbrah.remote.Api;
import com.app.dbrah.share.Common;
import com.app.dbrah.tags.Tags;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class ChatService extends Service {
    private AddChatMessageModel model;
    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            model = (AddChatMessageModel) intent.getSerializableExtra("data");
            sendMessage();
        }
        return START_STICKY;

    }

    public void sendMessage() {

        RequestBody msg_part = Common.getRequestBodyText(model.getText());
        RequestBody order_part = Common.getRequestBodyText(model.getOrder_id());
        RequestBody from_part = Common.getRequestBodyText("user");
        RequestBody repsentative_part = null;
        RequestBody msg_type_part = Common.getRequestBodyText(model.getType());
        MultipartBody.Part imagePart = null;
        RequestBody user_part = null;
        user_part = Common.getRequestBodyText(model.getUser_id());

        RequestBody provider_part=null;

        if (model.getRepresentative_id() != null) {
            repsentative_part =Common.getRequestBodyText(model.getRepresentative_id());
        }
        if (model.getProvider_id() != null) {
            provider_part = Common.getRequestBodyText(model.getProvider_id());
        }
        if (model.getType().equals("file") && model.getImage() != null) {
            imagePart = Common.getMultiPartFromPath(model.getImage(), "file");
        }


        Api.getService(Tags.base_url).sendMessages(order_part, msg_type_part, from_part, msg_part, repsentative_part, user_part, provider_part, imagePart)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<SingleMessageModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<SingleMessageModel> response) {
                        try {
                            Log.e("jfjj", response.code() + "" + response.errorBody().string());
                        } catch (Exception e) {
                            //e.printStackTrace();
                        }
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {
                                EventBus.getDefault().post(response.body().getData());
                                stopSelf();

                            }
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("chatError", e.toString());
                    }
                });

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }
}

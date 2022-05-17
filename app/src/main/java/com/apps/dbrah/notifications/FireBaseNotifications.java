package com.apps.dbrah.notifications;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;


import com.apps.dbrah.R;
import com.apps.dbrah.model.ChatUserModel;
import com.apps.dbrah.model.MessageModel;
import com.apps.dbrah.model.NotiFire;
import com.apps.dbrah.model.ProviderModel;
import com.apps.dbrah.model.RepresentModel;
import com.apps.dbrah.model.StatusResponse;
import com.apps.dbrah.model.UserModel;
import com.apps.dbrah.preferences.Preferences;
import com.apps.dbrah.remote.Api;
import com.apps.dbrah.share.App;
import com.apps.dbrah.tags.Tags;
import com.apps.dbrah.uis.activity_chat.ChatActivity;
import com.apps.dbrah.uis.activity_home.HomeActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Map;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;


public class FireBaseNotifications extends FirebaseMessagingService {
    private CompositeDisposable disposable = new CompositeDisposable();
    private Map<String, String> map;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        map = remoteMessage.getData();

        for (String key : map.keySet()) {
            Log.e("Key=", key + "_value=" + map.get(key));
        }

        manageNotification(map);


    }

    private void manageNotification(Map<String, String> map) {
        String title = map.get("title");
        String body = map.get("body");
        String notification_type = map.get("notification_type");
        String order_id = map.get("order_id");
        String order_status = map.get("status");
        String user_id = map.get("provider_id");
        String representative_id = map.get("representative_id");

        String sound_Path = "";
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        sound_Path = uri.toString();
        Intent cancelIntent = new Intent(this, BroadcastCancelNotification.class);
        PendingIntent cancelPending = PendingIntent.getBroadcast(this, 0, cancelIntent, 0);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder notificationCompat = new NotificationCompat.Builder(this, App.CHANNEL_ID)
                .setAutoCancel(true)
                .setOngoing(false)
                .setChannelId(App.CHANNEL_ID)
                .setDeleteIntent(cancelPending)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSound(Uri.parse(sound_Path), AudioManager.STREAM_NOTIFICATION);


        if (notification_type.equals("chat")) {

            String image = map.get("file");

            if (image != null && !image.isEmpty() && !map.get("type").equals("text")) {
                body = getString(R.string.attach_sent);
            } else {
                body = map.get("message");
            }


            notificationCompat.setContentTitle(title);
            notificationCompat.setStyle(new NotificationCompat.BigTextStyle().bigText(body));

            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("data", getChatUserModel(map));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
            taskStackBuilder.addNextIntent(intent);
            notificationCompat.setContentIntent(taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT));
            if (user_id == null) {
                user_id = "";
            }
            if (representative_id == null) {
                representative_id = "";
            }

            if (getRoomId() != null && (order_id.equals(getRoomId().getOrder_id())
                    && (representative_id.equals(getRoomId().getRepresentative_id())
                    || user_id.equals(getRoomId().getProvider_id())))) {
                ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                String className = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
                if (className.equals("com.apps.dbrah.uis.activity_chat.ChatActivity")) {

                    EventBus.getDefault().post(getMessageModel(map));

                } else {


                    if (getChatUserModel(map).getUser_image() != null && !getChatUserModel(map).getUser_image().isEmpty()) {

                        Glide.with(this)
                                .asBitmap()
                                .load(Uri.parse( getChatUserModel(map).getUser_image()))
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        notificationCompat.setLargeIcon(resource);
                                        manager.notify(Tags.not_id, notificationCompat.build());

                                    }
                                });
                    } else {
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);
                        notificationCompat.setLargeIcon(bitmap);
                        manager.notify(Tags.not_id, notificationCompat.build());
                    }


                }
            } else {
                if (getChatUserModel(map).getUser_image() != null && !getChatUserModel(map).getUser_image().isEmpty()) {
                    Glide.with(this)
                            .asBitmap()
                            .load(Uri.parse( getChatUserModel(map).getUser_image()))
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    notificationCompat.setLargeIcon(resource);
                                    manager.notify(Tags.not_id, notificationCompat.build());

                                }
                            });
                } else {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);
                    notificationCompat.setLargeIcon(bitmap);
                    manager.notify(Tags.not_id, notificationCompat.build());
                }

            }

        } else if (notification_type.equals("basic")) {


            if (order_status.equals("offered")) {
                title = getString(R.string.new_offer);
                body = getString(R.string.new_offer) + "-" + getChatUserModel(map).getUser_name() + "\n" + getString(R.string.order_num) + " #" + order_id;
            } else if (order_status.equals("preparing")) {
                if(body.equals("order is accepted by a representative")){
                    title = getString(R.string.your_order_accepted);
                    body = getString(R.string.your_order_accepted) + " " + getChatUserModel(map).getUser_name() + "\n" + getString(R.string.order_num) + " #" + order_id;
                }
                else  if(body.equals("order is picked up by the delivery")){
                    title = getString(R.string.picked_up);
                    body = getString(R.string.picked_up) + " " + getChatUserModel(map).getUser_name() + "\n" + getString(R.string.order_num) + " #" + order_id;
                }
                else {
                    body = getString(R.string.preparing) + " " + getChatUserModel(map).getUser_name() + "\n" + getString(R.string.order_num) + " #" + order_id;

                }
            } else if (order_status.equals("on_way")) {
                title = getString(R.string.on_the_way);
                body = getString(R.string.on_the_way) + " " + getChatUserModel(map).getUser_name() + "\n" + getString(R.string.order_num) + " #" + order_id;

            } else if (order_status.equals("delivered")) {
                title = getString(R.string.delivered);
                body = getString(R.string.delivered) + " " + getChatUserModel(map).getUser_name() + "\n" + getString(R.string.order_num) + " #" + order_id;

            }

            notificationCompat.setContentTitle(title);
            notificationCompat.setContentText(body);
            notificationCompat.setStyle(new NotificationCompat.BigTextStyle().bigText(body));

            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("order_id", order_id);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
            taskStackBuilder.addNextIntent(intent);
            notificationCompat.setContentIntent(taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT));

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);
            notificationCompat.setLargeIcon(bitmap);
            manager.notify(Tags.not_id, notificationCompat.build());
            EventBus.getDefault().post(new NotiFire(order_status));

        } else {

            notificationCompat.setContentTitle(title);
            notificationCompat.setContentText(body);
            notificationCompat.setStyle(new NotificationCompat.BigTextStyle().bigText(body));

            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("order_id", "");

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
            taskStackBuilder.addNextIntent(intent);
            notificationCompat.setContentIntent(taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT));

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);
            notificationCompat.setLargeIcon(bitmap);
            manager.notify(Tags.not_id, notificationCompat.build());
            EventBus.getDefault().post(new NotiFire(""));

        }

    }


    private MessageModel getMessageModel(Map<String, String> map) {
        String order_id = map.get("order_id");
        String id = map.get("id");
        String provider_id = map.get("provider_id");
        String user_id = map.get("user_id");
        String from_type = map.get("from_type");
        String message = map.get("message");
        String file = map.get("file");
        String type = map.get("type");
        String created_at = map.get("created_at");
        String updated_at = map.get("updated_at");

        if (file == null) {
            file = "";

        }

       ProviderModel user = new Gson().fromJson(map.get("provider"), ProviderModel.class);
        //ClientModel providerModel = new Gson().fromJson(map.get("user"), ClientModel.class);


        MessageModel messageModel = new MessageModel(id, provider_id, user_id, order_id,from_type, message, file, type, created_at, updated_at);

        return messageModel;
    }

    private ChatUserModel getChatUserModel(Map<String, String> map) {
        String order_id = map.get("order_id");
        ProviderModel userModel;
        ChatUserModel model;
        RepresentModel representModel;
        if (map.get("provider") != null) {
            userModel = new Gson().fromJson(map.get("provider"), ProviderModel.class);
            String user_id = userModel.getId();
            //  Log.e("llkkk",user_id);

            String user_name = userModel.getName();
            String user_phone = userModel.getPhone_code() + userModel.getPhone();
            String user_image = userModel.getImage();

            model = new ChatUserModel(map.get("provider_id"), map.get("user_id"), "", user_name, user_phone, user_image, order_id);

        }
        else {
            representModel = new Gson().fromJson(map.get("representative"), RepresentModel.class);
            String user_id = representModel.getId();
            String user_name = representModel.getName();
            String user_phone = representModel.getPhone_code() + representModel.getPhone();
            String user_image = representModel.getImage();

            model = new ChatUserModel("", map.get("user_id"), representModel.getId(), user_name, user_phone, user_image, order_id);

        }


        return model;

    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        if (getUserModel() == null) {
            return;
        }

        Api.getService(Tags.base_url)
                .updateFireBaseToken(getUserModel().getData().getId(), s, "android")
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
                                    UserModel userModel = getUserModel();
                                    userModel.setFirebase_token(s);
                                    setUserModel(userModel);

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

    public UserModel getUserModel() {
        Preferences preferences = Preferences.getInstance();
        return preferences.getUserData(this);
    }


    public void setUserModel(UserModel userModel) {
        Preferences preferences = Preferences.getInstance();
        preferences.createUpdateUserData(this, userModel);

    }


    public ChatUserModel getRoomId() {
        Preferences preferences = Preferences.getInstance();
        return preferences.getRoomId(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }
}

package com.app.dbrah.share;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Configuration;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.multidex.MultiDexApplication;

import com.app.dbrah.language.Language;


public class App extends MultiDexApplication {
    public static final String CHANNEL_ID = "dubrh_id_1099";
    public static final String CHANNEL_NAME = "dubrh_channel";
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //update your notifications there is no need for a Thread
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase, "ar"));
    }


    @Override
    public void onCreate() {
        super.onCreate();

        createChannel();
    }

    private void createChannel() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String sound_Path = "";
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            sound_Path = uri.toString();

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setShowBadge(true);
            channel.setDescription("dubrh channel");

            channel.setSound(Uri.parse(sound_Path), new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                    .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)

                    .build());

            manager.createNotificationChannel(channel);

        }
    }

}


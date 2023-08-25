package com.appnew.pjmk.Notification;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;

import com.appnew.pjmk.R;

public class ConfigNotification extends Application {
    public static final String CHANNEL_ID = "PJMK";

    @Override
    public void onCreate() {
        super.onCreate();
        config();
    }

    private void config() {
        //tên notification channel
        CharSequence name = getString(R.string.channel_notify);
        //mô tả notification channel
        String description = "Đã phát hiện có người";
        //Độ ưu tiên của thông báo
        int importance = NotificationManager.IMPORTANCE_HIGH;
        //lấy uri âm thông báo mặc định
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //tạo thêm audioAttributes
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION).build();
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        channel.setSound(uri, audioAttributes);
        //đăng ký thông báo
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}

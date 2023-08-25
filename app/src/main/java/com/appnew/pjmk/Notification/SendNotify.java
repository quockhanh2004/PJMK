package com.appnew.pjmk.Notification;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Date;

public class SendNotify {
    public void sendNotification(Context context, String s) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, ConfigNotification.CHANNEL_ID)
                .setContentTitle("Cảnh báo")
                .setContentText(s)
                .setColor(Color.RED)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);


        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

        } else {
            notificationManagerCompat.notify((int) new Date().getTime(), builder.build());
        }
    }
}

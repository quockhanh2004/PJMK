package com.appnew.pjmk.Services;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.appnew.pjmk.Notification.ConfigNotification;
import com.appnew.pjmk.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class BackgroundService extends Service {
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ESP32");
    private int human = 0;
    private boolean notify = false;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        read();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (human == 1) {
                    if (notify) {
//                    notify.sendNotification(BackgroundService.this, "Đã phát hiện có chuyển động");
                        sendNotification();
                    }
                }
            }
        }, 0, 7000);


        return super.onStartCommand(intent, flags, startId);
    }

    private void read() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    human = snapshot.child("human").getValue(Integer.class);
                    notify = snapshot.child("notification").getValue(Boolean.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần
            }
        });
    }

    private void sendNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, ConfigNotification.CHANNEL_ID)
                .setContentTitle("Cảnh báo")
                .setContentText("Phát hiện có người")
                .setColor(Color.RED)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            }
        }
        notificationManagerCompat.notify((int) new Date().getTime(), builder.build());
    }
}



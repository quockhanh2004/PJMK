package com.appnew.pjmk.Services;

import android.Manifest;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.appnew.pjmk.Notification.ConfigNotification;
import com.appnew.pjmk.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class ForegroundService extends Service {
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ESP32");
    private int human = 0;
    private boolean notify = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this, "Service đang chạy", Toast.LENGTH_SHORT).show();

        Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, ConfigNotification.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Dịch vụ kiểm tra cảm biến")
                .setContentText("Đang chạy ...")
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(logo).bigLargeIcon(null))
                .setLargeIcon(logo)
                .setColor(Color.RED)
                .setAutoCancel(true);

        Notification notification = builder.build();
        startForeground(1, notification);

        read();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (human == 1) {
                    if (notify) {
//                    notify.sendNotification(BackgroundService.this, "Đã phát hiện có chuyển động");
                        sendNotification();

                    }
                    // lưu lại log phát hiện người
                    saveLogToFirestore("Human == true");
                }
            }
        }, 0, 7000);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
//        super.onDestroy();
        Toast.makeText(this, "Destroy Services", Toast.LENGTH_SHORT).show();
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

    // Trong dịch vụ của bạn khi phát hiện sự kiện
    private void saveLogToFirestore(String detectionMessage) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        scheduleAutoLogCleanup();
        Map<String, Object> logData = new HashMap<>();
        long timestamp = System.currentTimeMillis() / 1000;
        logData.put("unix", timestamp);
        logData.put("time", FieldValue.serverTimestamp());
        logData.put("message", detectionMessage);

        db.collection("detectionLogs").document(String.valueOf(timestamp))
                .set(logData)
                .addOnSuccessListener(documentReference -> {
                    // Log được lưu thành công
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi nếu cần
                });
    }

    // Trong Service của bạn hoặc một nơi khác
    public void scheduleAutoLogCleanup() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();


        OneTimeWorkRequest cleanUpWorkRequest = new OneTimeWorkRequest.Builder(CleanUpLogsWorker.class)
                .setConstraints(constraints)
                .setInitialDelay(3, TimeUnit.DAYS) // Xóa sau 3 ngày
                .build();

        WorkManager.getInstance(this).enqueue(cleanUpWorkRequest);
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


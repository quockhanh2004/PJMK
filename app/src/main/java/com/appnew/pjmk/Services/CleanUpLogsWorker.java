package com.appnew.pjmk.Services;

import android.content.Context;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Calendar;
import java.util.Date;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import javax.xml.transform.Result;


// Worker class để xóa log
public class CleanUpLogsWorker extends Worker {

    public CleanUpLogsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Tính toán thời gian 3 ngày trước
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -3);
        Date threeDaysAgo = calendar.getTime();

        // Xóa tất cả các log cũ hơn 3 ngày
        db.collection("detectionLogs")
                .whereLessThan("time", threeDaysAgo)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        document.getReference().delete();
                    }
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi nếu cần
                });

        return Result.success();
    }
}

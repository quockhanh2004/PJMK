package com.appnew.pjmk.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.appnew.pjmk.Adapter.ToggleAdapter;
import com.appnew.pjmk.Model.Toggle;
import com.appnew.pjmk.Module.BlynkIoT;
import com.appnew.pjmk.Module.Callback;
import com.appnew.pjmk.Module.FirebaseManager;
import com.appnew.pjmk.R;
import com.appnew.pjmk.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mainBinding;
    private Intent intent;
    private String mail;
    private BlynkIoT blynkIoT;
    private ToggleAdapter toggleAdapter;
//    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ESP32");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        intent = getIntent();
        mail = intent.getStringExtra("mail");
        String token = intent.getStringExtra("token");
//        virtual = (MapVirtual) intent.getSerializableExtra("virtual");

        toggleAdapter = new ToggleAdapter(this);
        Toast.makeText(this, token, Toast.LENGTH_SHORT).show();
        blynkIoT = new BlynkIoT(this, token);
//        readDataBaseRealTime();
//        intent = new Intent(this, ForegroundService.class);
//        startService(intent);
        getDataBlynk();


        mainBinding.btnLog.setOnClickListener(v -> {
            startActivity(new Intent(this, LogActivity.class));
        });

        //    private MapVirtual virtual;
        FirebaseManager firebaseManager = FirebaseManager.getInstance();
        firebaseManager.getToggle(mail, new Callback<List<Toggle>>() {
            @Override
            public void onSuccess(List<Toggle> result) {
                toggleAdapter.setData(result, token);
            }

            @Override
            public void onError(Exception e) {
                Log.e("Error", "e");
            }
        });


        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mainBinding.rclToggle.setLayoutManager(gridLayoutManager);
        mainBinding.rclToggle.setAdapter(toggleAdapter);

        mainBinding.btnChangeToken.setOnClickListener(v -> {
            toggleAdapter.stopFechdata();
            blynkIoT.stopFetchingData();
            startActivity(new Intent(this, ChangeTokenActivity.class)
                    .putExtra("mail", mail)
                    .putExtra("token", token)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        });

        mainBinding.btnLogOut.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("Account", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isAutoLogin", false);
            editor.apply();
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

    }


    //    public void readDataBaseRealTime() {
//
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                // Xử lý khi dữ liệu thay đổi
//                if (snapshot.exists()) {
//
//                    // Sử dụng dữ liệu lấy được
////                    double thermal = snapshot.child("Thermal").getValue(Double.class);
////                    double humidity = snapshot.child("Humidity").getValue(Double.class);
////                    boolean toggle1 = snapshot.child("toggle1").getValue(Boolean.class);
////                    boolean toggle2 = snapshot.child("toggle2").getValue(Boolean.class);
////                    boolean toggle3 = snapshot.child("toggle3").getValue(Boolean.class);
////                    boolean toggle4 = snapshot.child("toggle4").getValue(Boolean.class);
//                    boolean notification = snapshot.child("notification").getValue(Boolean.class);
//
////                    mainBinding.Nhiet.setText(String.valueOf(thermal));
////                    mainBinding.Am.setText(String.valueOf(humidity));
////                    mainBinding.tg0.setChecked(toggle1);
////                    mainBinding.tg1.setChecked(toggle2);
////                    mainBinding.tg2.setChecked(toggle3);
////                    mainBinding.tg3.setChecked(toggle4);
////                    mainBinding.tg4.setChecked(notification);
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Xử lý khi xảy ra lỗi
//            }
//        });
//    }
    private void getDataBlynk() {

        blynkIoT.startFetchingData(0, new BlynkIoT.DataCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(String data) {
                mainBinding.Nhiet.setText(data);
            }

            @Override
            public void onError(String errorMessage) {

            }
        }, 1000, 0);
        blynkIoT.startFetchingData(1, new BlynkIoT.DataCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(String data) {
                mainBinding.Am.setText(data + "");
            }

            @Override
            public void onError(String errorMessage) {

            }
        }, 1000, 2);

        blynkIoT.startGetIsOnline(new BlynkIoT.DataCallback() {
            @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
            @Override
            public void onSuccess(String data) {
                if (Boolean.parseBoolean(data)) {
                    mainBinding.Status.setBackground(getDrawable(R.drawable.is_online));
                } else {
                    mainBinding.Status.setBackground(getDrawable(R.drawable.is_offline));
                }
            }

            @Override
            public void onError(String errorMessage) {

            }
        });


    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce = false, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        blynkIoT.stopFetchingData();
        toggleAdapter.stopFechdata();
    }

    @Override
    protected void onPause() {
        super.onPause();
        blynkIoT.stopFetchingData();
        toggleAdapter.stopFechdata();
    }
}
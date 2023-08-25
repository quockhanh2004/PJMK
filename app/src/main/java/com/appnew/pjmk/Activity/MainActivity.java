package com.appnew.pjmk.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.appnew.pjmk.Model.MapVirtual;
import com.appnew.pjmk.Services.ForegroundService;
import com.appnew.pjmk.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mainBinding;
    private Intent intent;
    private String mail;
    private MapVirtual virtual;
    //    private DeviceFireBaseRealTime fireBaseRealTime;
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ESP32");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        intent = getIntent();
        mail = intent.getStringExtra("mail");
        virtual = (MapVirtual) intent.getSerializableExtra("virtual");

        setTgBtn();
        readDataBaseRealTime();
        intent = new Intent(this, ForegroundService.class);
        startService(intent);

        mainBinding.btnLog.setOnClickListener(v -> {
            startActivity(new Intent(this, LogActivity.class));
        });

        mainBinding.tg0.setOnClickListener(v -> {
            databaseReference.child("toggle1").setValue(mainBinding.tg0.isChecked());
        });
        mainBinding.tg1.setOnClickListener(v -> {
            databaseReference.child("toggle2").setValue(mainBinding.tg1.isChecked());
        });
        mainBinding.tg2.setOnClickListener(v -> {
            databaseReference.child("toggle3").setValue(mainBinding.tg2.isChecked());
        });
        mainBinding.tg3.setOnClickListener(v -> {
            databaseReference.child("toggle4").setValue(mainBinding.tg3.isChecked());
        });
        mainBinding.tg4.setOnClickListener(v -> {
            databaseReference.child("notification").setValue(mainBinding.tg4.isChecked());
        });

    }

    public void setTgBtn() {

        mainBinding.tg0.setTextOn(virtual.getNameTg1() + " On");
        mainBinding.tg0.setTextOff(virtual.getNameTg1() + " Off");

        mainBinding.tg1.setTextOn(virtual.getNameTg2() + " On");
        mainBinding.tg1.setTextOff(virtual.getNameTg2() + " Off");

        mainBinding.tg2.setTextOn(virtual.getNameTg3() + " On");
        mainBinding.tg2.setTextOff(virtual.getNameTg3() + " Off");

        mainBinding.tg3.setTextOn(virtual.getNameTg4() + " On");
        mainBinding.tg3.setTextOff(virtual.getNameTg4() + " Off");

        mainBinding.tg4.setTextOn(virtual.getNameTg5() + " On");
        mainBinding.tg4.setTextOff(virtual.getNameTg5() + " Off");

        mainBinding.btn0.setText(virtual.getNameBtn1());
        mainBinding.btn1.setText(virtual.getNameBtn2());

        if (virtual.isAtvTg1()) {
            mainBinding.tg0.setVisibility(View.VISIBLE);
        } else {
            mainBinding.tg0.setVisibility(View.GONE);
        }
        if (virtual.isAtvTg2()) {
            mainBinding.tg1.setVisibility(View.VISIBLE);
        } else {
            mainBinding.tg1.setVisibility(View.GONE);
        }
        if (virtual.isAtvTg3()) {
            mainBinding.tg2.setVisibility(View.VISIBLE);
        } else {
            mainBinding.tg2.setVisibility(View.GONE);
        }
        if (virtual.isAtvTg4()) {
            mainBinding.tg3.setVisibility(View.VISIBLE);
        } else {
            mainBinding.tg3.setVisibility(View.GONE);
        }
        if (virtual.isAtvTg5()) {
            mainBinding.tg4.setVisibility(View.VISIBLE);
        } else {
            mainBinding.tg4.setVisibility(View.GONE);
        }

        if (virtual.isAtvBtn1()) {
            mainBinding.btn0.setVisibility(View.VISIBLE);
        } else {
            mainBinding.btn0.setVisibility(View.GONE);
        }
        if (virtual.isAtvBtn2()) {
            mainBinding.btn1.setVisibility(View.VISIBLE);
        } else {
            mainBinding.btn1.setVisibility(View.GONE);
        }
    }

    public void readDataBaseRealTime() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Xử lý khi dữ liệu thay đổi
                if (snapshot.exists()) {

                    // Sử dụng dữ liệu lấy được
                    double thermal = snapshot.child("Thermal").getValue(Double.class);
                    double humidity = snapshot.child("Humidity").getValue(Double.class);
                    boolean toggle1 = snapshot.child("toggle1").getValue(Boolean.class);
                    boolean toggle2 = snapshot.child("toggle2").getValue(Boolean.class);
                    boolean toggle3 = snapshot.child("toggle3").getValue(Boolean.class);
                    boolean toggle4 = snapshot.child("toggle4").getValue(Boolean.class);
                    boolean notification = snapshot.child("notification").getValue(Boolean.class);

                    mainBinding.Nhiet.setText(String.valueOf(thermal));
                    mainBinding.Am.setText(String.valueOf(humidity));
                    mainBinding.tg0.setChecked(toggle1);
                    mainBinding.tg1.setChecked(toggle2);
                    mainBinding.tg2.setChecked(toggle3);
                    mainBinding.tg3.setChecked(toggle4);
                    mainBinding.tg4.setChecked(notification);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi xảy ra lỗi
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

    }
}
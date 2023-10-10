package com.appnew.pjmk.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.appnew.pjmk.Module.UserFireBase;
import com.appnew.pjmk.R;

public class SplashScreenActivity extends AppCompatActivity {
    private UserFireBase userFireBaseAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("Account", Context.MODE_PRIVATE);
        boolean isRemember = sharedPreferences.getBoolean("isRemember", false);
        boolean autoLogin = sharedPreferences.getBoolean("isAutoLogin", false);

        // tự động đăng nhập
//        SharedPreferences preferencesAutoLogin = this.getSharedPreferences("AutoLogin", Context.MODE_PRIVATE);
        if (isRemember) {
            if (autoLogin) {
                Intent intent = new Intent(this, MainActivity.class);
                userFireBaseAcc = new UserFireBase(this);
                String user = sharedPreferences.getString("UserName", "");
                String token = sharedPreferences.getString("token", "???");
                intent.putExtra("token", token);
                intent.putExtra("mail", user);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }


        finish();
    }
}
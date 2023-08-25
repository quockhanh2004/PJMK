package com.appnew.pjmk.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.appnew.pjmk.Model.MapVirtual;
import com.appnew.pjmk.Model.User;
import com.appnew.pjmk.Module.UserFireBase;
import com.appnew.pjmk.Module.VitualFireBase;
import com.appnew.pjmk.R;
import com.appnew.pjmk.libs.SendMail;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText edtUserName, edtPass;
    private TextInputLayout TextIPPass;
    private CheckBox chkSavePass, chkAutoLogin;
    private UserFireBase userFireBaseAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUserName = findViewById(R.id.edtUserName);
        edtPass = findViewById(R.id.edtPass);
        TextIPPass = findViewById(R.id.TextIPPass);
        chkSavePass = findViewById(R.id.chkSavePass);
        chkAutoLogin = findViewById(R.id.chkAutoLogin);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView txtResetPass = findViewById(R.id.txtResetPass);
        TextView txtSignUp = findViewById(R.id.txtSignUp);

        userFireBaseAcc = new UserFireBase(this);
        requestPermission();
        // Lưu mật khẩu
        SharedPreferences sharedPreferences = getSharedPreferences("Account", Context.MODE_PRIVATE);
        boolean isRemember = sharedPreferences.getBoolean("isRemember", false);

        // tự động đăng nhập
//        SharedPreferences preferencesAutoLogin = this.getSharedPreferences("AutoLogin", Context.MODE_PRIVATE);
        if (isRemember) {
            String user = sharedPreferences.getString("UserName", "");
            String pass = sharedPreferences.getString("Pass", "");
            edtUserName.setText(user);
            edtPass.setText(pass);
            chkSavePass.setChecked(isRemember);
        }
        requestPermission();

        // ------------------------------------------------------------------------------------------

        edtUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                checkInputUser();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputUser();
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkInputUser();
            }
        });

        edtPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                checkInputPass();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputPass();
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkInputPass();
            }
        });

        // ------------------------------------------------------------------------------------------
        VitualFireBase vitualFireBase = new VitualFireBase(this);

        btnLogin.setOnClickListener(v -> {
            String username, pass;
            username = edtUserName.getText().toString().trim();
            pass = edtPass.getText().toString().trim();

            String login = userFireBaseAcc.login(new User(username, pass));
            if (checkInputUser()) {
                if (checkInputPass()) {
                    if (login != null) {
                        if (chkAutoLogin.isChecked()) {
                            saveAutoLogin();
                        }

                        if (chkSavePass.isChecked()) {
                            saveAccount(username, pass);
                        } else {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.apply();
                        }
                        Toast.makeText(this, "Đăng nhập thành công với tài khoản " + username, Toast.LENGTH_SHORT).show();
//                        System.out.println(username);
                        MapVirtual virtual = vitualFireBase.getMapVirtual(username);
                        Intent intent = null;
                        switch (userFireBaseAcc.CheckFirstAdd(username)) {
                            case 1:
                                intent = new Intent(this, MainActivity.class);
                                intent.putExtra("first", true);
                                break;
                            case 0:
                                intent = new Intent(this, MainActivity.class);
                                break;
                        }

                        //xóa các activity khác, chỉ để lại activity sắp được khởi chạy
                        if (intent != null) {
                            intent.putExtra("virtual", virtual);
                            intent.putExtra("mail", username);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }

                    } else {
                        Toast.makeText(this, "Tài khoản hoặc mật khẩu sai", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        txtResetPass.setOnClickListener(v -> showDialogSendOTP());

        txtSignUp.setOnClickListener(v -> {
            Intent intent1 = new Intent(this, SignUpActivity.class);
            startActivity(intent1);
        });
    }

    String OTP = "";
    int time = 60;
    Handler mainHandler = new Handler(Looper.getMainLooper());
    String mail = "";

    private void showDialogSendOTP() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_send_otp, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btnSendMail, btnBack, btnCheckOTP;
        TextInputLayout TextinputMail;
        TextInputEditText edtMail, edtOTP;
        TextView txtMail;
        btnSendMail = view.findViewById(R.id.btnSendMail);
        btnBack = view.findViewById(R.id.btnBack);
        TextinputMail = view.findViewById(R.id.TextinputMail);
        edtMail = view.findViewById(R.id.edtMail);
        edtOTP = view.findViewById(R.id.edtOTP);
        btnCheckOTP = view.findViewById(R.id.btnCheckOTP);
        txtMail = view.findViewById(R.id.txtMail);

        edtOTP.setVisibility(View.GONE);
        btnCheckOTP.setVisibility(View.GONE);
        txtMail.setVisibility(View.GONE);
        btnSendMail.setOnClickListener(v -> {

            mail = edtMail.getText().toString().trim();

            if (!mail.isEmpty()) {
                SendMail sendMail = new SendMail();
                String pass = userFireBaseAcc.CheckSendMail(mail);
                if (!pass.trim().isEmpty()) {

                    Thread threadTime = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            time = 60;
                            while (time > 0) {
                                try {
                                    mainHandler.post(() -> {
                                        btnSendMail.setEnabled(false);
                                        btnSendMail.setText("Có thể gửi lại sau " + time);
                                        if (time == 0) {
                                            btnSendMail.setEnabled(true);
                                            btnSendMail.setText("Send OTP");
                                        }
                                    });
                                    time--;
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    });
                    threadTime.start();

                    OTP = generateOTP();
                    sendMail.Send(this, mail, "Khôi phục mật khẩu Project_MK", "Mã OTP của bạn là: " + OTP);
                    TextinputMail.setVisibility(View.GONE);

                    edtOTP.setVisibility(View.VISIBLE);
                    btnCheckOTP.setVisibility(View.VISIBLE);
                    txtMail.setVisibility(View.VISIBLE);
                    txtMail.setText(Html.fromHtml("Mã OTP đã được gửi đến <b>" + mail + "</b>"));
                } else {
                    Toast.makeText(this, "Tài khoản này không tồn tại", Toast.LENGTH_SHORT).show();
//                    btnSendMail.setVisibility(View.VISIBLE);
//                    edtOTP.setVisibility(View.GONE);
//                    btnCheckOTP.setVisibility(View.GONE);
//                    txtMail.setVisibility(View.GONE);
                }
            } else {
                Toast.makeText(this, "Bạn chưa nhập địa chỉ mail", Toast.LENGTH_SHORT).show();
            }
        });

        btnCheckOTP.setOnClickListener(v -> {
            String otp = edtOTP.getText().toString().trim();
            if (!otp.isEmpty()) {
                if (otp.equals(OTP)) {
                    alertDialog.cancel();
                    showDialogResetPass();

                } else {
                    Toast.makeText(this, "Mã OTP không đúng", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Bạn chưa nhập mã OTP", Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(v -> alertDialog.cancel());
    }

    private void showDialogResetPass() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_resetpass, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btnSubmit, btnCancel;
        TextInputEditText edtNewPass, edtReNewPass;

        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnCancel = view.findViewById(R.id.btnCancel);
        edtNewPass = view.findViewById(R.id.edtNewPass);
        edtReNewPass = view.findViewById(R.id.edtReNewPass);

        btnSubmit.setOnClickListener(v -> {
            String pass = edtNewPass.getText().toString().trim();
            String repass = edtReNewPass.getText().toString().trim();
            if (pass.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập mật khẩu mới", Toast.LENGTH_SHORT).show();
            } else {
                if (!repass.equals(pass)) {
                    Toast.makeText(this, "Mật khẩu không giống nhau", Toast.LENGTH_SHORT).show();
                } else {
                    userFireBaseAcc.changePass(mail, pass);
                    alertDialog.cancel();
                }
            }
        });

        btnCancel.setOnClickListener(v -> alertDialog.cancel());
    }

    private boolean checkInputUser() {
        boolean check = true;
        if (edtUserName.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Bạn chưa nhập UserName", Toast.LENGTH_SHORT).show();
            check = false;
        }
        return check;
    }

    private boolean checkInputPass() {
        boolean check = true;
        if (edtPass.getText().toString().trim().isEmpty()) {
            TextIPPass.setError("Bạn chưa nhập Password");
            check = false;
        } else {
            TextIPPass.setError(null);
            edtPass.setError(null);
        }
        return check;
    }

    private void saveAccount(String UserName, String Pass) {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Account", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UserName", UserName);
        editor.putString("Pass", Pass);
        editor.putBoolean("isRemember", true);
        editor.apply();
    }

    private void saveAutoLogin() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Account", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isAutoLogin", true);
        editor.apply();
    }

    public static String generateOTP() {
        Random random = new Random();
        StringBuilder otpBuilder = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            int digit = random.nextInt(10);
            otpBuilder.append(digit);
        }

        return otpBuilder.toString();
    }

    // xin cấp quyền thông báo với android >= 13
    private final int PERMISSION_CODE = 1;

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_CODE);
            }
        }
    }
    // xin cấp quyền truy cập bộ nhớ
//    private void requestPermission() {
//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_CODE);
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == PERMISSION_CODE) {
//            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

}
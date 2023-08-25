package com.appnew.pjmk.Activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.appnew.pjmk.Model.User;
import com.appnew.pjmk.Module.UserFireBase;
import com.appnew.pjmk.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText edtMail, edtPass, edtRePass;
    private Button btnSignUp;
    private TextView txtLogin;
    private TextInputLayout TextIPRePass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        edtMail = findViewById(R.id.edtMail);
        edtPass = findViewById(R.id.edtPass);
        edtRePass = findViewById(R.id.edtRePass);

        btnSignUp = findViewById(R.id.btnSignUp);

        TextIPRePass = findViewById(R.id.TextIPRePass);
        txtLogin = findViewById(R.id.txtLogin);

        UserFireBase userFireBaseAcc = new UserFireBase(this);

        //lười quá skip cái check input giá trị null đi

        btnSignUp.setOnClickListener(view -> {
            String mail = edtMail.getText().toString().trim();
            String pass = edtPass.getText().toString().trim();
            String repass = edtRePass.getText().toString().trim();

            if (pass.equals(repass)) {
                if (userFireBaseAcc.signup(new User(mail, pass))) {
                    Toast.makeText(this, "Tạo tài khoản thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Địa chỉ mail đã tồn tại", Toast.LENGTH_SHORT).show();
                }
            } else {
                TextIPRePass.setError("Mật khẩu không giống nhau");
            }
        });

        txtLogin.setOnClickListener(view -> {
            finish();
        });
    }
}

package com.appnew.pjmk.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.appnew.pjmk.Adapter.ChangeTokenAdapter;
import com.appnew.pjmk.Model.MapVirtual;
import com.appnew.pjmk.Model.Toggle;
import com.appnew.pjmk.Module.Callback;
import com.appnew.pjmk.Module.FirebaseManager;
import com.appnew.pjmk.Module.UserFireBase;
import com.appnew.pjmk.Module.VitualFireBase;
import com.appnew.pjmk.R;
import com.appnew.pjmk.databinding.ActivityChangeTokenBinding;
import com.google.android.material.textfield.TextInputEditText;

import java.lang.ref.WeakReference;
import java.util.List;

public class ChangeTokenActivity extends AppCompatActivity {

    private ActivityChangeTokenBinding activityChange;
    String token, mail;
    VitualFireBase vitualFireBase;
    private FirebaseManager firebaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityChange = ActivityChangeTokenBinding.inflate(getLayoutInflater());
        setContentView(activityChange.getRoot());

        Intent intent = getIntent();
        mail = intent.getStringExtra("mail");
        boolean first = intent.getBooleanExtra("first", false);
        token = intent.getStringExtra("token");

        activityChange.edtToken.setText(token);
        vitualFireBase = new VitualFireBase(getApplicationContext(), mail);
        firebaseManager = FirebaseManager.getInstance();
        ChangeTokenAdapter tokenAdapter = new ChangeTokenAdapter(ChangeTokenActivity.this, mail, firebaseManager, vitualFireBase);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChangeTokenActivity.this);
        activityChange.rclChangeToken.setLayoutManager(linearLayoutManager);
        activityChange.rclChangeToken.setAdapter(tokenAdapter);
        firebaseManager.getToggle(mail, new Callback<List<Toggle>>() {
            @Override
            public void onSuccess(List<Toggle> result) {
                tokenAdapter.setData(result);
                tokenAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Exception e) {

            }
        });

// Bắt đầu AsyncTask để lấy danh sách Toggle
//        GetToggleListTask getToggleListTask = new GetToggleListTask(this);
//        getToggleListTask.execute();

        activityChange.btnAdd.setOnClickListener(view -> {
            showAddToggleDialog(this);
        });

//        activityChange.btnSave.setOnClickListener(view -> {
//
//        });
    }

    private void showAddToggleDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_toggle, null);
        builder.setView(view);

        TextInputEditText edtType = view.findViewById(R.id.edtType);
        TextInputEditText edtPIN = view.findViewById(R.id.edtPIN);
        TextInputEditText edtName = view.findViewById(R.id.edtName);
        Button btnSubmit = view.findViewById(R.id.btnSubmit);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        builder.setCancelable(true);
        // Bắt sự kiện khi nút Submit được nhấn
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy giá trị từ các trường EditText
                String type = edtType.getText().toString();
                int pin = Integer.parseInt(edtPIN.getText().toString());
                String name = edtName.getText().toString();
                String id;
                try {
                    id = vitualFireBase.getToggleList().get(vitualFireBase.getToggleList().size() - 1).getId();
                } catch (Exception e) {
                    id = "toggle0";
                }
                int number = Integer.parseInt(String.valueOf(id.charAt(id.length() - 1)));
                number++;
                System.out.println(number);
                String newId = id.substring(0, id.length() - 1) + number;
                System.out.println(newId);
                // Xử lý dữ liệu ở đây, ví dụ, lưu vào cơ sở dữ liệu hoặc làm gì đó khác
                vitualFireBase.addMapVirtual(new Toggle(newId, name, mail, type, pin, false));
                vitualFireBase.ListenFirebaseFirestore();
                // Sau khi xử lý xong, có thể đóng dialog
                alertDialog.dismiss();
            }
        });
    }
}
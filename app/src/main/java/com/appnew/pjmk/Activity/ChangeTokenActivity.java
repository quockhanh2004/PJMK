package com.appnew.pjmk.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.appnew.pjmk.Model.MapVirtual;
import com.appnew.pjmk.Module.UserFireBase;
import com.appnew.pjmk.Module.VitualFireBase;
import com.appnew.pjmk.databinding.ActivityChangeTokenBinding;

public class ChangeTokenActivity extends AppCompatActivity {

    private ActivityChangeTokenBinding activityChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityChange = ActivityChangeTokenBinding.inflate(getLayoutInflater());
        setContentView(activityChange.getRoot());

        Intent intent = getIntent();
        String mail = intent.getStringExtra("mail");
        boolean first = intent.getBooleanExtra("first", false);
        MapVirtual virtual = (MapVirtual) intent.getSerializableExtra("virtual");

        VitualFireBase vitualFireBase = new VitualFireBase(this);
//        vitualFireBase.ListenFirebaseFirestore();


//        activityChange.edtToken.setText(virtual.getToken());

        activityChange.edtName0.setText(virtual.getNameTg1());
        activityChange.edtName1.setText(virtual.getNameTg2());
        activityChange.edtName2.setText(virtual.getNameTg3());
        activityChange.edtName3.setText(virtual.getNameTg4());
        activityChange.edtNameBtn0.setText(virtual.getNameBtn1());
        activityChange.edtNameBtn1.setText(virtual.getNameBtn2());

//        activityChange.edtVNumber0.setText(String.valueOf(virtual.getNumberVTg1()));
//        activityChange.edtVNumber1.setText(String.valueOf(virtual.getNumberVTg2()));
//        activityChange.edtVNumber2.setText(String.valueOf(virtual.getNumberVTg3()));
//        activityChange.edtVNumber3.setText(String.valueOf(virtual.getNumberVTg4()));
//        activityChange.edtVNumberBtn0.setText(String.valueOf(virtual.getNumberVBtn1()));
//        activityChange.edtVNumberBtn1.setText(String.valueOf(virtual.getNumberVBtn2()));
//
//        activityChange.edtHumidity.setText(String.valueOf(virtual.getNumberVHumidity()));
//        activityChange.edtThermal.setText(String.valueOf(virtual.getNumberVThermal()));

        activityChange.swtg0.setChecked(virtual.isAtvTg1());
        activityChange.swtg1.setChecked(virtual.isAtvTg2());
        activityChange.swtg2.setChecked(virtual.isAtvTg3());
        activityChange.swtg3.setChecked(virtual.isAtvTg4());
        activityChange.swbtn0.setChecked(virtual.isAtvBtn1());
        activityChange.swbtn1.setChecked(virtual.isAtvBtn2());

        activityChange.btnSave.setOnClickListener(view -> {
//            virtual.setToken(activityChange.edtToken.getText().toString().trim());
            virtual.setNameTg1(activityChange.edtName0.getText().toString().trim());
            virtual.setNameTg2(activityChange.edtName1.getText().toString().trim());
            virtual.setNameTg3(activityChange.edtName2.getText().toString().trim());
            virtual.setNameTg4(activityChange.edtName3.getText().toString().trim());
            virtual.setNameBtn1(activityChange.edtNameBtn0.getText().toString().trim());
            virtual.setNameBtn2(activityChange.edtNameBtn1.getText().toString().trim());

//            virtual.setNumberVTg1(Integer.parseInt(activityChange.edtVNumber0.getText().toString()));
//            virtual.setNumberVTg2(Integer.parseInt(activityChange.edtVNumber1.getText().toString()));
//            virtual.setNumberVTg3(Integer.parseInt(activityChange.edtVNumber2.getText().toString()));
//            virtual.setNumberVTg4(Integer.parseInt(activityChange.edtVNumber3.getText().toString()));
//            virtual.setNumberVBtn1(Integer.parseInt(activityChange.edtVNumberBtn0.getText().toString()));
//            virtual.setNumberVBtn2(Integer.parseInt(activityChange.edtVNumberBtn1.getText().toString()));

            virtual.setAtvTg1(activityChange.swtg0.isChecked());
            virtual.setAtvTg2(activityChange.swtg1.isChecked());
            virtual.setAtvTg3(activityChange.swtg2.isChecked());
            virtual.setAtvTg4(activityChange.swtg3.isChecked());
            virtual.setAtvBtn1(activityChange.swbtn0.isChecked());
            virtual.setAtvBtn2(activityChange.swbtn1.isChecked());
            virtual.setMail(mail);
            if (first) {
                vitualFireBase.addMapVirtual(virtual);
                UserFireBase userFireBase = new UserFireBase(this);
                userFireBase.setFirstAdd(mail);
            } else {
                vitualFireBase.setMapVirtual(virtual);
            }
            Intent intent1 = new Intent(this, MainActivity.class);
            intent1.putExtra("mail", mail);
            intent1.putExtra("virtual", virtual);
            startActivity(intent1);
            finish();
        });
    }

//    private Switch swtg0, swtg1, swtg2, swtg3, swbtn0, swbtn1;
//    private TextInputEditText edtToken, edtThermal, edtHumidity, edtName0,
//            edtName1, edtName2, edtName3, edtNameBtn0, edtNameBtn1,
//            edtVNumber0, edtVNumber1, edtVNumber2, edtVNumber3, edtVNumberBtn0, edtVNumberBtn1;
//    private Button btnSave;
}
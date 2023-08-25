package com.appnew.pjmk.Module;

import android.content.Context;
import android.view.LayoutInflater;

import com.appnew.pjmk.Model.MapVirtual;
import com.appnew.pjmk.databinding.ActivityMainBinding;
import com.google.firebase.database.DatabaseReference;

public class DeviceFireBaseRealTime {
    //    private String mail;
    private ActivityMainBinding mainBinding;
    private MapVirtual virtual;
    private DatabaseReference databaseReference;

    public DeviceFireBaseRealTime(Context context, MapVirtual virtual, DatabaseReference databaseReference) {
//        this.mail = mail;
        this.virtual = virtual;
        this.databaseReference = databaseReference;
        mainBinding = ActivityMainBinding.inflate(LayoutInflater.from(context));
    }


}

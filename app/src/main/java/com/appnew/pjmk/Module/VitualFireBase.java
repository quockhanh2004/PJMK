package com.appnew.pjmk.Module;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.appnew.pjmk.Model.MapVirtual;
import com.appnew.pjmk.Model.User;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VitualFireBase {
    private MapVirtual mapVirtual = new MapVirtual();
    private Context context;
    private FirebaseFirestore database;
    private List<MapVirtual> virtualList = new ArrayList<>();

    public VitualFireBase(Context context) {
        this.context = context;
        database = FirebaseFirestore.getInstance();
        ListenFirebaseFirestore();
    }

    public MapVirtual getMapVirtual(String mail) {
        ListenFirebaseFirestore();
        for (MapVirtual V : virtualList) {
            if (V.getMail().equals(mail)) {
                return V;
            }
        }
        MapVirtual map = new MapVirtual();
        map.setDefaultAll();
        return map;
    }

    public void addMapVirtual(MapVirtual virtual) {
        HashMap<String, Object> mapTodo = virtual.convertHashMap();
        database.collection(MapVirtual.TABLE_NAME).document(virtual.getMail()).set(mapTodo)
                .addOnSuccessListener(unused ->
                        Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, "Thêm thất bại", Toast.LENGTH_SHORT).show());

    }

    public void setMapVirtual(MapVirtual virtual) {
        database.collection(MapVirtual.TABLE_NAME).document(virtual.getMail())
                .update(virtual.convertHashMap())
                .addOnSuccessListener(unused -> {
                    Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                    Log.e("Lỗi", "onFailure: " + e);
                });
    }

    public void ListenFirebaseFirestore() {
        database.collection(MapVirtual.TABLE_NAME).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("TAG", "Listen virtual failed", error);
                    return;
                }
                if (value != null) {
                    for (DocumentChange dc : value.getDocumentChanges()) {
                        switch (dc.getType()) {
                            case ADDED:
                                dc.getDocument().toObject(MapVirtual.class);
                                virtualList.add(dc.getDocument().toObject(MapVirtual.class));
                                break;
                            case MODIFIED:
                                MapVirtual updateVirtual = dc.getDocument().toObject(MapVirtual.class);
                                if (dc.getOldIndex() == dc.getNewIndex()) {
                                    virtualList.set(dc.getOldIndex(), updateVirtual);
                                } else {
                                    virtualList.remove(dc.getOldIndex());
                                    virtualList.add(updateVirtual);
                                }
                                break;
                            case REMOVED:
                                dc.getDocument().toObject(User.class);
                                virtualList.remove(dc.getOldIndex());
                        }
                    }
                }
            }
        });
    }
}

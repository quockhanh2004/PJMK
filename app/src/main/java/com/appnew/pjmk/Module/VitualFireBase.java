package com.appnew.pjmk.Module;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.appnew.pjmk.Model.Toggle;
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
    private Toggle toggle = new Toggle();
    private Context context;
    private String mail;
    private FirebaseFirestore database;
    private List<Toggle> toggleList = new ArrayList<>();

    public VitualFireBase(Context context) {
        this.context = context;
    }

    public VitualFireBase(Context context, String mail) {
        this.context = context;
        this.mail = mail.trim();
        database = FirebaseFirestore.getInstance();
        ListenFirebaseFirestore();
    }

    public List<Toggle> getToggleList() {
        return toggleList;
    }

    public void addMapVirtual(Toggle virtual) {
        HashMap<String, Object> mapTodo = virtual.convertHashMap();
        System.out.println(Toggle.TABLE_NAME + "/" + mail + "/" + mail + "/" + virtual.getId());
        database.collection(Toggle.TABLE_NAME).document(mail).collection(mail).document(virtual.getId()).set(mapTodo)
                .addOnSuccessListener(unused ->
                        Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, "Thêm thất bại", Toast.LENGTH_SHORT).show());

    }

    public void setMapVirtual(Toggle toggle) {
        database.collection(Toggle.TABLE_NAME).document(mail).collection(mail).document(toggle.getId())
                .update(toggle.convertHashMap())
                .addOnSuccessListener(unused -> {
                    Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                    Log.e("Lỗi", "onFailure: " + e);
                });
    }

    public void ListenFirebaseFirestore() {
        database.collection(Toggle.TABLE_NAME).document(mail).collection(mail).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                                dc.getDocument().toObject(Toggle.class);
                                toggleList.add(dc.getDocument().toObject(Toggle.class));
                                break;
                            case MODIFIED:
                                Toggle updateVirtual = dc.getDocument().toObject(Toggle.class);
                                if (dc.getOldIndex() == dc.getNewIndex()) {
                                    toggleList.set(dc.getOldIndex(), updateVirtual);
                                } else {
                                    toggleList.remove(dc.getOldIndex());
                                    toggleList.add(updateVirtual);
                                }
                                break;
                            case REMOVED:
                                dc.getDocument().toObject(Toggle.class);
                                toggleList.remove(dc.getOldIndex());
                        }
                    }
                }
            }
        });
    }
}

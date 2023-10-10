package com.appnew.pjmk.Module;

import android.net.DnsResolver;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.appnew.pjmk.Model.Toggle;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FirebaseManager {

    private static FirebaseManager instance;

    private FirebaseFirestore firestore;

    private FirebaseManager() {
        // Khởi tạo Firebase và lắng nghe dữ liệu ở đây
        firestore = FirebaseFirestore.getInstance();
    }

    public static FirebaseManager getInstance() {
        if (instance == null) {
            instance = new FirebaseManager();
        }
        return instance;
    }

    public FirebaseFirestore getFirestore() {
        return firestore;
    }

    // Thêm các phương thức khác để lấy dữ liệu từ Firebase và thực hiện lắng nghe dữ liệu ở đây

    public void setMapVirtual(Toggle toggle) {
        firestore.collection(Toggle.TABLE_NAME).document(toggle.getMail()).collection(toggle.getMail()).document(toggle.getId())
                .update(toggle.convertHashMap())
                .addOnSuccessListener(unused -> {
//                    Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
//                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                    Log.e("Lỗi", "onFailure: " + e);
                });
    }

    public void getToggle(String mail, Callback<List<Toggle>> callback) {
        List<Toggle> list = new ArrayList<>();
        firestore.collection(Toggle.TABLE_NAME).document(mail).collection(mail).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                                list.add(dc.getDocument().toObject(Toggle.class));
//                                callback.onSuccess(list);
                                break;
                            case MODIFIED:
                                Toggle updateVirtual = dc.getDocument().toObject(Toggle.class);
                                if (dc.getOldIndex() == dc.getNewIndex()) {
                                    list.set(dc.getOldIndex(), updateVirtual);
                                } else {
                                    list.remove(dc.getOldIndex());
                                    list.add(updateVirtual);
                                }
//                                callback.onSuccess(list);
                                break;
                            case REMOVED:
                                dc.getDocument().toObject(Toggle.class);
                                list.remove(dc.getOldIndex());

                        }
                    }
                    callback.onSuccess(list);
                }
            }
        });
    }
}
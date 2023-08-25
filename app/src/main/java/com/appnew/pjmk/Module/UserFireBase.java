package com.appnew.pjmk.Module;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.appnew.pjmk.Model.User;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserFireBase {

    Context context;
    private final FirebaseFirestore database;
    private User tempUser;

    public UserFireBase(Context context) {
        this.context = context;
        database = FirebaseFirestore.getInstance();
        ListenFirebaseFirestore();
    }


    private List<User> userList = new ArrayList<>();

    public String login(User user) {
        for (User us : userList) {
            if (us.getMail().equals(user.getMail().trim())) {
                if (BCrypt.checkpw(user.getPass(), us.getPass())) {
                    tempUser = us;
                    return us.getMail();
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    public boolean signup(User user) {
        for (User us : userList) {
            if (user.getMail().trim().equals(us.getMail())) {
                Toast.makeText(context, "Địa chỉ mail đã tồn tại", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        String hashedPassword = BCrypt.hashpw(user.getPass(), BCrypt.gensalt());
        user.setPass(hashedPassword);
        user.setFirstAdd(1);
        HashMap<String, Object> mapTodo = user.convertHashMap();
        database.collection(User.TableName).document(user.getMail()).set(mapTodo)
                .addOnSuccessListener(unused ->
                        Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, "Thêm thất bại", Toast.LENGTH_SHORT).show());
        return true;
    }

    boolean success = false;

    public void changePass(String mail, String pass) {
        String hashedPassword = BCrypt.hashpw(pass, BCrypt.gensalt());
        database.collection(User.TableName).document(mail).update("pass", hashedPassword).addOnSuccessListener(unused -> {
//            this.success = true;
        }).addOnFailureListener(e -> {
//            success = false;
        });
    }

    public String CheckSendMail(String mail) {
        for (User us : userList) {
            if (mail.equals(us.getMail())) {
                return us.getPass();
            }
        }
        return "";
    }

    public int CheckFirstAdd(String mail) {
        if (mail.equals(tempUser.getMail())) {
            if (tempUser.getFirstAdd() == 1) {
                return 1;
            }
            return 0;
        }
        return 2;
    }

    public void setFirstAdd(String mail) {
        Map<String, Object> updateFirst = new HashMap<>();
        updateFirst.put("firstAdd", 0);
        database.collection(User.TableName).document(mail).update(updateFirst)
                .addOnSuccessListener(unused -> {
                })
                .addOnFailureListener(e -> {
                    Log.e("SetFirst", "Đã xảy ra lỗi trong quá trình chuyển firstAdd thành false");
                });
    }

    public void ListenFirebaseFirestore() {
        database.collection(User.TableName).addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.e("TAG", "Listen failed", error);
                return;
            }
            if (value != null) {
                for (DocumentChange dc : value.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            dc.getDocument().toObject(User.class);
                            userList.add(dc.getDocument().toObject(User.class));
                            break;
                        case MODIFIED:
                            User updateUser = dc.getDocument().toObject(User.class);
                            if (dc.getOldIndex() == dc.getNewIndex()) {
                                userList.set(dc.getOldIndex(), updateUser);
                            } else {
                                userList.remove(dc.getOldIndex());
                                userList.add(updateUser);
                            }
                            break;
                        case REMOVED:
                            dc.getDocument().toObject(User.class);
                            userList.remove(dc.getOldIndex());
                    }
                }
            }
        });
    }
}

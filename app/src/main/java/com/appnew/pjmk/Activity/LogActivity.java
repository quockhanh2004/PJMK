package com.appnew.pjmk.Activity;

import static com.appnew.pjmk.R.drawable.ic_pause;
import static com.appnew.pjmk.R.drawable.ic_play;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;

import com.appnew.pjmk.Adapter.LogAdapter;
import com.appnew.pjmk.Model.Log;
import com.appnew.pjmk.Model.User;
import com.appnew.pjmk.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class LogActivity extends AppCompatActivity {
    FirebaseFirestore db;
    LogAdapter logAdapter;
    RecyclerView rcl;
    ImageButton imageButton;
    boolean pause = false;
    List<Log> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        db = FirebaseFirestore.getInstance();
        read();
        rcl = findViewById(R.id.rcl);
        imageButton = findViewById(R.id.pause);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcl.setLayoutManager(layoutManager);
        logAdapter = new LogAdapter(this);
        list.add(new Log(0, null, null));
        rcl.setAdapter(logAdapter);
        imageButton.setOnClickListener(v -> {
            if (!pause) {
                pause = true;
                imageButton.setImageResource(ic_play);
            } else {
                pause = false;
                imageButton.setImageResource(ic_pause);
            }
        });

    }

    private void read() {
        db.collection(Log.Table_Name).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    android.util.Log.e("TAG", "Listen failed", error);
                    return;
                }
                if (value != null) {
                    for (DocumentChange dc : value.getDocumentChanges()) {
                        switch (dc.getType()) {
                            case ADDED:
                                dc.getDocument().toObject(Log.class);
                                list.add(dc.getDocument().toObject(Log.class));
                                logAdapter.add(dc.getDocument().toObject(Log.class));

                                if (!pause) {
                                    rcl.scrollToPosition(list.size() - 2);
                                }
                                break;
                            case MODIFIED:
                                Log updateLog = dc.getDocument().toObject(Log.class);
                                if (dc.getOldIndex() == dc.getNewIndex()) {
                                    logAdapter.update(updateLog, dc.getOldIndex());
//                                    list.set(dc.getOldIndex(), updateLog);
                                } else {
                                    logAdapter.delete(dc.getOldIndex());
//                                    list.remove(dc.getOldIndex());
                                    logAdapter.add(updateLog);
//                                    list.add(updateLog);
                                }
                                break;
                            case REMOVED:
                                dc.getDocument().toObject(User.class);
                                logAdapter.delete(dc.getOldIndex());
//                                list.remove(dc.getOldIndex());
                        }
                    }
                }
            }
        });
    }
}
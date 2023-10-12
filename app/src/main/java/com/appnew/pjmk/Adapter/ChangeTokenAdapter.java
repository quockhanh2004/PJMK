package com.appnew.pjmk.Adapter;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.appnew.pjmk.Model.Toggle;
import com.appnew.pjmk.Module.FirebaseManager;
import com.appnew.pjmk.Module.VitualFireBase;
import com.appnew.pjmk.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class ChangeTokenAdapter extends RecyclerView.Adapter<ChangeTokenAdapter.ViewHoler> {
    private Context context;
    private List<Toggle> list;
    private String mail;
    private FirebaseManager firebaseManager;
    private VitualFireBase vitualFireBase;

    public ChangeTokenAdapter(Context context, String mail, FirebaseManager firebaseManager, VitualFireBase vitualFireBase) {
        this.context = context;
        this.mail = mail;
        this.firebaseManager = firebaseManager;
        this.vitualFireBase = vitualFireBase;
    }

    public void setData(List<Toggle> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ChangeTokenAdapter.ViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_change_token, parent, false);
        return new ViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChangeTokenAdapter.ViewHoler holder, int position) {
        holder.txtType.setText(list.get(holder.getAdapterPosition()).getType());
        holder.txtName.setText(list.get(holder.getAdapterPosition()).getName());
        holder.txtPIN.setText(String.valueOf(list.get(holder.getAdapterPosition()).getPIN()));

        holder.layoutItemChange.setOnClickListener(v -> {
            showEditToggleDialog(context, list.get(holder.getAdapterPosition()));
        });
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    private void showEditToggleDialog(Context context, Toggle toggle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
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

        edtType.setText(toggle.getType());
        edtName.setText(toggle.getName());
        edtPIN.setText(String.valueOf(toggle.getPIN()));

        // Bắt sự kiện khi nút Submit được nhấn
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy giá trị từ các trường EditText
                String type = edtType.getText().toString();
                int pin = Integer.parseInt(edtPIN.getText().toString());
                String name = edtName.getText().toString();
//                String id = vitualFireBase.getToggleList().get(vitualFireBase.getToggleList().size()).getId();
//                int number = id.charAt(id.length() - 1);
//                number++;
//                String newId = id.substring(0, id.length() -1) + String.valueOf(number);
                // Xử lý dữ liệu ở đây, ví dụ, lưu vào cơ sở dữ liệu hoặc làm gì đó khác
                vitualFireBase.addMapVirtual(new Toggle(toggle.getId(), name, mail, type, pin, false));
                vitualFireBase.ListenFirebaseFirestore();
                // Sau khi xử lý xong, có thể đóng dialog
                alertDialog.dismiss();
            }
        });
    }

    public class ViewHoler extends RecyclerView.ViewHolder {
        TextView txtType, txtPIN, txtName;
        LinearLayout layoutItemChange;

        public ViewHoler(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPIN = itemView.findViewById(R.id.txtPIN);
            txtType = itemView.findViewById(R.id.txtType);
            layoutItemChange = itemView.findViewById(R.id.layoutItemChange);
        }
    }
}

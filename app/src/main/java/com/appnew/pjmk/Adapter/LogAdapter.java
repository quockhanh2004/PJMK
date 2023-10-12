package com.appnew.pjmk.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appnew.pjmk.Model.Log;
import com.appnew.pjmk.R;

import java.util.ArrayList;
import java.util.List;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.ViewHoler> {
    List<Log> list = new ArrayList<>();
    Context context;

    public LogAdapter(Context context) {
        this.context = context;
        list.add(new Log(0, null, null));
//        list = logs;
    }

    @NonNull
    @Override
    public LogAdapter.ViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_log, parent, false);
        return new ViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogAdapter.ViewHoler holder, int position) {
        holder.time.setText(list.get(holder.getAdapterPosition()).getTime());
        if (list.get(holder.getAdapterPosition()).getUnix() > 0) {
            holder.unix.setText(String.valueOf(list.get(holder.getAdapterPosition()).getUnix()));
        }
        holder.human.setText(list.get(holder.getAdapterPosition()).getHuman());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void add(Log log) {
        list.add(list.size() - 1, log);
        notifyItemInserted(list.size() - 1);
    }

    public void update(Log log, int position) {
        list.set(position, log);
        notifyItemInserted(position);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void delete(List<Log> logs) {
        list.clear();
        list = logs;
        notifyDataSetChanged();
    }

    public void delete(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHoler extends RecyclerView.ViewHolder {
        TextView time, human, unix;

        public ViewHoler(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            human = itemView.findViewById(R.id.human);
            unix = itemView.findViewById(R.id.unix);
        }
    }
}

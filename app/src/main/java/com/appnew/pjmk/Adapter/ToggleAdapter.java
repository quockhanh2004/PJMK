package com.appnew.pjmk.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.appnew.pjmk.Model.Toggle;
import com.appnew.pjmk.Module.BlynkIoT;
import com.appnew.pjmk.Module.FirebaseManager;
import com.appnew.pjmk.R;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ToggleAdapter extends RecyclerView.Adapter<ToggleAdapter.ViewHoler> {
    private final Context context;
    private List<Toggle> toggleList;
    private String token;
    FirebaseManager firebaseManager;
    BlynkIoT blynkIoT;

    public ToggleAdapter(Context context) {
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Toggle> toggleList, String token) {
        this.toggleList = toggleList;
        this.token = token;
        blynkIoT = new BlynkIoT(context, token);
        firebaseManager = FirebaseManager.getInstance();
        notifyDataSetChanged();
//        reLoadData();
    }

    @Override
    public ToggleAdapter.ViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_toggle, parent, false);
        return new ViewHoler(view);
    }

    @Override
    public void onBindViewHolder(ToggleAdapter.ViewHoler holder, int position) {
//        boolean status = false;
        toggleList.get(holder.getAdapterPosition());
        Timer task = new Timer();
        if (toggleList.get(holder.getAdapterPosition()).getType().equalsIgnoreCase("toggle")) {
            task.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
//                for (Toggle toggle: toggleList) {
                    blynkIoT.fetchData(toggleList.get(holder.getAdapterPosition()).getPIN(), new BlynkIoT.DataCallback() {
                        @Override
                        public void onSuccess(String data) {
                            toggleList.get(holder.getAdapterPosition()).setStatus(data.equals("1"));
//                            System.out.println(data);
                            if (toggleList.get(holder.getAdapterPosition()).isStatus()) {
                                holder.layoutToggle.setBackgroundColor(Color.parseColor("#00FF00"));
                                holder.txtNameToggle.setText(toggleList.get(holder.getAdapterPosition()).getName() + " ON");
                            } else {
                                holder.layoutToggle.setBackgroundColor(Color.parseColor("#000000"));
                                holder.txtNameToggle.setText(toggleList.get(holder.getAdapterPosition()).getName() + " OFF");
                            }
                        }

                        @Override
                        public void onError(String errorMessage) {

                        }
                    });
//                    status.equals() = toggle.isStatus();
                }
            }, 0, 1000);
        } else {
            holder.layoutToggle.setBackgroundColor(Color.parseColor("#F44336"));
            holder.txtNameToggle.setText(toggleList.get(holder.getAdapterPosition()).getName());
        }


        holder.layoutToggle.setOnClickListener(v -> {
            if (toggleList.get(holder.getAdapterPosition()).getType().equalsIgnoreCase("toggle")) {
                blynkIoT.sendData(toggleList.get(holder.getAdapterPosition()).getPIN(), !toggleList.get(holder.getAdapterPosition()).isStatus() ? "1" : "0");
                toggleList.get(holder.getAdapterPosition()).setStatus(!toggleList.get(holder.getAdapterPosition()).isStatus());
                if (toggleList.get(holder.getAdapterPosition()).isStatus()) {
                    holder.layoutToggle.setBackgroundColor(Color.parseColor("#00FF00"));
                    holder.txtNameToggle.setText(toggleList.get(holder.getAdapterPosition()).getName() + " ON");
                } else {
                    holder.layoutToggle.setBackgroundColor(Color.parseColor("#000000"));
                    holder.txtNameToggle.setText(toggleList.get(holder.getAdapterPosition()).getName() + " OFF");
                }
            } else {
                blynkIoT.sendData(toggleList.get(holder.getAdapterPosition()).getPIN(), "1");
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        blynkIoT.sendData(toggleList.get(holder.getAdapterPosition()).getPIN(), "0");
                    }
                }, 3000);

            }


//            firebaseManager.setMapVirtual(toggle);
        });

    }

    private void reLoadData() {
        Timer task = new Timer();
        task.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (Toggle toggle : toggleList) {
                    blynkIoT.fetchData(toggle.getPIN(), new BlynkIoT.DataCallback() {
                        @Override
                        public void onSuccess(String data) {
                            toggle.setStatus(Boolean.parseBoolean(data));
//                            firebaseManager.setMapVirtual(toggle);

                        }

                        @Override
                        public void onError(String errorMessage) {

                        }
                    });
                }
            }
        }, 0, 1000);
    }

    public void stopFechdata(){
        blynkIoT.stopFetchingData();
    }

    @Override
    public int getItemCount() {
        if (toggleList != null) {
            return toggleList.size();
        }
        return 0;
    }

    public class ViewHoler extends RecyclerView.ViewHolder {
        private LinearLayout layoutToggle;
        private TextView txtNameToggle;

        public ViewHoler(View itemView) {
            super(itemView);
            layoutToggle = itemView.findViewById(R.id.layoutToggle);
            txtNameToggle = itemView.findViewById(R.id.txtNameToggle);
        }
    }
}

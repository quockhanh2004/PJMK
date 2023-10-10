package com.appnew.pjmk.Module;//package com.appNew.test.Module;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Timer;
import java.util.TimerTask;

public class BlynkIoT {

    private String TOKEN = "";

    private static final String BASE_URL = "https://blynk.cloud/external/api/";

    private RequestQueue requestQueue;
    private final Context context;
    private Timer fetchDataTimer;

    public interface DataCallback {
        void onSuccess(String data);

        void onError(String errorMessage);
    }

    public BlynkIoT(Context context, String token) {
        this.context = context.getApplicationContext();
        TOKEN = token;
        requestQueue = getRequestQueue();
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public void fetchData(int VNumber, final DataCallback callback) {
        String apiUrl = BASE_URL + "get?token=" + TOKEN + "&V" + VNumber;
//        System.out.println(apiUrl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiUrl,
                callback::onSuccess,
                error -> {
                    // Xử lý lỗi
                    callback.onError(error.getMessage());
                });

        requestQueue.add(stringRequest);
    }

    public void sendData(int VNumber, String data) {
        String apiUrl = BASE_URL + "update?token=" + TOKEN + "&V" + VNumber + "=" + data;
//        Toast.makeText(context, data, Toast.LENGTH_SHORT).show();
//        System.out.println(apiUrl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiUrl,
                System.out::println,
                System.out::println);

        requestQueue.add(stringRequest);
    }

    public void isOnline(DataCallback callback) {
        String apiUrl = BASE_URL + "isHardwareConnected?token=" + TOKEN;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiUrl,
                data -> callback.onSuccess(data),
                error -> {
                });
        requestQueue.add(stringRequest);
    }

    public void startGetIsOnline(DataCallback callback) {
//        stopFetchingData(); // Đảm bảo dừng lấy dữ liệu trước khi bắt đầu lại

        fetchDataTimer = new Timer();
        fetchDataTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                isOnline(callback);
            }
        }, 0, 500);
    }

    public void startFetchingData(int VNumber, final DataCallback callback, long interval, long delay) {
//        stopFetchingData(); // Đảm bảo dừng lấy dữ liệu trước khi bắt đầu lại

        fetchDataTimer = new Timer();
        fetchDataTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                fetchData(VNumber, callback);
            }
        }, delay, interval);
    }

//    public void stopFetchingData() {
//        if (fetchDataTimer != null) {
//            fetchDataTimer.cancel();
//            fetchDataTimer = null;
//        }
//    }
}

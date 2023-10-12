package com.appnew.pjmk.Model;

import com.google.firebase.firestore.FieldValue;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;

public class Log {
    private long unix;
    private com.google.firebase.Timestamp time;
    private String message;
    public static String Table_Name = "detectionLogs";

    public Log() {
    }

    public long getUnix() {
        return unix;
    }

    public void setUnix(long unix) {
        this.unix = unix;
    }

    public String getTime() {
        try {
            if (unix == 0) {
                return "";
            }
            Instant instant = Instant.ofEpochSecond(unix);
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = localDateTime.format(formatter);
            return formattedDateTime;
        } catch (Exception e) {
//            long currentUnixTimeInSeconds = System.currentTimeMillis() / 1000;
//            Instant instant = Instant.ofEpochSecond(currentUnixTimeInSeconds);
//            LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//            String formattedDateTime = localDateTime.format(formatter);
            return "";
        }
    }

    public void setTime(com.google.firebase.Timestamp time) {
        this.time = time;
    }

    public String getHuman() {
        return message;
    }

    public void setHuman(String message) {
        this.message = message;
    }

    public Log(long unix, com.google.firebase.Timestamp time, String message) {
        this.unix = unix;
        this.time = time;
        this.message = message;
    }
}

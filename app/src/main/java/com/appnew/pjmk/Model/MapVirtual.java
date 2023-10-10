package com.appnew.pjmk.Model;

import java.io.Serializable;
import java.util.HashMap;

public class MapVirtual implements Serializable {

    public static String TABLE_NAME = "MapVirtual";
    private String mail, type, Name;
    private int PIN;

    public MapVirtual() {
    }

    public MapVirtual(String type, int PIN, String Name) {
        this.type = type;
        this.PIN = PIN;
        this.Name = Name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getPIN() {
        return PIN;
    }

    public void setPIN(int PIN) {
        this.PIN = PIN;
    }

    public HashMap<String, Object> convertHashMap() {
        HashMap<String, Object> work = new HashMap<>();

        work.put("PIN", PIN);
        work.put("type", type);
        work.put("Name", Name);
        work.put("mail", mail);

        return work;
    }
}

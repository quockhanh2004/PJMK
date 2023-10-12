package com.appnew.pjmk.Model;

import java.util.HashMap;

public class Toggle {
    public static final String TABLE_NAME = "MapVirtual";
    private String id, name, mail, type;
    private int PIN;
    private boolean status;

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

    public int getPIN() {
        return PIN;
    }

    public void setPIN(int PIN) {
        this.PIN = PIN;
    }

    public Toggle() {

    }

    public Toggle(String name, int pin, boolean status) {
//        this.id = id;
        this.name = name;
        this.PIN = pin;
        this.status = status;
    }

    public Toggle(String id, String name, String mail, String type, int PIN, boolean status) {
        this.id = id;
        this.name = name;
        this.mail = mail;
        this.type = type;
        this.PIN = PIN;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public HashMap<String, Object> convertHashMap() {
        HashMap<String, Object> work = new HashMap<>();

        work.put("PIN", PIN);
        work.put("type", type);
        work.put("name", name);
        work.put("mail", mail);
        work.put("id", id);
        work.put("status", status);

        return work;
    }
}

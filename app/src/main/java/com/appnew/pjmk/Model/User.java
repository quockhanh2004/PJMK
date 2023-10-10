package com.appnew.pjmk.Model;

import java.io.Serializable;
import java.util.HashMap;

public class User implements Serializable {
    public static String TableName = "User_Project_MK";
    private String mail;
    private String pass;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String token;
    private int firstAdd = 0;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getFirstAdd() {
        return firstAdd;
    }

    public void setFirstAdd(int firstAdd) {
        this.firstAdd = firstAdd;
    }

    public User() {
    }

    public User(String mail, String pass) {
        this.mail = mail;
        this.pass = pass;
    }

    public User(String mail, String pass, int firstAdd) {
        this.mail = mail;
        this.pass = pass;
        this.firstAdd = firstAdd;
    }

    public HashMap<String, Object> convertHashMap() {
        HashMap<String, Object> work = new HashMap<>();
        work.put("mail", mail);
        work.put("firstAdd", firstAdd);
        work.put("pass", pass);
        work.put("token", token);
        return work;
    }
}

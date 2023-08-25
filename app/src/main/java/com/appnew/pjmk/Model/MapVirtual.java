package com.appnew.pjmk.Model;

import java.io.Serializable;
import java.util.HashMap;

public class MapVirtual implements Serializable {

    public static String TABLE_NAME = "MapVirtual";
    private String mail, nameTg1, nameTg2,
            nameTg3, nameTg4, nameTg5,
            nameBtn1, nameBtn2;
    //    private int numberVThermal, numberVHumidity,
//            numberVTg1, numberVTg2, numberVTg3, numberVTg4,
//            numberVBtn1, numberVBtn2;
    private boolean atvTg1, atvTg2, atvTg3, atvTg4, atvTg5,
            atvBtn1, atvBtn2;

    public MapVirtual() {
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

//    public String getToken() {
//        return token;
//    }
//
//    public void setToken(String token) {
//        this.token = token;
//    }

    public String getNameTg1() {
        return nameTg1;
    }

    public void setNameTg1(String nameTg1) {
        this.nameTg1 = nameTg1;
    }

    public String getNameTg2() {
        return nameTg2;
    }

    public void setNameTg2(String nameTg2) {
        this.nameTg2 = nameTg2;
    }

    public String getNameTg3() {
        return nameTg3;
    }

    public void setNameTg3(String nameTg3) {
        this.nameTg3 = nameTg3;
    }

    public String getNameTg4() {
        return nameTg4;
    }

    public String getNameTg5() {
        return nameTg5;
    }

    public void setNameTg5(String nameTg5) {
        this.nameTg5 = nameTg5;
    }

    public boolean isAtvTg5() {
        return atvTg5;
    }

    public void setAtvTg5(boolean atvTg5) {
        this.atvTg5 = atvTg5;
    }

    public void setNameTg4(String nameTg4) {
        this.nameTg4 = nameTg4;
    }

    public String getNameBtn1() {
        return nameBtn1;
    }

    public void setNameBtn1(String nameBtn1) {
        this.nameBtn1 = nameBtn1;
    }

    public String getNameBtn2() {
        return nameBtn2;
    }

    public void setNameBtn2(String nameBtn2) {
        this.nameBtn2 = nameBtn2;
    }

//    public int getNumberVThermal() {
//        return numberVThermal;
//    }
//
//    public void setNumberVThermal(int numberVThermal) {
//        this.numberVThermal = numberVThermal;
//    }
//
//    public int getNumberVHumidity() {
//        return numberVHumidity;
//    }
//
//    public void setNumberVHumidity(int numberVHumidity) {
//        this.numberVHumidity = numberVHumidity;
//    }
//
//    public int getNumberVTg1() {
//        return numberVTg1;
//    }
//
//    public void setNumberVTg1(int numberVTg1) {
//        this.numberVTg1 = numberVTg1;
//    }
//
//    public int getNumberVTg2() {
//        return numberVTg2;
//    }
//
//    public void setNumberVTg2(int numberVTg2) {
//        this.numberVTg2 = numberVTg2;
//    }
//
//    public int getNumberVTg3() {
//        return numberVTg3;
//    }
//
//    public void setNumberVTg3(int numberVTg3) {
//        this.numberVTg3 = numberVTg3;
//    }
//
//    public int getNumberVTg4() {
//        return numberVTg4;
//    }
//
//    public void setNumberVTg4(int numberVTg4) {
//        this.numberVTg4 = numberVTg4;
//    }
//
//    public int getNumberVBtn1() {
//        return numberVBtn1;
//    }
//
//    public void setNumberVBtn1(int numberVBtn1) {
//        this.numberVBtn1 = numberVBtn1;
//    }
//
//    public int getNumberVBtn2() {
//        return numberVBtn2;
//    }
//
//    public void setNumberVBtn2(int numberVBtn2) {
//        this.numberVBtn2 = numberVBtn2;
//    }

    public boolean isAtvTg1() {
        return atvTg1;
    }

    public void setAtvTg1(boolean atvTg1) {
        this.atvTg1 = atvTg1;
    }

    public boolean isAtvTg2() {
        return atvTg2;
    }

    public void setAtvTg2(boolean atvTg2) {
        this.atvTg2 = atvTg2;
    }

    public boolean isAtvTg3() {
        return atvTg3;
    }

    public void setAtvTg3(boolean atvTg3) {
        this.atvTg3 = atvTg3;
    }

    public boolean isAtvTg4() {
        return atvTg4;
    }

    public void setAtvTg4(boolean atvTg4) {
        this.atvTg4 = atvTg4;
    }

    public boolean isAtvBtn1() {
        return atvBtn1;
    }

    public void setAtvBtn1(boolean atvBtn1) {
        this.atvBtn1 = atvBtn1;
    }

    public boolean isAtvBtn2() {
        return atvBtn2;
    }

    public void setAtvBtn2(boolean atvBtn2) {
        this.atvBtn2 = atvBtn2;
    }

    public void setDefaultAll() {
        nameBtn1 = "Button 1";
        nameBtn2 = "Button 2";
        nameTg1 = "Toggle 1";
        nameTg2 = "Toggle 2";
        nameTg3 = "Toggle 3";
        nameTg4 = "Toggle 4";
        atvBtn1 = false;
        atvBtn2 = false;
        atvTg1 = false;
        atvTg2 = false;
        atvTg3 = false;
        atvTg4 = false;
//        numberVThermal = 0;
//        numberVHumidity = 1;
//        numberVTg1 = 2;
//        numberVTg2 = 3;
//        numberVTg3 = 4;
//        numberVTg4 = 5;
//        numberVBtn1 = 6;
//        numberVBtn2 = 7;

//        String id = UUID.randomUUID().toString();
//        token = id;
    }

    public HashMap<String, Object> convertHashMap() {
        HashMap<String, Object> work = new HashMap<>();

        work.put("nameTg1", nameTg1);
//        work.put("numberVTg1", numberVTg1);
        work.put("atvTg1", atvTg1);

        work.put("nameTg2", nameTg2);
//        work.put("numberVTg2", numberVTg2);
        work.put("atvTg2", atvTg2);

        work.put("nameTg3", nameTg3);
//        work.put("numberVTg3", numberVTg3);
        work.put("atvTg3", atvTg3);

        work.put("nameTg4", nameTg4);
//        work.put("numberVTg4", numberVTg4);
        work.put("atvTg4", atvTg4);

        work.put("nameTg5", nameTg5);
        work.put("atvTg5", atvTg5);

        work.put("nameBtn1", nameBtn1);
//        work.put("numberVBtn1", numberVBtn1);
        work.put("atvBtn1", atvBtn1);


        work.put("nameBtn2", nameBtn2);
//        work.put("numberVBtn2", numberVBtn2);
        work.put("atvBtn2", atvBtn2);

        work.put("mail", mail);
//        work.put("token", token);
//        work.put("numberVThermal", numberVThermal);
//        work.put("numberVHumidity", numberVHumidity);

        return work;
    }
}

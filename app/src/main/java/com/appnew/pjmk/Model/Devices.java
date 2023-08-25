package com.appnew.pjmk.Model;

public class Devices {
    double thermal;
    double humidity;
    boolean toggle1;
    boolean toggle2;
    boolean toggle3;
    boolean toggle4;
    boolean button1;
    boolean button2;

    public Devices() {
    }

    public Devices(double thermal, double humidity, boolean toggle1, boolean toggle2, boolean toggle3, boolean toggle4, boolean button1, boolean button2) {
        this.thermal = thermal;
        this.humidity = humidity;
        this.toggle1 = toggle1;
        this.toggle2 = toggle2;
        this.toggle3 = toggle3;
        this.toggle4 = toggle4;
        this.button1 = button1;
        this.button2 = button2;
    }

    public double getThermal() {
        return thermal;
    }

    public void setThermal(double thermal) {
        this.thermal = thermal;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public boolean isToggle1() {
        return toggle1;
    }

    public void setToggle1(boolean toggle1) {
        this.toggle1 = toggle1;
    }

    public boolean isToggle2() {
        return toggle2;
    }

    public void setToggle2(boolean toggle2) {
        this.toggle2 = toggle2;
    }

    public boolean isToggle3() {
        return toggle3;
    }

    public void setToggle3(boolean toggle3) {
        this.toggle3 = toggle3;
    }

    public boolean isToggle4() {
        return toggle4;
    }

    public void setToggle4(boolean toggle4) {
        this.toggle4 = toggle4;
    }

    public boolean isButton1() {
        return button1;
    }

    public void setButton1(boolean button1) {
        this.button1 = button1;
    }

    public boolean isButton2() {
        return button2;
    }

    public void setButton2(boolean button2) {
        this.button2 = button2;
    }
}

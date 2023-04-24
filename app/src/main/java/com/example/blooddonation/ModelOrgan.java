package com.example.blooddonation;


public class ModelOrgan {

    public ModelOrgan(){

    }
    String address,answer,email,map,mono,name,password,pin,question,time,id;

    public ModelOrgan(String address, String email,String map, String mono, String name, String password, String pin, String time, String id) {
        this.address = address;
        this.email = email;
        this.map = map;
        this.mono = mono;
        this.name = name;
        this.password = password;
        this.pin = pin;
        this.time = time;
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getMono() {
        return mono;
    }

    public void setMono(String mono) {
        this.mono = mono;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

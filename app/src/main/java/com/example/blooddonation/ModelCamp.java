package com.example.blooddonation;

public class ModelCamp {

    String address,date,id,image,map,mono,name,pin,tag,time,userid;

    public ModelCamp(String address, String date, String id, String image, String map, String mono, String name, String pin, String tag, String time, String userid) {
        this.address = address;
        this.date = date;
        this.id = id;
        this.image = image;
        this.map = map;
        this.mono = mono;
        this.name = name;
        this.pin = pin;
        this.tag = tag;
        this.time = time;
        this.userid = userid;
    }

    public ModelCamp() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}

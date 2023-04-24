package com.example.blooddonation;

public class ModelNGO {

    public ModelNGO(){}

    String address,email,id,img_ngo,mono,name,pin;

    public ModelNGO(String address, String email, String id, String img_ngo, String mono, String name, String pin) {
        this.address = address;
        this.email = email;
        this.id = id;
        this.img_ngo = img_ngo;
        this.mono = mono;
        this.name = name;
        this.pin = pin;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg_ngo() {
        return img_ngo;
    }

    public void setImg_ngo(String img_ngo) {
        this.img_ngo = img_ngo;
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
}

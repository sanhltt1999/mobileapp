package com.intern.cndd.model;

public class Users {

    private String phone;
    private String password;
    private String name;
    private String address;
    private String picture;

    public Users() {}

    public Users(String phone, String password, String name, String address, String picture) {
        this.phone = phone;
        this.password = password;
        this.name = name;
        this.address = address;
        this.picture = picture;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}

package com.intern.cndd.model;

public class Users {

    private String id;
    private String phone;
    private String password;
    private String name;
    private String address;
    private String image;

    public Users() {}

    public Users(String id, String phone, String password, String name, String address, String picture) {
        this.id = id;
        this.phone = phone;
        this.password = password;
        this.name = name;
        this.address = address;
        this.image = picture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

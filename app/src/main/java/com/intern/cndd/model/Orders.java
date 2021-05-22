package com.intern.cndd.model;

public class Orders {

    private String id;
    private String name;
    private String phone;
    private String address;
    private String state;
    private String date;
    private String time;
    private String cost;

    public Orders(){}

    public Orders(String id, String name, String phone, String address, String state, String date, String time, String cost) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.state = state;
        this.date = date;
        this.time = time;
        this.cost = cost;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}

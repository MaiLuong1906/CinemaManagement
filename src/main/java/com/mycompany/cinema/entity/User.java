package com.mycompany.cinema.entity;

import java.time.LocalDate;

public class User {
    private int id;
    private String username;
    private int gender;
    private String address;
    LocalDate dob;
    // cons
    public User(){
    }
    // con day du tham so
    public User(int id, String username, int gender, String address, LocalDate dob){
        this.id = id;
        this.username = username;
        this.gender = gender;
        this.address = address;
        this.dob = dob;
    }
    // cac get set
    public String getAddress() {
        return address;
    }
    public LocalDate getDob() {
        return dob;
    }
    public String getGender() {
        if(gender==0) return "Female";
        else return "Male";
    }
    public int getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setDob(LocalDate dob) {
        this.dob = dob;
    }
    public void setGender(int gender) {
        if(gender==0||gender==1) this.gender = gender;
        else System.out.println("Invalid");
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;

/**
 *
 * @author LENOVO
 */
public class User {
    private String fullname;
    private String email;
    private String phone;
    private String password;
    private int role_Id;
    private Date dateCreated;

    public User() {
    }

    public User( String fullname, String email, String phone, String password, int role_Id, Date dateCreated) {
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role_Id = role_Id;
        this.dateCreated = dateCreated;
    }

    public User(String fullname, String email, String phone, String password, int role_Id) {
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role_Id = role_Id;
    }


    

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public int getRole() {
        return role_Id;
    }

    public void setRole(int role_Id) {
        this.role_Id = role_Id;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public String toString() {
        return ", fullname=" + fullname + ", email=" + email + ", phone=" + phone + ", password=" + password + ", role=" + role_Id + ", dateCreated=" + dateCreated + '}';
    }
    
    
}

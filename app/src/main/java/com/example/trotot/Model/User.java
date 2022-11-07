package com.example.trotot.Model;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private int user_id;
    private String username;
    private String password;
    private String email;
    private String phone_number;
    private String full_name;
    private Date create_at;
    private int role_id;
    private String avatar;

    public User(){}

    public User(int user_id, String username, String avatar) {
        this.user_id = user_id;
        this.username = username;
        this.avatar = avatar;
    }

    public User(String avatar){
        this.avatar = avatar;
    }

    public User(String username, String avatar) {
        this.username = username;
        this.avatar = avatar;
    }

    public User(int user_id, String username, String password, String email, String phone_number, String full_name, Date create_at, int role_id, String avatar) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone_number = phone_number;
        this.full_name = full_name;
        this.create_at = create_at;
        this.role_id = role_id;
        this.avatar = avatar;
    }

    public User(String username, String email, String phone_number, String full_name, String avatar) {
        this.username = username;
        this.email = email;
        this.phone_number = phone_number;
        this.full_name = full_name;
        this.avatar = avatar;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public Date getCreate_at() {
        return create_at;
    }

    public void setCreate_at(Date create_at) {
        this.create_at = create_at;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}

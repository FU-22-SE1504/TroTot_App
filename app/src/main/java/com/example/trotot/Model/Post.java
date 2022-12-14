package com.example.trotot.Model;

import java.io.Serializable;

public class Post implements Serializable {
    private int post_id;
    private int user_id;
    private String title;
    private String description;
    private String address;
    private String price;
    private int type_id;
    private String poster;
    private String contact;

    public Post(int post_id, int user_id, String title, String description, String address, String price, int type_id, String poster, String contact) {
        this.post_id = post_id;
        this.user_id = user_id;
        this.title = title;
        this.description = description;
        this.address = address;
        this.price = price;
        this.type_id = type_id;
        this.poster = poster;
        this.contact = contact;
    }

    public int getPost_id() {
        return post_id;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}

package com.example.trotot.Post;

public class Recentpost {
    private String recentpost_name;
    private String recentpost_location;
    private Integer recentpost_img;
    private String recentpost_describe;
    private String recentpost_price;

    public Recentpost(String recentpost_name, String recentpost_location, Integer recentpost_img,String recentpost_describe, String recentpost_price) {
        this.recentpost_name = recentpost_name;
        this.recentpost_location = recentpost_location;
        this.recentpost_img = recentpost_img;
        this.recentpost_describe = recentpost_describe;
        this.recentpost_price = recentpost_price;
    }


    public String getRecentpost_name() {
        return recentpost_name;
    }

    public void setRecentpost_name(String recentpost_name) {
        this.recentpost_name = recentpost_name;
    }

    public String getRecentpost_location() {
        return recentpost_location;
    }

    public void setRecentpost_location(String recentpost_location) {
        this.recentpost_location = recentpost_location;
    }

    public Integer getRecentpost_img() {
        return recentpost_img;
    }

    public void setRecentpost_img(Integer recentpost_img) {
        this.recentpost_img = recentpost_img;
    }

    public String getRecentpost_describe() {
        return recentpost_describe;
    }

    public void setRecentpost_describe(String recentpost_describe) {
        this.recentpost_describe = recentpost_describe;
    }

    public String getRecentpost_price() {
        return recentpost_price;
    }

    public void setRecentpost_price(String recentpost_price) {
        this.recentpost_price = recentpost_price;
    }
}

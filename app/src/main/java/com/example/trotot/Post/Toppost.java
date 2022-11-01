package com.example.trotot.Post;

public class Toppost {
    private String top_name;
    private String top_location;
    private int top_img;

    public Toppost(String top_name, String top_location, int top_img) {
        this.top_name = top_name;
        this.top_location = top_location;
        this.top_img = top_img;
    }

    public String getTop_name() {
        return top_name;
    }

    public void setTop_name(String top_name) {
        this.top_name = top_name;
    }

    public String getTop_location() {
        return top_location;
    }

    public void setTop_location(String top_location) {
        this.top_location = top_location;
    }

    public int getTop_img() {
        return top_img;
    }

    public void setTop_img(int top_img) {
        this.top_img = top_img;
    }
}

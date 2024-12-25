package com.example.proje11;

public class Post {
    private int id;
    private String username;
    private String postText;
    private String imagePath;

    public Post(int id, String username, String postText, String imagePath) {
        this.id = id;
        this.username = username;
        this.postText = postText;
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPostText() {
        return postText;
    }

    public String getImagePath() {
        return imagePath;
    }
}

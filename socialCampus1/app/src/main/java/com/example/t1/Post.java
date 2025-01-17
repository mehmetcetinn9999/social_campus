package com.example.t1;

public class Post {
    private String username;
    private String content;
    private String timestamp;
    private String imageUri;


    public Post(String username, String content, String timestamp, String imageUri) {
        this.username = username;
        this.content = content;
        this.timestamp = timestamp;
        this.imageUri = imageUri;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }


    //UNİT TEST
    public boolean isValid() {
        return username != null && !username.isEmpty();
    }
}

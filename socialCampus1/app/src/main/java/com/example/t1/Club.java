package com.example.t1;

public class Club {
    private String username;
    private String clubName;
    private String content;
    private String imageUri;
    private long timestamp;

    public Club(String username, String clubName, String content, String imageUri, long timestamp) {
        this.username = username;
        this.clubName = clubName;
        this.content = content;
        this.imageUri = imageUri;
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public String getClubName() {
        return clubName;
    }

    public String getContent() {
        return content;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

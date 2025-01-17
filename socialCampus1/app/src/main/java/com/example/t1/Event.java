package com.example.t1;

public class Event {
    private String username;
    private String content;
    private String timestamp;
    private String imageUri;
    private String eventDate;
    private String price;

    public Event(String username, String content, String timestamp, String imageUri, String eventDate, String price) {
        this.username = username;
        this.content = content;
        this.timestamp = timestamp;
        this.imageUri = imageUri;
        this.eventDate = eventDate;
        this.price = price;
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

    public String getEventDate() {
        return eventDate;
    }

    public String getPrice() {
        return price;
    }
}
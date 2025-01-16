package com.example.t1;

public class Event {
    private String description;
    private String date;
    private String price;
    private String contact;

    public Event(String description, String date, String price, String contact) {
        this.description = description;
        this.date = date;
        this.price = price;
        this.contact = contact;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getPrice() {
        return price;
    }

    public String getContact() {
        return contact;
    }
}

package com.example.t1;

public class Club {
    private String name;
    private String description;
    private String contact;

    // Constructor
    public Club(String name, String description, String contact) {
        this.name = name;
        this.description = description;
        this.contact = contact;
    }

    // Getter Metodları
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getContact() {
        return contact;
    }
}

package com.example.event;

public class UserEvent {

    private String email;
    private String action;

    public UserEvent() {}

    public UserEvent(String email, String action) {
        this.email = email;
        this.action = action;
    }

    // Getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}

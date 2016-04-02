package com.gordonchild.websocket.domain;

public class ChatSession extends Session {

    private String username;

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

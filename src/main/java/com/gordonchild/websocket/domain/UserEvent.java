package com.gordonchild.websocket.domain;

public class UserEvent extends SocketData {

    private String username;

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

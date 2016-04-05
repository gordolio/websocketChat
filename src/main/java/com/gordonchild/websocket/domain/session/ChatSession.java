package com.gordonchild.websocket.domain.session;

public abstract class ChatSession implements Session {

    private String username;
    private String roomName;

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoomName() {
        return this.roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

}

package com.gordonchild.websocket.domain.request;

public class JoinRoomRequest extends RoomRequest {

    private String username;

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

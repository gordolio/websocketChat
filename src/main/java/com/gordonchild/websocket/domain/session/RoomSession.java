package com.gordonchild.websocket.domain.session;

public abstract class RoomSession implements Session {

    private String roomName;

    public String getRoomName() {
        return this.roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}

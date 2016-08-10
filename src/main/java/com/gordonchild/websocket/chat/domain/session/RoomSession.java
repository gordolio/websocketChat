package com.gordonchild.websocket.chat.domain.session;

public abstract class RoomSession implements Session {

    private String roomName;

    public String getRoomName() {
        return this.roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}

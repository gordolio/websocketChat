package com.gordonchild.websocket.exception;

public class RoomNotFoundException extends RuntimeException {

    private final String roomId;

    public RoomNotFoundException(String roomId) {
        super(String.format("The session could not be found: %s", roomId));
        this.roomId = roomId;
    }

    public String getRoomId() {
        return this.roomId;
    }

}

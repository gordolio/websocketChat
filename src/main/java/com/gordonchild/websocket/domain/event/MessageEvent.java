package com.gordonchild.websocket.domain.event;

public class MessageEvent extends UserData {

    private String message;

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

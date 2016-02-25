package com.gordonchild.websocket.domain;

public class ChatMessage extends UserEvent {

    private String message;

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

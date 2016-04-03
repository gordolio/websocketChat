package com.gordonchild.websocket.domain;

import com.gordonchild.websocket.domain.event.UserData;

public class ChatMessage extends UserData {

    private String message;

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

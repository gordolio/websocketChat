package com.gordonchild.websocket.chat.domain.request;

public class SendMessageRequest extends RoomRequest {

    private String message;

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

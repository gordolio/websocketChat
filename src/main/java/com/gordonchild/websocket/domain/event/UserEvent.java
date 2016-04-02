package com.gordonchild.websocket.domain.event;

import com.gordonchild.websocket.domain.SocketData;

public class UserEvent extends SocketData {

    private String username;
    private String publicId;

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPublicId() {
        return this.publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }
}

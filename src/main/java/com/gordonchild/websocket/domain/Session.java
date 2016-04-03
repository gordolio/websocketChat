package com.gordonchild.websocket.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Session {

    private String sessionId;
    private String publicId;
    @JsonIgnore
    private String socketSessionId;

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getPublicId() {
        return this.publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public String getSocketSessionId() {
        return this.socketSessionId;
    }

    public void setSocketSessionId(String socketSessionId) {
        this.socketSessionId = socketSessionId;
    }
}

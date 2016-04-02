package com.gordonchild.websocket.domain;

public class Session {

    private String sessionId;
    private String publicId;

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
}

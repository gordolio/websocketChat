package com.gordonchild.websocket.chat.domain.request;

public class ConnectRequest {

    private String sessionId;

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

}

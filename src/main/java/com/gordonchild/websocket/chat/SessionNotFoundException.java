package com.gordonchild.websocket.chat;

public class SessionNotFoundException extends RuntimeException {

    private final String sessionId;

    public SessionNotFoundException(String sessionId) {
        super(String.format("The session could not be found: %s", sessionId));
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return this.sessionId;
    }

}

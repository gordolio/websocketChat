package com.gordonchild.websocket.chat;

public class UserNotFoundException extends RuntimeException {

    private final String userId;
    private final String sessionId;

    public UserNotFoundException(String userId, String sessionId) {
        super(String.format("The user: %s does not exist in the session: %s", userId, sessionId));
        this.userId = userId;
        this.sessionId = sessionId;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getSessionId() {
        return this.sessionId;
    }
}

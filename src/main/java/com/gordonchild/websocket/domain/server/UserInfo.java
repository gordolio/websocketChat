package com.gordonchild.websocket.domain.server;

import org.apache.commons.lang3.RandomStringUtils;

public class UserInfo {

    private final String username;
    private final String sessionId;
    private final String publicId;
    private boolean socketCreated;

    public UserInfo(String username) {
        this.username = username;
        this.sessionId = RandomStringUtils.randomAlphanumeric(8);
        this.publicId = RandomStringUtils.randomAlphanumeric(8);
        this.socketCreated = false;
    }

    public String getUsername() {
        return this.username;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public String getPublicId() {
        return this.publicId;
    }

    public boolean isSocketCreated() {
        return this.socketCreated;
    }

    public void setSocketCreated(boolean socketCreated) {
        this.socketCreated = socketCreated;
    }
}

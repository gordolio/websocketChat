package com.gordonchild.websocket.domain;

import org.apache.commons.lang3.RandomStringUtils;

public class User {

    private final String username;
    private final String userId;

    public User(String username) {
        this.username = username;
        this.userId = RandomStringUtils.randomAlphanumeric(8);
    }

    public String getUsername() {
        return this.username;
    }

    public String getUserId() {
        return this.userId;
    }

}

package com.gordonchild.websocket.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Session {

    private final String sessionId;
    private final Map<String,User> users;


    public Session(String sessionId) {
        this.users = new ConcurrentHashMap<>();
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public Collection<User> getUsers() {
        return Collections.unmodifiableCollection(users.values());
    }

    public User getUser(String userId) {
        return this.users.get(userId);
    }

    public void addUser(User user) {
        this.users.put(user.getUserId(), user);
    }

}

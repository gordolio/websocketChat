package com.gordonchild.websocket.domain.server;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.gordonchild.websocket.domain.session.Session;

public class RoomInfo<T extends Session> {

    private final String roomName;
    private final Map<String,T> users;

    public RoomInfo(String roomName) {
        this.users = new ConcurrentHashMap<>();
        this.roomName = roomName;
    }


    public String getRoomName() {
        return this.roomName;
    }

    public Collection<T> getUsers() {
        return Collections.unmodifiableCollection(this.users.values());
    }

    public T getUser(String userId) {
        return this.users.get(userId);
    }

    public void addUser(T userInfo) {
        this.users.put(userInfo.getSessionId(), userInfo);
    }

    public void removeUser(T session) {
        this.users.remove(session.getSessionId());
    }

}

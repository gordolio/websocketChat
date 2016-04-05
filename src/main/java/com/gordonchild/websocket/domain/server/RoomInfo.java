package com.gordonchild.websocket.domain.server;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.gordonchild.websocket.domain.session.ChatSession;

public class RoomInfo {

    private final String roomName;
    private final Map<String,ChatSession> users;


    public RoomInfo(String roomName) {
        this.users = new ConcurrentHashMap<>();
        this.roomName = roomName;
    }

    public String getRoomName() {
        return this.roomName;
    }

    public Collection<ChatSession> getUsers() {
        return Collections.unmodifiableCollection(this.users.values());
    }

    public ChatSession getUser(String userId) {
        return this.users.get(userId);
    }

    public void addUser(ChatSession userInfo) {
        this.users.put(userInfo.getSessionId(), userInfo);
    }

    public void removeUser(ChatSession session) {
        this.users.remove(session.getSessionId());
    }

}

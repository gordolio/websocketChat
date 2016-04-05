package com.gordonchild.websocket.service;

import com.gordonchild.websocket.domain.session.Session;

public interface SessionService {
    Session startSession();
    <T extends Session> T getSession(String sessionId, Class<T> context);
    Session getSession(String sessionId);
    <T extends Session> T getSocketSession(String socketSessionId, Class<T> context);
    Session getSocketSession(String socketSessionId);

    void updateSocketSessionId(String sessionId, String socketSessionId);

    void removeSession(String sessionId);
    void removeSocketSession(String socketSessionId);
}

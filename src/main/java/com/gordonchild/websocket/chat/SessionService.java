package com.gordonchild.websocket.chat;

import com.gordonchild.websocket.domain.Session;

public interface SessionService<RQ, S extends Session> {
    S startSession(RQ request);
    S getSession(String sessionId);
    S getSocketSession(String socketSessionId);

    void updateSocketSessionId(String sessionId, String socketSessionId);

    void removeSession(String sessionId);
    void removeSocketSession(String socketSessionId);
}

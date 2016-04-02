package com.gordonchild.websocket.chat;

import com.gordonchild.websocket.domain.Session;

public interface SessionService<RQ, S extends Session> {
    S startSession(RQ request);
}

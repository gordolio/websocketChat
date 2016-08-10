package com.gordonchild.websocket.chat.domain.session;

public interface Session {

    String getSessionId();
    void setSessionId(String sessionId);
    String getPublicId();
    void setPublicId(String publicId);
    String getSocketSessionId();
    void setSocketSessionId(String socketSessionId);

}

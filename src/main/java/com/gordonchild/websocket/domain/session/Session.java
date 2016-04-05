package com.gordonchild.websocket.domain.session;

public interface Session {

    String getSessionId();
    void setSessionId(String sessionId);
    String getPublicId();
    void setPublicId(String publicId);
    String getSocketSessionId();
    void setSocketSessionId(String socketSessionId);

}

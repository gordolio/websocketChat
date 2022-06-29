package com.gordonchild.websocket.domain.session;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BaseSessionInternal implements Session {

    private String sessionId;
    private String publicId;
    @JsonIgnore
    private String socketSessionId;
    @JsonIgnore
    private Map<String,Object> data = new ConcurrentHashMap<>();

    public String toLoggingString() {
        var sb = new StringBuilder().append("Session: ").append(this.sessionId).append(" PublicId: ").append(this.publicId).append(" SocketSessionId: ").append(this.socketSessionId);
        data.forEach((key, value) -> sb.append(" ").append(key).append("=").append(value));
        return sb.toString();
    }

    @Override
    public String getSessionId() {
        return this.sessionId;
    }

    @Override
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String getPublicId() {
        return this.publicId;
    }

    @Override
    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    @Override
    public String getSocketSessionId() {
        return this.socketSessionId;
    }

    public void setSocketSessionId(String socketSessionId) {
        this.socketSessionId = socketSessionId;
    }

    public final <T> void putSessionData(T data) {
        String className = data.getClass().getSuperclass().getName();
        this.data.put(className, data);
    }

    @SuppressWarnings("unchecked")
    public final <T> T getSessionData(Class<T> dataClazz) {
        String className = dataClazz.getName();
        Object obj = this.data.get(className);
        if(obj != null && dataClazz.isInstance(obj)) {
            return (T)obj;
        } else {
            return null;
        }
    }
}

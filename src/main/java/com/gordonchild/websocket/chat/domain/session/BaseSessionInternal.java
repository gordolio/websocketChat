package com.gordonchild.websocket.chat.domain.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class BaseSessionInternal implements Session {

    private String sessionId;
    private String publicId;
    @JsonIgnore
    private String socketSessionId;
    @JsonIgnore
    private Map<String,Object> data = new ConcurrentHashMap<>();

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

package com.gordonchild.websocket.domain.session;

abstract class BaseSession<T> implements Session {

    private Class<T> clazz;
    private Session session;

    BaseSession(Class<T> clazz, Session session) {
        this.clazz = clazz;
        if(session instanceof BaseSessionInternal) {
            BaseSessionInternal bsi = (BaseSessionInternal)session;
            T sessionData = bsi.getSessionData(clazz);
            this.copyValuesFrom(sessionData);
        } else {
            throw new IllegalArgumentException("Session is not base session");
        }
    }

    @Override
    public String getSessionId() {
        return this.session.getSessionId();
    }

    @Override
    public void setSessionId(String sessionId) {
        this.session.setSessionId(sessionId);
    }

    @Override
    public String getPublicId() {
        return this.session.getPublicId();
    }

    @Override
    public void setPublicId(String publicId) {
        this.session.setPublicId(publicId);
    }

    @Override
    public String getSocketSessionId() {
        return this.session.getSocketSessionId();
    }

    @Override
    public void setSocketSessionId(String socketSessionId) {
        this.session.setSocketSessionId(socketSessionId);
    }

    abstract void copyValuesFrom(T other);

}

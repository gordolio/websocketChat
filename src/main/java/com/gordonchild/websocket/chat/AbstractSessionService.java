package com.gordonchild.websocket.chat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.util.StringUtils;

import com.gordonchild.websocket.domain.Session;

public abstract class AbstractSessionService<RQ, S extends Session> implements SessionService<RQ, S> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractSessionService.class);

    private Class<S> clazz;
    private Map<String,S> sessionMap;
    private Map<String,S> socketSessionMap;

    public AbstractSessionService(Class<S> clazz) {
        this.clazz = clazz;
        this.sessionMap = new ConcurrentHashMap<>();
        this.socketSessionMap = new ConcurrentHashMap<>();
    }

    @Override
    public S getSession(String sessionId) {
        return this.sessionMap.get(sessionId);
    }

    @Override
    public S getSocketSession(String socketSessionId) {
        return this.socketSessionMap.get(socketSessionId);
    }

    @Override
    public S startSession(RQ sessionRequest) {
        S session = null;
        try {
            session = clazz.newInstance();
            session.setSessionId(StringUtils.randomAlphanumeric(10));
            session.setPublicId(StringUtils.randomAlphanumeric(10));
            this.sessionMap.put(session.getSessionId(), session);
            this.populateSession(session, sessionRequest);
        } catch(ReflectiveOperationException ex) {
            LOG.error("Could not create session", ex);
        }
        return session;
    }

    protected abstract void populateSession(S session, RQ sessionRequest);

    @Override
    public void removeSession(String sessionId) {
        S session = this.sessionMap.get(sessionId);
        if(session == null) {
            return;
        }
        if(session.getSocketSessionId() != null) {
            this.socketSessionMap.remove(session.getSocketSessionId());
        }
        this.sessionMap.remove(sessionId);
    }

    @Override
    public void removeSocketSession(String socketSessionId) {
        S session = this.socketSessionMap.get(socketSessionId);
        if(session != null) {
            this.removeSession(session.getSessionId());
        }
    }

    @Override
    public void updateSocketSessionId(String sessionId, String socketSessionId) {
        S session = this.getSession(sessionId);
        if(session == null) {
            return;
        }
        if(session.getSocketSessionId() != null) {
            this.socketSessionMap.remove(session.getSocketSessionId());
        }
        session.setSocketSessionId(socketSessionId);
        this.socketSessionMap.put(socketSessionId, session);
    }
}

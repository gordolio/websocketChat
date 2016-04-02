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

    public AbstractSessionService(Class<S> clazz) {
        this.clazz = clazz;
        this.sessionMap = new ConcurrentHashMap<>();
    }

    public S getSession(String sessionId) {
        return this.sessionMap.get(sessionId);
    }

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

    public void removeSession(String sessionId) {
        this.sessionMap.remove(sessionId);
    }
}

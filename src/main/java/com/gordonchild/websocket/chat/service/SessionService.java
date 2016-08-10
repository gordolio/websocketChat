package com.gordonchild.websocket.chat.service;

import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import com.gordonchild.websocket.chat.domain.session.BaseSessionInternal;
import com.gordonchild.websocket.chat.domain.session.Session;
import com.gordonchild.websocket.chat.exception.SessionNotFoundException;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;

@Service("sessionService")
public class SessionService {

    private Map<String,BaseSessionInternal> sessionMap;
    private Map<String,BaseSessionInternal> socketSessionMap;

    public SessionService() {
        this.sessionMap = new ConcurrentHashMap<>();
        this.socketSessionMap = new ConcurrentHashMap<>();
    }

    public Session startSession() {
        BaseSessionInternal baseSession = new BaseSessionInternal();
        baseSession.setSessionId(StringUtils.randomAlphanumeric(10));
        baseSession.setPublicId(StringUtils.randomAlphanumeric(10));
        this.sessionMap.put(baseSession.getSessionId(), baseSession);
        return baseSession;
    }

    public <T extends Session> T getSession(String sessionId, Class<T> context) {
        BaseSessionInternal session = this.sessionMap.get(sessionId);
        if(session == null) {
            throw new SessionNotFoundException("The session: " + sessionId + " could not be found");
        }
        T contextData = session.getSessionData(context);
        if(contextData == null) {
            contextData = this.makeSessionContext(session, context);
            session.putSessionData(contextData);
        }
        return contextData;
    }

    public Session getSession(String sessionId) {
        return this.sessionMap.get(sessionId);
    }

    public <T extends Session> T getSocketSession(String socketSessionId, Class<T> context) {
        Session session = this.socketSessionMap.get(socketSessionId);
        if(session == null) {
            throw new SessionNotFoundException("Socket session: " + socketSessionId + " not found");
        }
        return this.getSession(session.getSessionId(), context);
    }

    public Session getSocketSession(String socketSessionId) {
        return this.socketSessionMap.get(socketSessionId);
    }

    public void removeSession(String sessionId) {
        BaseSessionInternal baseSession = this.sessionMap.get(sessionId);
        if(baseSession == null) {
            return;
        }
        if(baseSession.getSocketSessionId() != null) {
            this.socketSessionMap.remove(baseSession.getSocketSessionId());
        }
        this.sessionMap.remove(sessionId);
    }

    public void removeSocketSession(String socketSessionId) {
        BaseSessionInternal baseSession = this.socketSessionMap.get(socketSessionId);
        if(baseSession != null) {
            this.removeSession(baseSession.getSessionId());
        }
    }

    public void updateSocketSessionId(String sessionId, String socketSessionId) {
        BaseSessionInternal baseSession = this.sessionMap.get(sessionId);
        if(baseSession == null) {
            return;
        }
        if(baseSession.getSocketSessionId() != null) {
            this.socketSessionMap.remove(baseSession.getSocketSessionId());
        }
        baseSession.setSocketSessionId(socketSessionId);
        this.socketSessionMap.put(socketSessionId, baseSession);
    }

    @SuppressWarnings("unchecked")
    private <T extends Session> T makeSessionContext(final Session existingSession, Class<T> clazz) {
        ProxyFactory factory = new ProxyFactory();
        factory.setSuperclass(clazz);
        factory.setFilter(method-> Modifier.isAbstract(method.getModifiers()));
        MethodHandler handler = (self, thisMethod, proceed, args)
                    -> thisMethod.invoke(existingSession, args);
        try {
            return (T) factory.create(new Class<?>[]{}, new Object[]{}, handler);
        } catch(ReflectiveOperationException ex) {
            throw new SessionProxyException("Error creating proxy", ex);
        }
    }

    private class SessionProxyException extends RuntimeException {

        private SessionProxyException(String message, Throwable cause) {
            super(message, cause);
        }

    }
}

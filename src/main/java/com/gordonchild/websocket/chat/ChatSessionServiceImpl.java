package com.gordonchild.websocket.chat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.gordonchild.websocket.domain.ChatMessage;
import com.gordonchild.websocket.domain.JoinNotify;
import com.gordonchild.websocket.domain.LeaveNotify;
import com.gordonchild.websocket.domain.SendMessageRequest;
import com.gordonchild.websocket.domain.Session;
import com.gordonchild.websocket.domain.User;
import com.gordonchild.websocket.domain.UserJoinRequest;
import com.gordonchild.websocket.domain.UserLeaveRequest;

@Service
public class ChatSessionServiceImpl implements ChatSessionService {

    private Map<String,Session> sessions = new ConcurrentHashMap<>();

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void sendMessage(SendMessageRequest request) {
        Session session = this.getSession(request.getSessionId());
        User fromUser = getUser(session, request.getUserId());
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSessionId(session.getSessionId());
        chatMessage.setUsername(fromUser.getUsername());
        chatMessage.setMessage(request.getMessage());

        this.simpMessagingTemplate.convertAndSend("/topic/chats-" + session.getSessionId(), chatMessage);
    }

    @Override
    public void userJoin(UserJoinRequest request) {
        Session session = this.getSession(request.getSessionId());
        User joinUser = getUser(session, request.getUserId());

        JoinNotify joinNotify = new JoinNotify();
        joinNotify.setUsername(joinUser.getUsername());
        joinNotify.setSessionId(session.getSessionId());

        this.simpMessagingTemplate.convertAndSend("/topic/chats-" + session.getSessionId(), joinNotify);
    }

    @Override
    public void userLeave(UserLeaveRequest request) {
        Session session = this.getSession(request.getSessionId());
        User user = getUser(session, request.getUserId());

        LeaveNotify leaveNotify = new LeaveNotify();
        leaveNotify.setUsername(user.getUsername());
        leaveNotify.setSessionId(session.getSessionId());

        this.simpMessagingTemplate.convertAndSend("/topic/chats-" + session.getSessionId(), leaveNotify);
    }

    private Session getSession(String sessionId) {
        Session session = this.sessions.get(sessionId);

        if(session == null) {
            session = new Session(sessionId);
            this.sessions.put(sessionId, session);
        }
        return session;
    }

    private static User getUser(Session session, String userId) {
        User user = session.getUser(userId);
        if(user == null) {
            user = new User(userId);
            session.addUser(user);
        }
        return user;
    }

}

package com.gordonchild.websocket.service;

import static com.gordonchild.websocket.domain.request.UserVoteRequest.VoteType.CLEAR;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.gordonchild.websocket.domain.event.MessageEvent;
import com.gordonchild.websocket.domain.event.JoinEvent;
import com.gordonchild.websocket.domain.event.LeaveEvent;
import com.gordonchild.websocket.domain.event.RevealVotesEvent;
import com.gordonchild.websocket.domain.event.TypingEvent;
import com.gordonchild.websocket.domain.event.UserData;
import com.gordonchild.websocket.domain.event.UserVoteData;
import com.gordonchild.websocket.domain.event.VoteEvent;
import com.gordonchild.websocket.domain.request.JoinRoomRequest;
import com.gordonchild.websocket.domain.request.LeaveRoomRequest;
import com.gordonchild.websocket.domain.request.RevealVoteRequest;
import com.gordonchild.websocket.domain.request.RoomRequest;
import com.gordonchild.websocket.domain.request.SendMessageRequest;
import com.gordonchild.websocket.domain.request.UserTypingRequest;
import com.gordonchild.websocket.domain.request.UserVoteRequest;
import com.gordonchild.websocket.domain.server.RoomInfo;
import com.gordonchild.websocket.domain.session.ChatSession;

@Service("chatRoomService")
public class ChatRoomServiceImpl implements ChatRoomService {

    private static final Logger LOG = LoggerFactory.getLogger(ChatRoomService.class);

    private static final String CHAT_TOPIC = "/topic/chats-";

    private Map<String,RoomInfo> rooms = new ConcurrentHashMap<>();

    @Autowired
    private SessionService sessionService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void sendMessage(SendMessageRequest request) {
        ChatSession session = this.sessionService.getSession(request.getSessionId(), ChatSession.class);
        MessageEvent chatMessage = this.createChatEvent(session, MessageEvent.class);
        chatMessage.setMessage(request.getMessage());
        this.simpMessagingTemplate.convertAndSend(CHAT_TOPIC + request.getRoomName(), chatMessage);
    }

    @Override
    public void userJoin(JoinRoomRequest request) {
        ChatSession chatSession = this.sessionService.getSession(request.getSessionId(), ChatSession.class);
        chatSession.setUsername(request.getUsername());

        RoomInfo roomInfo = this.getRoom(request);
        roomInfo.addUser(chatSession);

        chatSession.setRoomName(request.getRoomName());
        JoinEvent joinEvent = this.createChatEvent(chatSession, JoinEvent.class);
        List<UserData> users = new ArrayList<>();
        roomInfo.getUsers().forEach(userSession->{
            UserData userData = new UserData();
            userData.setUsername(userSession.getUsername());
            userData.setPublicId(userSession.getPublicId());
            users.add(userData);
        });
        joinEvent.setAllUsers(users);
        this.simpMessagingTemplate.convertAndSend(CHAT_TOPIC + request.getRoomName(), joinEvent);
    }

    @Override
    public void userLeave(LeaveRoomRequest request) {
        ChatSession session = this.sessionService.getSession(request.getSessionId(), ChatSession.class);
        RoomInfo roomInfo = this.getRoom(request);
        roomInfo.removeUser(session);
        if(roomInfo.getUsers().isEmpty()) {
            this.rooms.remove(roomInfo.getRoomName());
        }
        this.sessionService.removeSession(session.getSessionId());
        LeaveEvent leaveEvent = this.createChatEvent(session, LeaveEvent.class);
        this.simpMessagingTemplate.convertAndSend(CHAT_TOPIC + request.getRoomName(), leaveEvent);
    }

    @Override
    public void userTyping(UserTypingRequest request) {
        ChatSession session = this.sessionService.getSession(request.getSessionId(), ChatSession.class);
        TypingEvent typingEvent = this.createChatEvent(session, TypingEvent.class);
        this.simpMessagingTemplate.convertAndSend(CHAT_TOPIC + request.getRoomName(), typingEvent);
    }

    @Override
    public void userVote(UserVoteRequest request) {
        ChatSession session = this.sessionService.getSession(request.getSessionId(), ChatSession.class);
        VoteEvent voteEvent = this.createChatEvent(session, VoteEvent.class);

        if(CLEAR.equals(request.getVote())) {
            session.setCurrentVote(null);
            voteEvent.setDidClear(true);
        } else {
            session.setCurrentVote(request.getVote());
        }
        this.simpMessagingTemplate.convertAndSend(CHAT_TOPIC + request.getRoomName(), voteEvent);
    }

    @Override
    public void revealVotes(RevealVoteRequest request) {
        ChatSession session = this.sessionService.getSession(request.getSessionId(), ChatSession.class);
        RevealVotesEvent event = this.createChatEvent(session, RevealVotesEvent.class);
        RoomInfo roomInfo = this.getRoom(request);

        List<UserVoteData> votes = new ArrayList<>();
        roomInfo.getUsers().forEach(userSession->{
            UserVoteData vote = new UserVoteData();
            vote.setUsername(userSession.getUsername());
            vote.setPublicId(userSession.getPublicId());
            vote.setVote(userSession.getCurrentVote());
            votes.add(vote);
        });
        event.setVotes(votes);

        this.simpMessagingTemplate.convertAndSend(CHAT_TOPIC + request.getRoomName(), event);
    }

    @Override
    public ChatSession getSocketSession(String socketSessionId) {
        return this.sessionService.getSocketSession(socketSessionId, ChatSession.class);
    }

    private RoomInfo getRoom(RoomRequest room) {
        return this.getRoom(room.getRoomName());
    }
    private RoomInfo getRoom(String roomName) {
        RoomInfo roomInfo = this.rooms.get(roomName);

        if(roomInfo == null) {
            roomInfo = new RoomInfo(roomName);
            this.rooms.put(roomName, roomInfo);
        }
        return roomInfo;
    }

    private <T extends UserData> T createChatEvent(ChatSession chatSession, Class<T> clazz) {
        T obj = null;
        try {
            obj = clazz.newInstance();
            obj.setUsername(chatSession.getUsername());
            obj.setPublicId(chatSession.getPublicId());
        } catch(ReflectiveOperationException ex) {
            LOG.error("error instantiating chat event", ex);
        }
        return obj;
    }

}

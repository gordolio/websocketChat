package com.gordonchild.websocket.chat.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.gordonchild.websocket.chat.domain.event.ClearVotesEvent;
import com.gordonchild.websocket.chat.domain.event.JoinEvent;
import com.gordonchild.websocket.chat.domain.event.LeaveEvent;
import com.gordonchild.websocket.chat.domain.event.MessageEvent;
import com.gordonchild.websocket.chat.domain.event.RevealVotesEvent;
import com.gordonchild.websocket.chat.domain.event.TypingEvent;
import com.gordonchild.websocket.chat.domain.event.UserData;
import com.gordonchild.websocket.chat.domain.event.UserVoteData;
import com.gordonchild.websocket.chat.domain.event.VoteEvent;
import com.gordonchild.websocket.chat.domain.request.ClearVotingRequest;
import com.gordonchild.websocket.chat.domain.request.JoinRoomRequest;
import com.gordonchild.websocket.chat.domain.request.LeaveRoomRequest;
import com.gordonchild.websocket.chat.domain.request.RevealVoteRequest;
import com.gordonchild.websocket.chat.domain.request.RoomRequest;
import com.gordonchild.websocket.chat.domain.request.SendMessageRequest;
import com.gordonchild.websocket.chat.domain.request.UserAwayRequest;
import com.gordonchild.websocket.chat.domain.request.UserTypingRequest;
import com.gordonchild.websocket.chat.domain.request.UserVoteRequest;
import com.gordonchild.websocket.chat.domain.server.ChatRoomInfo;
import com.gordonchild.websocket.chat.domain.session.ChatRoomSession;

@Service("chatRoomService")
public class ChatRoomServiceImpl implements ChatRoomService {

    private static final Logger LOG = LoggerFactory.getLogger(ChatRoomService.class);

    private static final String CHAT_TOPIC = "/topic/chats-";

    private Map<String,ChatRoomInfo> rooms = new ConcurrentHashMap<>();

    @Autowired
    private SessionService sessionService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void sendMessage(SendMessageRequest request) {
        ChatRoomSession session = this.sessionService.getSession(request.getSessionId(), ChatRoomSession.class);
        MessageEvent chatMessage = this.createChatEvent(session, MessageEvent.class);
        chatMessage.setMessage(request.getMessage());
        this.simpMessagingTemplate.convertAndSend(CHAT_TOPIC + request.getRoomName(), chatMessage);
    }

    @Override
    public void userJoin(JoinRoomRequest request) {
        ChatRoomSession chatRoomSession = this.sessionService.getSession(request.getSessionId(), ChatRoomSession.class);
        chatRoomSession.setUsername(request.getUsername());

        ChatRoomInfo roomInfo = this.getRoom(request);
        roomInfo.addUser(chatRoomSession);

        chatRoomSession.setRoomName(request.getRoomName());
        JoinEvent joinEvent = this.createChatEvent(chatRoomSession, JoinEvent.class);
        List<UserData> users = new ArrayList<>();
        roomInfo.getUsers().forEach(userSession->{
            UserData userData = new UserData();
            userData.setUsername(userSession.getUsername());
            userData.setPublicId(userSession.getPublicId());
            if(userSession.isVoteHidden() && !UserVoteRequest.VoteType.UNVOTE.equals(userSession.getCurrentVote())) {
                userData.setVote(UserVoteRequest.VoteType.HIDDEN);
            } else {
                userData.setVote(userSession.getCurrentVote());
            }
            users.add(userData);
        });
        joinEvent.setAllUsers(users);
        this.simpMessagingTemplate.convertAndSend(CHAT_TOPIC + request.getRoomName(), joinEvent);
    }

    @Override
    public void userLeave(LeaveRoomRequest request) {
        ChatRoomSession session = this.sessionService.getSession(request.getSessionId(), ChatRoomSession.class);
        ChatRoomInfo roomInfo = this.getRoom(request);
        roomInfo.removeUser(session);
        if(roomInfo.getUsers().isEmpty()) {
            this.rooms.remove(roomInfo.getRoomName());
        }
        this.sessionService.removeSession(session.getSessionId());
        LeaveEvent leaveEvent = this.createChatEvent(session, LeaveEvent.class);
        this.simpMessagingTemplate.convertAndSend(CHAT_TOPIC + request.getRoomName(), leaveEvent);
    }

    @Override
    public void userAway(UserAwayRequest userAwayRequest) {

    }

    @Override
    public void userTyping(UserTypingRequest request) {
        ChatRoomSession session = this.sessionService.getSession(request.getSessionId(), ChatRoomSession.class);
        TypingEvent typingEvent = this.createChatEvent(session, TypingEvent.class);
        this.simpMessagingTemplate.convertAndSend(CHAT_TOPIC + request.getRoomName(), typingEvent);
    }

    @Override
    public void userVote(UserVoteRequest request) {
        ChatRoomSession session = this.sessionService.getSession(request.getSessionId(), ChatRoomSession.class);
        VoteEvent voteEvent = this.createChatEvent(session, VoteEvent.class);

        session.setCurrentVote(request.getVote());
        if(session.isVoteHidden() && !UserVoteRequest.VoteType.UNVOTE.equals(session.getCurrentVote())) {
            voteEvent.setVote(UserVoteRequest.VoteType.HIDDEN);
        } else {
            voteEvent.setVote(request.getVote());
        }
        this.simpMessagingTemplate.convertAndSend(CHAT_TOPIC + request.getRoomName(), voteEvent);
    }

    @Override
    public void revealVotes(RevealVoteRequest request) {
        ChatRoomSession session = this.sessionService.getSession(request.getSessionId(), ChatRoomSession.class);
        RevealVotesEvent event = this.createChatEvent(session, RevealVotesEvent.class);
        ChatRoomInfo roomInfo = this.getRoom(request);

        List<UserVoteData> votes = new ArrayList<>();
        roomInfo.getUsers().forEach(userSession->{
            UserVoteData vote = new UserVoteData();
            vote.setUsername(userSession.getUsername());
            vote.setPublicId(userSession.getPublicId());
            vote.setVote(userSession.getCurrentVote());
            userSession.setVoteHidden(false);
            votes.add(vote);
        });
        event.setVotes(votes);

        this.simpMessagingTemplate.convertAndSend(CHAT_TOPIC + request.getRoomName(), event);
    }

    @Override
    public void clearVoting(ClearVotingRequest request) {
        ChatRoomSession session = this.sessionService.getSession(request.getSessionId(), ChatRoomSession.class);
        ClearVotesEvent event = this.createChatEvent(session, ClearVotesEvent.class);
        ChatRoomInfo roomInfo = this.getRoom(request);
        roomInfo.getUsers().forEach(userSession->{
            userSession.setVoteHidden(true);
            userSession.setCurrentVote(UserVoteRequest.VoteType.UNVOTE);
        });

        this.simpMessagingTemplate.convertAndSend(CHAT_TOPIC + request.getRoomName(), event);
    }

    @Override
    public ChatRoomSession getSocketSession(String socketSessionId) {
        return this.sessionService.getSocketSession(socketSessionId, ChatRoomSession.class);
    }

    private ChatRoomInfo getRoom(RoomRequest room) {
        return this.getRoom(room.getRoomName());
    }
    private ChatRoomInfo getRoom(String roomName) {
        ChatRoomInfo roomInfo = this.rooms.get(roomName);

        if(roomInfo == null) {
            roomInfo = new ChatRoomInfo(roomName);
            this.rooms.put(roomName, roomInfo);
        }
        return roomInfo;
    }

    private <T extends UserData> T createChatEvent(ChatRoomSession chatRoomSession, Class<T> clazz) {
        T obj = null;
        try {
            obj = clazz.newInstance();
            obj.setUsername(chatRoomSession.getUsername());
            obj.setPublicId(chatRoomSession.getPublicId());
        } catch(ReflectiveOperationException ex) {
            LOG.error("error instantiating chat event", ex);
        }
        return obj;
    }

}

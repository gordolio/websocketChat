package com.gordonchild.websocket.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.gordonchild.websocket.chat.ChatRoomService;
import com.gordonchild.websocket.domain.ChatSession;
import com.gordonchild.websocket.domain.request.LeaveRoomRequest;

@Component
public class SessionDisconnectedEventListener implements ApplicationListener<SessionDisconnectEvent> {

    @Autowired
    private ChatRoomService chatRoomService;

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        ChatSession chatSession = this.chatRoomService.getSocketSession(event.getSessionId());
        LeaveRoomRequest leaveRoomRequest = new LeaveRoomRequest();
        leaveRoomRequest.setSessionId(chatSession.getSessionId());
        leaveRoomRequest.setRoomName(chatSession.getRoomName());
        this.chatRoomService.userLeave(leaveRoomRequest);
    }
}

package com.gordonchild.websocket.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.gordonchild.websocket.domain.request.LeaveRoomRequest;
import com.gordonchild.websocket.domain.session.ChatSession;
import com.gordonchild.websocket.service.ChatRoomService;

@Component
public class SessionDisconnectedEventListener implements ApplicationListener<SessionDisconnectEvent> {

    @Autowired
    private ChatRoomService chatRoomService;

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        ChatSession session = this.chatRoomService.getSocketSession(event.getSessionId());
        LeaveRoomRequest leaveRoomRequest = new LeaveRoomRequest();
        leaveRoomRequest.setSessionId(session.getSessionId());
        leaveRoomRequest.setRoomName(session.getRoomName());
        this.chatRoomService.userLeave(leaveRoomRequest);
    }
}

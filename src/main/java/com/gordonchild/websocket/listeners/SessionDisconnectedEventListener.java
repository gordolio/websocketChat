package com.gordonchild.websocket.listeners;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.gordonchild.websocket.domain.request.LeaveRoomRequest;
import com.gordonchild.websocket.domain.session.ChatSession;
import com.gordonchild.websocket.service.ChatRoomService;

@Component
public class SessionDisconnectedEventListener implements ApplicationListener<SessionDisconnectEvent> {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Autowired
    private ChatRoomService chatRoomService;

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        final ChatSession session = this.chatRoomService.getSocketSession(event.getSessionId());
        this.chatRoomService.userLeave(new LeaveRoomRequest(session));
        /*
        this.chatRoomService.userAway(new UserAwayRequest(session));
        this.scheduler.schedule(()->this.chatRoomService.userLeave(new LeaveRoomRequest(session)),
                5, TimeUnit.MINUTES);
               */
    }
}

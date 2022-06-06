package com.gordonchild.websocket.listeners;

import com.gordonchild.websocket.domain.request.LeaveRoomRequest;
import com.gordonchild.websocket.domain.session.ChatRoomSession;
import com.gordonchild.websocket.service.ChatRoomService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Component
public class SessionDisconnectedEventListener implements ApplicationListener<SessionDisconnectEvent> {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private final ChatRoomService chatRoomService;

    public SessionDisconnectedEventListener(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        final ChatRoomSession session = this.chatRoomService.getSocketSession(event.getSessionId());
        this.chatRoomService.userLeave(new LeaveRoomRequest(session));
        /*
        this.chatRoomService.userAway(new UserAwayRequest(session));
        this.scheduler.schedule(()->this.chatRoomService.userLeave(new LeaveRoomRequest(session)),
                5, TimeUnit.MINUTES);
               */
    }
}

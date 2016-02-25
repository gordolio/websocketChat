package com.gordonchild.websocket.chat;

import com.gordonchild.websocket.domain.SendMessageRequest;
import com.gordonchild.websocket.domain.UserJoinRequest;
import com.gordonchild.websocket.domain.UserLeaveRequest;

public interface ChatSessionService {

    void sendMessage(SendMessageRequest request);
    void userJoin(UserJoinRequest userId);
    void userLeave(UserLeaveRequest user);

}

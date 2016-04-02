package com.gordonchild.websocket.chat;

import com.gordonchild.websocket.domain.ChatSession;
import com.gordonchild.websocket.domain.StartChatRequest;
import com.gordonchild.websocket.domain.request.SendMessageRequest;
import com.gordonchild.websocket.domain.request.JoinRoomRequest;
import com.gordonchild.websocket.domain.request.LeaveRoomRequest;

public interface ChatRoomService extends SessionService<StartChatRequest,ChatSession> {

    void sendMessage(SendMessageRequest request);
    void userJoin(JoinRoomRequest userId);
    void userLeave(LeaveRoomRequest user);

}

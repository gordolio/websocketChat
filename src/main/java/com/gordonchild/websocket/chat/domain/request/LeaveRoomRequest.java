package com.gordonchild.websocket.chat.domain.request;

import com.gordonchild.websocket.chat.domain.session.ChatRoomSession;

public class LeaveRoomRequest extends RoomRequest {

    public LeaveRoomRequest(ChatRoomSession chatRoomSession) {
        super(chatRoomSession);
    }

}

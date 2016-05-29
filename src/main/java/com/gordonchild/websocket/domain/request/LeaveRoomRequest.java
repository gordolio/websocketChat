package com.gordonchild.websocket.domain.request;

import com.gordonchild.websocket.domain.session.ChatRoomSession;

public class LeaveRoomRequest extends RoomRequest {

    public LeaveRoomRequest(ChatRoomSession chatRoomSession) {
        super(chatRoomSession);
    }

}

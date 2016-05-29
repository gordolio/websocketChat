package com.gordonchild.websocket.domain.request;

import com.gordonchild.websocket.domain.session.ChatRoomSession;

public class UserAwayRequest extends RoomRequest {

    public UserAwayRequest(ChatRoomSession chatRoomSession) {
        super(chatRoomSession);
    }

}

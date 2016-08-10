package com.gordonchild.websocket.chat.domain.request;

import com.gordonchild.websocket.chat.domain.session.ChatRoomSession;

public class UserAwayRequest extends RoomRequest {

    public UserAwayRequest(ChatRoomSession chatRoomSession) {
        super(chatRoomSession);
    }

}

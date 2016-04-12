package com.gordonchild.websocket.domain.request;

import com.gordonchild.websocket.domain.session.ChatSession;

public class UserAwayRequest extends RoomRequest {

    public UserAwayRequest(ChatSession chatSession) {
        super(chatSession);
    }

}

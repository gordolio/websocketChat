package com.gordonchild.websocket.domain.server;

import com.gordonchild.websocket.domain.session.ChatRoomSession;

public class ChatRoomInfo extends RoomInfo<ChatRoomSession> {

    public ChatRoomInfo(String roomName) {
        super(roomName);
    }

}

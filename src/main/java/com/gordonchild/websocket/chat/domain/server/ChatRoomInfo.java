package com.gordonchild.websocket.chat.domain.server;

import com.gordonchild.websocket.chat.domain.session.ChatRoomSession;

public class ChatRoomInfo extends RoomInfo<ChatRoomSession> {

    public ChatRoomInfo(String roomName) {
        super(roomName);
    }

}

package com.gordonchild.websocket.chat.domain.event;

import com.gordonchild.websocket.chat.domain.server.ChatRoomInfo;

public class PlayStateChangeEvent extends UserData {

    private ChatRoomInfo.PlayState playState;

    public ChatRoomInfo.PlayState getPlayState() {
        return this.playState;
    }

    public void setPlayState(ChatRoomInfo.PlayState playState) {
        this.playState = playState;
    }
}

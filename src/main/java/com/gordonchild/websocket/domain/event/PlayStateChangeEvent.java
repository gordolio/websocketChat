package com.gordonchild.websocket.domain.event;

import com.gordonchild.websocket.domain.server.ChatRoomInfo;

public class PlayStateChangeEvent extends UserData {

    private ChatRoomInfo.PlayState playState;

    public ChatRoomInfo.PlayState getPlayState() {
        return this.playState;
    }

    public void setPlayState(ChatRoomInfo.PlayState playState) {
        this.playState = playState;
    }
}

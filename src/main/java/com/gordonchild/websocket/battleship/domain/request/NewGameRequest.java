package com.gordonchild.websocket.battleship.domain.request;

public class NewGameRequest {

    private String playerName;

    public String getPlayerName() {
        return this.playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}

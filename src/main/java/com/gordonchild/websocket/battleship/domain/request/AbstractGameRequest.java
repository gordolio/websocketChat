package com.gordonchild.websocket.battleship.domain.request;

public abstract class AbstractGameRequest {

    private String gameId;

    public String getGameId() {
        return this.gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}

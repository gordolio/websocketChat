package com.gordonchild.websocket.battleship.domain.request;

public class JoinGameRequest extends NewGameRequest {

    private String gameId;

    public String getGameId() {
        return this.gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}

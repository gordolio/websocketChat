package com.gordonchild.websocket.battleship.exception;

public class GameNotFoundException extends RuntimeException {

    private String gameId;

    public GameNotFoundException(String gameId) {
        this.gameId = gameId;
    }

    public String getGameId() {
        return this.gameId;
    }
}

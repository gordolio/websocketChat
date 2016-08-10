package com.gordonchild.websocket.battleship.domain.request;

public class StartGameRequest extends GameRequest {

    private String opponentPublicId;

    public String getOpponentPublicId() {
        return this.opponentPublicId;
    }

    public void setOpponentPublicId(String opponentPublicId) {
        this.opponentPublicId = opponentPublicId;
    }
}

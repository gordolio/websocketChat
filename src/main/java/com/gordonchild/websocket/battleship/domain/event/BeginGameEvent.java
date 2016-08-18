package com.gordonchild.websocket.battleship.domain.event;

public class BeginGameEvent extends AbstractGameEvent {

    private String playerOneName;
    private String playerTwoName;

    public String getPlayerOneName() {
        return this.playerOneName;
    }

    public void setPlayerOneName(String playerOneName) {
        this.playerOneName = playerOneName;
    }

    public String getPlayerTwoName() {
        return this.playerTwoName;
    }

    public void setPlayerTwoName(String playerTwoName) {
        this.playerTwoName = playerTwoName;
    }
}

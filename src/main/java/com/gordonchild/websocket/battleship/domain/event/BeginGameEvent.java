package com.gordonchild.websocket.battleship.domain.event;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gordonchild.websocket.battleship.domain.handler.Ship;
import com.gordonchild.websocket.battleship.utils.DetailedShipSerializer;

public class BeginGameEvent extends AbstractGameEvent {

    private String playerOneName;
    private String playerTwoName;
    @JsonSerialize(contentUsing=DetailedShipSerializer.class)
    private List<Ship> ships;

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

    public List<Ship> getShips() {
        return this.ships;
    }

    public void setShips(List<Ship> ships) {
        this.ships = ships;
    }
}

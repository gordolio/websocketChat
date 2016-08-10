package com.gordonchild.websocket.battleship.domain;

public class FireResult {

    public enum ResultStatus {
        MISS,HIT,SUNK,GAME_OVER,INVALID_POSITION
    }

    private ResultStatus resultStatus;
    private Position position;
    private Ship ship;

    public Position getPosition() {
        return this.position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public ResultStatus getResultStatus() {
        return this.resultStatus;
    }

    public void setResultStatus(ResultStatus resultStatus) {
        this.resultStatus = resultStatus;
    }

    public Ship getShip() {
        return this.ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }
}

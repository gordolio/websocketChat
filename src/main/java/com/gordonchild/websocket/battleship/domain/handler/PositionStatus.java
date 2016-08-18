package com.gordonchild.websocket.battleship.domain.handler;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gordonchild.websocket.battleship.utils.PositionHelper;

public class PositionStatus {

    public enum Status {
        EMPTY, SHIP, HIT, MISS
    }

    private Status status;
    @JsonIgnore
    private int position;

    public PositionStatus(){}
    public PositionStatus(int position, Status status) {
        this.position = position;
        this.status = status;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getX() {
        return PositionHelper.getX(this.position);
    }

    public int getY() {
        return PositionHelper.getY(this.position);
    }

    public void setX(int x) {
        this.position = PositionHelper.positionWithX(this.position, x);
    }

    public void setY(int y) {
        this.position = PositionHelper.positionWithY(this.position, y);
    }
}

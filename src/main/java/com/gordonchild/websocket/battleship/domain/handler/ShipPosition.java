package com.gordonchild.websocket.battleship.domain.handler;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gordonchild.websocket.battleship.utils.PositionHelper;

public class ShipPosition {

    public enum Direction {
        NORTH(-10),EAST(1),SOUTH(10),WEST(-1);

        int translate;

        Direction(int translate) {
            this.translate = translate;
        }

        public int getTranslate() {
            return this.translate;
        }
    }

    private Ship ship;
    @JsonIgnore
    private int startPosition;
    private Direction direction;
    private Boolean sunk;
    private Boolean[] hits;
    private Integer numberOfHits;

    public Ship getShip() {
        return this.ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Boolean getSunk() {
        return this.sunk;
    }

    public void setSunk(Boolean sunk) {
        this.sunk = sunk;
    }

    public Boolean[] getHits() {
        return this.hits;
    }

    public void setHits(Boolean[] hits) {
        this.hits = hits;
    }

    public int getStartPosition() {
        return this.startPosition;
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    public int getNumberOfHits() {
        return this.numberOfHits;
    }

    public void setNumberOfHits(int numberOfHits) {
        this.numberOfHits = numberOfHits;
    }

    public int getX() {
        return PositionHelper.getX(this.startPosition);
    }

    public int getY() {
        return PositionHelper.getY(this.startPosition);
    }

    public void setX(int x) {
        this.startPosition = PositionHelper.positionWithX(this.startPosition,x);
    }

    public void setY(int y) {
        this.startPosition = PositionHelper.positionWithY(this.startPosition,y);
    }
}

package com.gordonchild.websocket.battleship.domain;

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
    private int position;
    private Direction direction;
    private Boolean sunk;
    private Boolean[] hits;
    private int numberOfHits;

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

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getNumberOfHits() {
        return this.numberOfHits;
    }

    public void setNumberOfHits(int numberOfHits) {
        this.numberOfHits = numberOfHits;
    }
}

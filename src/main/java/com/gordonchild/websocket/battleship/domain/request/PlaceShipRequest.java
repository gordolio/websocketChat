package com.gordonchild.websocket.battleship.domain.request;

import com.gordonchild.websocket.battleship.domain.event.AbstractGameEvent;
import com.gordonchild.websocket.battleship.domain.handler.Ship;
import com.gordonchild.websocket.battleship.domain.handler.ShipPosition;

public class PlaceShipRequest extends AbstractGameEvent {

    private int x;
    private int y;
    private Ship ship;
    private ShipPosition.Direction direction;

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Ship getShip() {
        return this.ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public ShipPosition.Direction getDirection() {
        return this.direction;
    }

    public void setDirection(ShipPosition.Direction direction) {
        this.direction = direction;
    }
}

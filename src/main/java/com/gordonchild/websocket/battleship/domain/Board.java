package com.gordonchild.websocket.battleship.domain;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private List<ShipPosition> shipPositions = new ArrayList<>();
    private PositionStatus[] positions = new PositionStatus[100];
    private int sunkShips = 0;

    public List<ShipPosition> getShipPositions() {
        return this.shipPositions;
    }

    public void setShipPositions(List<ShipPosition> shipPositions) {
        this.shipPositions = shipPositions;
    }

    public PositionStatus[] getPositions() {
        return this.positions;
    }

    public void setPositions(PositionStatus[] positions) {
        this.positions = positions;
    }

    public int getSunkShips() {
        return this.sunkShips;
    }

    public void setSunkShips(int sunkShips) {
        this.sunkShips = sunkShips;
    }
}

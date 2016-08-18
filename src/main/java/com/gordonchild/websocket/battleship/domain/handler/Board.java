package com.gordonchild.websocket.battleship.domain.handler;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private List<ShipPosition> shipPositions;
    private PositionStatus[] positions;
    private int sunkShips;

    public Board() {
        this.positions = new PositionStatus[100];
        for(int i=0;i<100;i++) {
            this.positions[i] = new PositionStatus(i, PositionStatus.Status.EMPTY);
        }
        this.shipPositions = new ArrayList<>();
        this.sunkShips = 0;
    }

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

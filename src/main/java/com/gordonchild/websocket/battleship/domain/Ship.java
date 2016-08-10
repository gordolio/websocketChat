package com.gordonchild.websocket.battleship.domain;

public enum Ship {
    AIRCRAFT_CARRIER(5, "Aircraft Carrier"),
    BATTLESHIP(4, "Battleship"),
    SUBMARINE(3, "Submarine"),
    DESTROYER(3, "Destroyer"),
    PATROL_BOAT(2, "Patrol Boat");

    private int size;
    private String writtenName;

    Ship(int size,String writtenName) {
        this.size = size;
        this.writtenName = writtenName;
    }

    public int getSize() {
        return this.size;
    }

    public String getWrittenName() {
        return this.writtenName;
    }
}

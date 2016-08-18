package com.gordonchild.websocket.battleship.exception;

public class InvalidPositionException extends RuntimeException {

    private int x;
    private int y;

    public InvalidPositionException(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}

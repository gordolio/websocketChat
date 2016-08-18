package com.gordonchild.websocket.battleship.utils;

public class PositionHelper {

    public static int getX(int position) {
        return position % 10;
    }

    public static int getY(int position) {
        return (position - getX(position)) / 10;
    }

    public static int getPosition(int x, int y) {
        return y * 10 + x;
    }

    public static int positionWithX(int position, int x) {
        return getY(position) + x;
    }

    public static int positionWithY(int position, int y) {
        return getX(position) + y * 10;
    }

}

package com.gordonchild.websocket.battleship.domain.handler;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ShipPositionTest {

    @Test
    public void testGridPositions() {

        ShipPosition position = new ShipPosition();
        position.setX(5);
        position.setY(3);

        assertThat(position.getX(), is(5));
        assertThat(position.getY(), is(3));

    }

}

package com.gordonchild.websocket.service;

import static com.gordonchild.websocket.domain.battleship.Game.Player;

import com.gordonchild.websocket.domain.battleship.FireResult;
import com.gordonchild.websocket.domain.battleship.Game;
import com.gordonchild.websocket.domain.battleship.Position;
import com.gordonchild.websocket.domain.battleship.ShipPosition;

public interface BattleshipService {

    void placeShip(final Game game, Player player, ShipPosition shipPosition);
    FireResult fireAtPosition(final Game game, Player player, Position position);

}

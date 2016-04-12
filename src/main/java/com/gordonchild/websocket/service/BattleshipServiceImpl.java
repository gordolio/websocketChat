package com.gordonchild.websocket.service;

import static com.gordonchild.websocket.domain.battleship.Game.Player;
import static com.gordonchild.websocket.domain.battleship.PositionStatus.EMPTY;

import org.springframework.stereotype.Service;

import com.gordonchild.websocket.domain.battleship.Board;
import com.gordonchild.websocket.domain.battleship.FireResult;
import com.gordonchild.websocket.domain.battleship.Game;
import com.gordonchild.websocket.domain.battleship.Position;
import com.gordonchild.websocket.domain.battleship.PositionStatus;
import com.gordonchild.websocket.domain.battleship.Ship;
import com.gordonchild.websocket.domain.battleship.ShipPosition;
import com.gordonchild.websocket.exception.PositionNotEmptyException;

@Service("battleshipService")
public class BattleshipServiceImpl implements BattleshipService {

    @FunctionalInterface
    private interface PositionAction {
        PositionStatus performActionOnPosition(PositionStatus status);
    }

    @FunctionalInterface
    private interface ShipAction {
        void performActionOnShip(ShipPosition ship, int position);
    }


    @Override
    public void placeShip(final Game game, Player player, ShipPosition shipPosition) {
        Board board;
        if(Player.ONE.equals(player)) {
            board = game.getPlayerOneBoard();
        } else {
            board = game.getPlayerTwoBoard();
        }
        this.checkForCollision(board, shipPosition);
        this.addShipToBoard(board, shipPosition);
    }

    private void checkForCollision(Board board, ShipPosition position) {
        this.doWithBoardAndPos(board, position, status->{
            if(!EMPTY.equals(status)) {
                throw new PositionNotEmptyException();
            }
            return status;
        });
    }

    private void doWithBoardAndPos(Board board, ShipPosition position, PositionAction action) {
        Ship ship = position.getShip();
        PositionStatus[] positionStatuses = board.getPositions();
        for(int i=0;i<ship.getSize();i++) {
            int newPos = i * position.getDirection().getTranslate() + position.getPosition();
            positionStatuses[newPos] = action.performActionOnPosition(positionStatuses[newPos]);
        }
    }

    private void addShipToBoard(Board board, ShipPosition position) {
        this.doWithBoardAndPos(board, position, status->PositionStatus.SHIP);
        board.getShipPositions().add(position);
    }

    private void findShipPosition(Board board, int foundShipIndex, ShipAction shipAction) {
        for(ShipPosition shipPosition : board.getShipPositions()) {
            for(int i=0;i<shipPosition.getShip().getSize();i++) {
                int newPos = i * shipPosition.getDirection().getTranslate() + shipPosition.getPosition();
                if(newPos == foundShipIndex) {
                    shipAction.performActionOnShip(shipPosition, i);
                }
            }
        }
    }

    @Override
    public FireResult fireAtPosition(final Game game, Player player, Position position) {
        Board board;
        if(Player.ONE.equals(player)) {
            board = game.getPlayerOneBoard();
        } else {
            board = game.getPlayerTwoBoard();
        }

        final FireResult fireResult = new FireResult();
        int pos = 10 * position.getY() + position.getX();
        PositionStatus[] positionStatuses = board.getPositions();
        if(positionStatuses[pos] == null || positionStatuses[pos] == PositionStatus.EMPTY) {
            positionStatuses[pos] = PositionStatus.MISS;
            fireResult.setResultStatus(FireResult.ResultStatus.MISS);
            fireResult.setPosition(position);
        } else if(positionStatuses[pos] == PositionStatus.SHIP) {
            this.findShipPosition(board, pos, (ShipPosition ship, int hitPosition)->{
                Boolean[] hits = ship.getHits();
                hits[hitPosition] = true;
                ship.setNumberOfHits(ship.getNumberOfHits() + 1);
                fireResult.setResultStatus(FireResult.ResultStatus.HIT);
                if(ship.getNumberOfHits() == ship.getShip().getSize()) {
                    fireResult.setResultStatus(FireResult.ResultStatus.SUNK);
                    ship.setSunk(true);
                    board.setSunkShips(board.getSunkShips() + 1);
                }
                if(board.getSunkShips() == board.getShipPositions().size()) {
                    fireResult.setResultStatus(FireResult.ResultStatus.GAME_OVER);
                    game.setGameStatus(Game.GameStatus.OVER);
                }
            });
            positionStatuses[pos] = PositionStatus.HIT;
        } else {
            fireResult.setResultStatus(FireResult.ResultStatus.INVALID_POSITION);
            fireResult.setPosition(position);
        }

        return fireResult;
    }
}

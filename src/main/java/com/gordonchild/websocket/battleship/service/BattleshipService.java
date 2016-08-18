package com.gordonchild.websocket.battleship.service;

import static com.gordonchild.websocket.battleship.domain.handler.PositionStatus.Status.EMPTY;
import static com.gordonchild.websocket.battleship.domain.handler.PositionStatus.Status.HIT;
import static com.gordonchild.websocket.battleship.domain.handler.PositionStatus.Status.MISS;
import static com.gordonchild.websocket.battleship.domain.handler.PositionStatus.Status.SHIP;

import org.springframework.stereotype.Service;

import com.gordonchild.websocket.battleship.domain.GameStatus;
import com.gordonchild.websocket.battleship.domain.Player;
import com.gordonchild.websocket.battleship.domain.handler.Board;
import com.gordonchild.websocket.battleship.domain.handler.FireResult;
import com.gordonchild.websocket.battleship.domain.handler.Game;
import com.gordonchild.websocket.battleship.domain.handler.Position;
import com.gordonchild.websocket.battleship.domain.handler.PositionStatus;
import com.gordonchild.websocket.battleship.domain.handler.Ship;
import com.gordonchild.websocket.battleship.domain.handler.ShipPosition;
import com.gordonchild.websocket.battleship.exception.PositionNotEmptyException;
import com.gordonchild.websocket.battleship.utils.PositionHelper;

@Service
public class BattleshipService {

    @FunctionalInterface
    private interface PositionAction {
        void performActionOnPosition(PositionStatus status);
    }

    @FunctionalInterface
    private interface ShipAction {
        void performActionOnShip(ShipPosition ship, int position);
    }


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
            if(!EMPTY.equals(status.getStatus())) {
                throw new PositionNotEmptyException(status.getX(), status.getY());
            }
        });
    }

    private void doWithBoardAndPos(Board board, ShipPosition position, PositionAction action) {
        Ship ship = position.getShip();
        PositionStatus[] positionStatuses = board.getPositions();
        for(int i=0;i<ship.getSize();i++) {
            int newPos = i * position.getDirection().getTranslate() + position.getStartPosition();
            action.performActionOnPosition(positionStatuses[newPos]);
        }
    }

    private void addShipToBoard(Board board, ShipPosition position) {
        this.doWithBoardAndPos(board, position, status->status.setStatus(SHIP));
        board.getShipPositions().add(position);
    }

    private void findShipPosition(Board board, int foundShipIndex, ShipAction shipAction) {
        for(ShipPosition shipPosition : board.getShipPositions()) {
            for(int i=0;i<shipPosition.getShip().getSize();i++) {
                int newPos = i * shipPosition.getDirection().getTranslate() + shipPosition.getStartPosition();
                if(newPos == foundShipIndex) {
                    shipAction.performActionOnShip(shipPosition, i);
                }
            }
        }
    }

    public FireResult fireAtPosition(final Game game, Player player, Position position) {
        Board board;
        if(Player.ONE.equals(player)) {
            board = game.getPlayerOneBoard();
        } else {
            board = game.getPlayerTwoBoard();
        }

        final FireResult fireResult = new FireResult();
        int pos = PositionHelper.getPosition(position.getX(), position.getY());
        PositionStatus[] positionStatuses = board.getPositions();
        if(pos >= 100) {
            fireResult.setResultStatus(FireResult.ResultStatus.INVALID_POSITION);
            fireResult.setPosition(position);
        } else if(EMPTY.equals(positionStatuses[pos].getStatus())) {
            positionStatuses[pos].setStatus(MISS);
            fireResult.setResultStatus(FireResult.ResultStatus.MISS);
            fireResult.setPosition(position);
        } else if(SHIP.equals(positionStatuses[pos].getStatus())) {
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
                    game.setGameStatus(GameStatus.OVER);
                }
            });
            positionStatuses[pos].setStatus(HIT);
        }

        return fireResult;
    }
}

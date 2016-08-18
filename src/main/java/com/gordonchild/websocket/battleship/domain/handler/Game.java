package com.gordonchild.websocket.battleship.domain.handler;

import com.gordonchild.websocket.battleship.domain.GameStatus;
import com.gordonchild.websocket.battleship.domain.Player;

public class Game {

    private String gameId;
    private String playerOneId;
    private String playerTwoId;
    private Board playerOneBoard;
    private Board playerTwoBoard;
    private String playerOneName;
    private String playerTwoName;
    private Player currentPlayerTurn;
    private GameStatus gameStatus;

    public String getGameId() {
        return this.gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public Board getPlayerOneBoard() {
        return this.playerOneBoard;
    }

    public void setPlayerOneBoard(Board playerOneBoard) {
        this.playerOneBoard = playerOneBoard;
    }

    public Board getPlayerTwoBoard() {
        return this.playerTwoBoard;
    }

    public void setPlayerTwoBoard(Board playerTwoBoard) {
        this.playerTwoBoard = playerTwoBoard;
    }

    public GameStatus getGameStatus() {
        return this.gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public String getPlayerOneId() {
        return this.playerOneId;
    }

    public void setPlayerOneId(String playerOneId) {
        this.playerOneId = playerOneId;
    }

    public String getPlayerTwoId() {
        return this.playerTwoId;
    }

    public void setPlayerTwoId(String playerTwoId) {
        this.playerTwoId = playerTwoId;
    }

    public String getPlayerOneName() {
        return this.playerOneName;
    }

    public void setPlayerOneName(String playerOneName) {
        this.playerOneName = playerOneName;
    }

    public String getPlayerTwoName() {
        return this.playerTwoName;
    }

    public void setPlayerTwoName(String playerTwoName) {
        this.playerTwoName = playerTwoName;
    }
}

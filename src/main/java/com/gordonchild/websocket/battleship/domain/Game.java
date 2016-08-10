package com.gordonchild.websocket.battleship.domain;

public class Game {

    public enum Player {
        ONE,TWO
    }

    public enum GameStatus {
        SETUP,ACTIVE,OVER
    }

    private String gameId;
    private Board playerOneBoard;
    private Board playerTwoBoard;
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
}

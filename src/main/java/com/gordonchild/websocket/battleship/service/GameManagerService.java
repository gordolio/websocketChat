package com.gordonchild.websocket.battleship.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gordonchild.websocket.battleship.domain.GameStatus;
import com.gordonchild.websocket.battleship.domain.Player;
import com.gordonchild.websocket.battleship.domain.handler.Board;
import com.gordonchild.websocket.battleship.domain.handler.Game;
import com.gordonchild.websocket.battleship.domain.handler.ShipPosition;
import com.gordonchild.websocket.battleship.exception.GameNotFoundException;

@Service
public class GameManagerService {

    private Map<String,Game> games = new HashMap<>();

    @Autowired
    private BattleshipService battleshipService;

    public Game newGame(String playerOneName, String sessionId) {
        Game game = new Game();
        String gameId;
        do {
            gameId = RandomStringUtils.randomAlphabetic(5);
        } while(this.games.containsKey(gameId));
        game.setGameId(gameId);
        game.setGameStatus(GameStatus.SETUP);
        game.setPlayerOneId(sessionId);
        game.setPlayerOneName(playerOneName);
        game.setPlayerOneBoard(new Board());
        game.setPlayerTwoBoard(new Board());
        this.games.put(gameId,game);
        return game;
    }

    public Game joinGame(String playerTwoName, String gameId, String sessionId) {
        Game game = this.getGame(gameId);
        game.setPlayerTwoId(sessionId);
        game.setPlayerTwoName(playerTwoName);
        return game;
    }

    private void placeShip(String gameId, String sessionId, ShipPosition position) {
        Game game = this.getGame(gameId);
        Player player;
        if(sessionId.equals(game.getPlayerOneId())) {
            player = Player.ONE;
        } else if(sessionId.equals(game.getPlayerTwoId())) {
            player = Player.TWO;
        } else {
            throw new RuntimeException(String.format("Invalid sessionId: %s for game: %s", sessionId, gameId));
        }
        this.battleshipService.placeShip(game, player, position);
    }

    private Game getGame(String gameId) {
        Game game = this.games.get(gameId);
        if(game == null) {
            throw new GameNotFoundException(gameId);
        }
        return game;
    }



}

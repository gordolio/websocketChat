package com.gordonchild.websocket.battleship.controller;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gordonchild.websocket.battleship.domain.event.BeginGameEvent;
import com.gordonchild.websocket.battleship.domain.event.NewGameEvent;
import com.gordonchild.websocket.battleship.domain.handler.Game;
import com.gordonchild.websocket.battleship.domain.request.JoinGameRequest;
import com.gordonchild.websocket.battleship.domain.request.NewGameRequest;
import com.gordonchild.websocket.battleship.domain.request.PlaceShipRequest;
import com.gordonchild.websocket.battleship.service.GameManagerService;

@Controller
public class BattleshipController implements ErrorController {

    private static final String BATTLESHIP_ERROR = "battleship_error";
    private static final String SESSION_ID = "simpSessionId";
    private static final String GAME_EVENTS = "/gameEvents";
    private static final String GAME_EVENTS_QUEUE = "/queue" + GAME_EVENTS;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private GameManagerService gameManagerService;

    @Autowired
    private Mapper mapper;

    @MessageMapping("/newGame")
    @SendToUser(GAME_EVENTS_QUEUE)
    public NewGameEvent newGame(@Payload NewGameRequest newGameRequest, @Header(SESSION_ID) String sessionId) {
        Game game = this.gameManagerService.newGame(newGameRequest.getPlayerName(), sessionId);
        return this.mapper.map(game, NewGameEvent.class);
    }

    @MessageMapping("/joinGame")
    public void joinGame(@Payload JoinGameRequest joinGameRequest, @Header(SESSION_ID) String sessionId) {
        Game game = this.gameManagerService.joinGame(joinGameRequest.getPlayerName(), joinGameRequest.getGameId(), sessionId);
        BeginGameEvent event = this.mapper.map(game, BeginGameEvent.class);

        this.sendToUser(game.getPlayerOneId(), GAME_EVENTS_QUEUE, event);
        this.sendToUser(game.getPlayerTwoId(), GAME_EVENTS_QUEUE, event);
    }

    @MessageMapping("/placeShip")
    public void placeShip(@Payload PlaceShipRequest request, @Header(SESSION_ID) String sessionId) {

    }

    @RequestMapping("/battleship")
    public String index() {
        return "battleship";
    }

    @Override
    public String getErrorPath() {
        return BATTLESHIP_ERROR;
    }

    private void sendToUser(String sessionId, String destination, Object payload) {
        this.simpMessagingTemplate.convertAndSendToUser(
                sessionId,
                destination,
                payload,
                createHeaders(sessionId, payload.getClass()));
    }

    private MessageHeaders createHeaders(String sessionId, Class<?> returnType) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setHeader(SimpMessagingTemplate.CONVERSION_HINT_HEADER, returnType);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }
}

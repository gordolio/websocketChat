package com.gordonchild.websocket.battleship.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import com.gordonchild.websocket.battleship.domain.request.StartGameRequest;
import com.gordonchild.websocket.battleship.service.BattleshipService;

@Controller
public class BattleshipController implements ErrorController {

    private static final String BATTLESHIP_ERROR = "battleship_error";

    @Autowired
    private BattleshipService battleshipService;

    @MessageMapping("/startGame")
    public void sendMessage(@Payload StartGameRequest startGameRequest) {
    }

    @Override
    public String getErrorPath() {
        return BATTLESHIP_ERROR;
    }
}

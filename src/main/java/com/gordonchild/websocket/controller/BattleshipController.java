package com.gordonchild.websocket.controller;

import com.gordonchild.websocket.domain.battleship.request.StartGameRequest;
import com.gordonchild.websocket.service.BattleshipService;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class BattleshipController implements ErrorController {

    private static final String BATTLESHIP_ERROR = "battleship_error";

    private final BattleshipService battleshipService;

    public BattleshipController(BattleshipService battleshipService) {
        this.battleshipService = battleshipService;
    }

    @MessageMapping("/startGame")
    public void sendMessage(@Payload StartGameRequest startGameRequest) {
    }

    //@Override
    public String getErrorPath() {
        return BATTLESHIP_ERROR;
    }
}

package com.gordonchild.websocket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;

import com.gordonchild.websocket.service.BattleshipService;

@Controller
public class BattleshipController implements ErrorController {

    private static final String BATTLESHIP_ERROR = "battleship_error";

    @Autowired
    private BattleshipService battleshipService;


    @Override
    public String getErrorPath() {
        return BATTLESHIP_ERROR;
    }
}

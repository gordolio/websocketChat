package com.gordonchild.websocket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/party")
public class PartyController {

    @RequestMapping("/create")
    public String createParty() {
        return "";
    }
}

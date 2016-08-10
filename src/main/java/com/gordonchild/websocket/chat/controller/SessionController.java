package com.gordonchild.websocket.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gordonchild.websocket.chat.domain.request.ConnectRequest;
import com.gordonchild.websocket.chat.domain.session.Session;
import com.gordonchild.websocket.chat.service.SessionService;

@Controller
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @ResponseBody
    @RequestMapping(path="/startSession",method=RequestMethod.GET)
    public Session startSession() {
        return this.sessionService.startSession();
    }

    @MessageMapping("/socketConnect")
    public void socketConnect(@Payload ConnectRequest connectRequest, SimpMessageHeaderAccessor headerAccessor) {
        this.sessionService.updateSocketSessionId(connectRequest.getSessionId(), headerAccessor.getSessionId());
    }

}

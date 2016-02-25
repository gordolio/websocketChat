package com.gordonchild.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gordonchild.websocket.chat.ChatSessionService;
import com.gordonchild.websocket.domain.SendMessageRequest;
import com.gordonchild.websocket.domain.UserJoinRequest;
import com.gordonchild.websocket.domain.UserLeaveRequest;

@Controller
public class ChatController implements ErrorController {

    private static final String ERROR_PATH = "/error";

    @Autowired
    private ChatSessionService chatSessionService;

    @RequestMapping("/")
    public String home() {
        return "index";
    }

    @RequestMapping("/chat")
    public String chat() {
        return "chat";
    }

    @RequestMapping(ERROR_PATH)
    public String error() {
        return "error";
    }

    @MessageMapping("/sendMessage")
    public void sendMessage(@Payload SendMessageRequest sendMessageRequest) {
        this.chatSessionService.sendMessage(sendMessageRequest);
    }

    @MessageMapping("/userJoin")
    public void userJoin(@Payload UserJoinRequest user) {
        this.chatSessionService.userJoin(user);
    }

    @MessageMapping("/userLeave")
    public void userLeave(@Payload UserLeaveRequest user) {
        this.chatSessionService.userLeave(user);
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}

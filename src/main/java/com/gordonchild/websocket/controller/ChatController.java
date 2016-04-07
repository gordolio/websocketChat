package com.gordonchild.websocket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gordonchild.websocket.domain.request.JoinRoomRequest;
import com.gordonchild.websocket.domain.request.LeaveRoomRequest;
import com.gordonchild.websocket.domain.request.SendMessageRequest;
import com.gordonchild.websocket.domain.request.UserTypingRequest;
import com.gordonchild.websocket.service.ChatRoomService;

@Controller
public class ChatController implements ErrorController {

    private static final String ERROR_PATH = "/error";

    @Autowired
    private ChatRoomService chatRoomService;

    @RequestMapping("/")
    public String home() {
        return "index";
    }

    @RequestMapping(ERROR_PATH)
    public String error() {
        return "error";
    }

    @MessageMapping("/sendMessage")
    public void sendMessage(@Payload SendMessageRequest sendMessageRequest) {
        this.chatRoomService.sendMessage(sendMessageRequest);
    }

    @MessageMapping("/joinRoom")
    public void userJoin(@Payload JoinRoomRequest user) {
        this.chatRoomService.userJoin(user);
    }

    @MessageMapping("/userTyping")
    public void userTyping(@Payload UserTypingRequest user) {
        this.chatRoomService.userTyping(user);
    }

    @MessageMapping("/leaveRoom")
    public void userLeave(@Payload LeaveRoomRequest user) {
        this.chatRoomService.userLeave(user);
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}

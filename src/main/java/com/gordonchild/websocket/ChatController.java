package com.gordonchild.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gordonchild.websocket.chat.ChatRoomService;
import com.gordonchild.websocket.domain.ChatSession;
import com.gordonchild.websocket.domain.StartChatRequest;
import com.gordonchild.websocket.domain.request.ConnectRequest;
import com.gordonchild.websocket.domain.request.JoinRoomRequest;
import com.gordonchild.websocket.domain.request.LeaveRoomRequest;
import com.gordonchild.websocket.domain.request.SendMessageRequest;

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

    @RequestMapping("/startChat")
    @ResponseBody
    public ChatSession startSession(StartChatRequest request) {
        return this.chatRoomService.startSession(request);
    }

    @MessageMapping("/socketConnect")
    public void socketConnect(@Payload ConnectRequest connectRequest, SimpMessageHeaderAccessor headerAccessor) {
        this.chatRoomService.updateSocketSessionId(connectRequest.getSessionId(), headerAccessor.getSessionId());
    }

    @MessageMapping("/sendMessage")
    public void sendMessage(@Payload SendMessageRequest sendMessageRequest) {
        this.chatRoomService.sendMessage(sendMessageRequest);
    }

    @MessageMapping("/userJoin")
    public void userJoin(@Payload JoinRoomRequest user) {
        this.chatRoomService.userJoin(user);
    }

    @MessageMapping("/userLeave")
    public void userLeave(@Payload LeaveRoomRequest user) {
        this.chatRoomService.userLeave(user);
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}

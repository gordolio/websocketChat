package com.gordonchild.websocket.chat.service;

import com.gordonchild.websocket.chat.domain.request.ClearVotingRequest;
import com.gordonchild.websocket.chat.domain.request.JoinRoomRequest;
import com.gordonchild.websocket.chat.domain.request.LeaveRoomRequest;
import com.gordonchild.websocket.chat.domain.request.RevealVoteRequest;
import com.gordonchild.websocket.chat.domain.request.SendMessageRequest;
import com.gordonchild.websocket.chat.domain.request.UserAwayRequest;
import com.gordonchild.websocket.chat.domain.request.UserTypingRequest;
import com.gordonchild.websocket.chat.domain.request.UserVoteRequest;
import com.gordonchild.websocket.chat.domain.session.ChatRoomSession;

public interface ChatRoomService {

    void sendMessage(SendMessageRequest request);
    void userJoin(JoinRoomRequest userId);
    void userLeave(LeaveRoomRequest user);
    void userAway(UserAwayRequest userAwayRequest);
    void userTyping(UserTypingRequest user);
    void userVote(UserVoteRequest request);
    void revealVotes(RevealVoteRequest request);
    void clearVoting(ClearVotingRequest request);

    ChatRoomSession getSocketSession(String sessionId);
}

package com.gordonchild.websocket.domain.event;

import com.gordonchild.websocket.domain.request.UserVoteRequest;

public class UserVoteData extends UserData {

    private UserVoteRequest.VoteType vote;

    public UserVoteRequest.VoteType getVote() {
        return this.vote;
    }

    public void setVote(UserVoteRequest.VoteType vote) {
        this.vote = vote;
    }
}

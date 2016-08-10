package com.gordonchild.websocket.chat.domain.event;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gordonchild.websocket.chat.domain.request.UserVoteRequest;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME,property="type")
public class UserData {

    private String username;
    private String publicId;
    private UserVoteRequest.VoteType vote;

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPublicId() {
        return this.publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public UserVoteRequest.VoteType getVote() {
        return this.vote;
    }

    public void setVote(UserVoteRequest.VoteType vote) {
        this.vote = vote;
    }
}

package com.gordonchild.websocket.domain.request;

public class UserVoteRequest extends RoomRequest {

    public enum VoteType {
        QUESTION, ZERO, HALF, ONE, TWO, THREE, FIVE, EIGHT, THIRTEEN, TWENTY_ONE, BREAK, CLEAR
    }

    private VoteType vote;

    public VoteType getVote() {
        return this.vote;
    }

    public void setVote(VoteType vote) {
        this.vote = vote;
    }
}

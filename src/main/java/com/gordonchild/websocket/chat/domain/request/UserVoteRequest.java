package com.gordonchild.websocket.chat.domain.request;

public class UserVoteRequest extends RoomRequest {

    public enum VoteType {
        UNVOTE, HIDDEN, QUESTION, BREAK, ZERO, HALF, ONE, TWO, THREE, FIVE, EIGHT, THIRTEEN, TWENTY_ONE
    }

    private VoteType vote;

    public VoteType getVote() {
        return this.vote;
    }

    public void setVote(VoteType vote) {
        this.vote = vote;
    }
}

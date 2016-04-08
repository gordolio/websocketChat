package com.gordonchild.websocket.domain.event;

import java.util.List;

public class RevealVotesEvent extends UserData {

    private List<UserVoteData> votes;

    public List<UserVoteData> getVotes() {
        return this.votes;
    }

    public void setVotes(List<UserVoteData> votes) {
        this.votes = votes;
    }
}

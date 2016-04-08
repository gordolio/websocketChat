package com.gordonchild.websocket.domain.event;

public class VoteEvent extends UserData {

    private Boolean didClear;

    public Boolean getDidClear() {
        return this.didClear;
    }

    public void setDidClear(Boolean didClear) {
        this.didClear = didClear;
    }
}

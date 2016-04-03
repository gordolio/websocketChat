package com.gordonchild.websocket.domain.event;

import java.util.List;

public class JoinEvent extends UserData {

    private List<UserData> allUsers;

    public List<UserData> getAllUsers() {
        return this.allUsers;
    }

    public void setAllUsers(List<UserData> allUsers) {
        this.allUsers= allUsers;
    }
}

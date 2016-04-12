package com.gordonchild.websocket.domain.request;

import com.gordonchild.websocket.domain.session.ChatSession;

public class RoomRequest {

  private String sessionId;
  private String roomName;

  public RoomRequest(){}
  public RoomRequest(ChatSession chatSession) {
    this.sessionId = chatSession.getSessionId();
    this.roomName = chatSession.getRoomName();
  }

  public String getSessionId() {
    return this.sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public String getRoomName() {
    return roomName;
  }

  public void setRoomName(String roomName) {
    this.roomName = roomName;
  }
}

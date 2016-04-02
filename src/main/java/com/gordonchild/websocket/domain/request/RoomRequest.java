package com.gordonchild.websocket.domain.request;

public class RoomRequest {

  private String sessionId;
  private String roomName;

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

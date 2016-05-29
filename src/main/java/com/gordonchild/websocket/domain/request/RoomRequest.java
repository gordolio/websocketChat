package com.gordonchild.websocket.domain.request;

import com.gordonchild.websocket.domain.session.ChatRoomSession;

public class RoomRequest {

  private String sessionId;
  private String roomName;

  public RoomRequest(){}
  public RoomRequest(ChatRoomSession chatRoomSession) {
    this.sessionId = chatRoomSession.getSessionId();
    this.roomName = chatRoomSession.getRoomName();
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

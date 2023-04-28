package com.example.realtimechat.model;

public class RoomChat {

    String roomId ;
    String email ;
    String lastMessage ;

    public RoomChat(String roomId, String email, String lastMessage) {
        this.roomId = roomId;
        this.email = email;
        this.lastMessage = lastMessage;
    }

    public RoomChat() {
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}

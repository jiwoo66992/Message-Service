package com.example.realtimechat.chat;

public class Message {
    public Long id;
    public String name;
    public String content;
    public String roomId;
    public Integer userId;
    public boolean me;

    public Message( String name, String content, String roomId, Integer userId, boolean me) {
        this.name = name;
        this.content = content;
        this.me = me;
        this.roomId = roomId;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean getMe() {
        return me;
    }

    public void setMe(boolean me) {
        this.me = me;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}

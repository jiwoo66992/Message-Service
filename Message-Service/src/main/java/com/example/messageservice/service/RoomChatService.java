package com.example.messageservice.service;

import com.example.messageservice.domain.entity.RoomChat;
import com.example.messageservice.input.CreateRoomChatInput;
import com.example.messageservice.output.RoomChatItem;

import java.util.List;

public interface RoomChatService {

  RoomChat createRoomChat(CreateRoomChatInput input);

  List<RoomChatItem> getAllRoomChat(Integer userId);
}

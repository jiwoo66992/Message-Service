package com.example.messageservice.service;

import com.example.messageservice.domain.dto.MessageDto;
import com.example.messageservice.domain.entity.Message;
import com.example.messageservice.output.GetMessageOutput;

import java.util.List;

public interface MessageService {

  void save(MessageDto message);

  List<GetMessageOutput> getAllMessageByRoomId(String convertId);

}

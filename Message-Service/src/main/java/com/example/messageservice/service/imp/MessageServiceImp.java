package com.example.messageservice.service.imp;

import com.example.messageservice.domain.dto.MessageDto;
import com.example.messageservice.domain.entity.Message;
import com.example.messageservice.domain.entity.RoomChat;
import com.example.messageservice.domain.entity.Account;
import com.example.messageservice.exception.NotFoundException;
import com.example.messageservice.output.GetMessageOutput;
import com.example.messageservice.repository.MessageRepository;
import com.example.messageservice.repository.RoomChatRepository;
import com.example.messageservice.repository.RoomChatUserRepository;
import com.example.messageservice.repository.AccountRepository;
import com.example.messageservice.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImp implements MessageService {
  private final MessageRepository messageRepository;
  private final RoomChatRepository roomChatRepository;
  private final AccountRepository accountRepository;
  private final RoomChatUserRepository roomChatUserRepository;

  public MessageServiceImp(MessageRepository messageRepository, RoomChatRepository roomChatRepository,
                           AccountRepository accountRepository, RoomChatUserRepository roomChatUserRepository) {
    this.messageRepository = messageRepository;
    this.roomChatRepository = roomChatRepository;
    this.accountRepository = accountRepository;
    this.roomChatUserRepository = roomChatUserRepository;
  }

  @Override
  public void save(MessageDto input) {
    RoomChat roomChat = roomChatRepository.findByConvertId(input.getRoomId());
    Optional<Account> user = accountRepository.findById(input.getUserId());

    if (user.isEmpty()) {
      throw new NotFoundException("Can not found user by id = " + input.getUserId());
    }

    messageRepository.save(new Message(input.getContent(), input.getName(), roomChat, user.get()));
  }

  @Override
  public List<GetMessageOutput> getAllMessageByRoomId(String convertId) {
    RoomChat roomChat = roomChatRepository.findByConvertId(convertId);

    if (roomChat == null) {
      throw new NotFoundException("Can not found room chat id = " + convertId);
    }

    List<GetMessageOutput> outputs = new ArrayList<>();

    for (Message item : roomChat.getMessages()) {
      outputs.add(new GetMessageOutput(item.getId(), item.getContent(), item.getAccount().getId(),
          item.getAccount().getEmail(), item.getCreatedDate()));
    }

    return outputs;
  }

}

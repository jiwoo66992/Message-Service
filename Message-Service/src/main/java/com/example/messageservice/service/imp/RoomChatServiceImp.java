package com.example.messageservice.service.imp;

import com.example.messageservice.constant.Constant;
import com.example.messageservice.domain.entity.Account;
import com.example.messageservice.domain.entity.RoomChat;
import com.example.messageservice.domain.entity.RoomChatUser;
import com.example.messageservice.exception.NotFoundException;
import com.example.messageservice.input.CreateRoomChatInput;
import com.example.messageservice.output.RoomChatItem;
import com.example.messageservice.repository.AccountRepository;
import com.example.messageservice.repository.RoomChatRepository;
import com.example.messageservice.repository.RoomChatUserRepository;
import com.example.messageservice.service.RoomChatService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomChatServiceImp implements RoomChatService {
  private final RoomChatRepository roomChatRepository;
  private final AccountRepository accountRepository;
  private final RoomChatUserRepository roomChatUserRepository;

  public RoomChatServiceImp(RoomChatRepository roomChatRepository, AccountRepository accountRepository,
                            RoomChatUserRepository roomChatUserRepository) {
    this.roomChatRepository = roomChatRepository;
    this.accountRepository = accountRepository;
    this.roomChatUserRepository = roomChatUserRepository;
  }

  @Override
  public RoomChat createRoomChat(CreateRoomChatInput input) {
    Account account = findUserById(input.getUserId());
    Account friend = findUserById(input.getFriendId());

    List<RoomChatUser> lstRoomChatUser = roomChatUserRepository.findAllByAccount(account);
    List<RoomChatUser> lstRoomChatFriend = roomChatUserRepository.findAllByAccount(friend);

    RoomChat roomChat = null;

    for (RoomChatUser item : lstRoomChatUser) {
      if (lstRoomChatFriend.stream().anyMatch(i -> i.getRoomChat().getId().equals(item.getRoomChat().getId()))) {
        roomChat = item.getRoomChat();
      }
    }

    if (roomChat == null) {
      roomChat = roomChatRepository.save(new RoomChat());
      roomChat.setConvertId(roomChat.getId().toString());
      roomChat = roomChatRepository.save(roomChat);

      // Create 2 record room_chat_user
      roomChatUserRepository.save(new RoomChatUser(Constant.ROOM_NORMAL, account, roomChat));
      roomChatUserRepository.save(new RoomChatUser(Constant.ROOM_NORMAL, friend, roomChat));
    }

    return roomChat;
  }

  @Override
  public List<RoomChatItem> getAllRoomChat(Integer userId) {
    Optional<Account> user = accountRepository.findById(userId);
    if (user.isEmpty()) {
      throw new NotFoundException("Can not found user by id = " + userId);
    }
    List<RoomChatUser> lst = roomChatUserRepository.findAllByAccount(user.get());

    List<RoomChatItem> output = new ArrayList<>();

    for (RoomChatUser item : lst) {
      if (item.getRoomChat().getMessages().size() > 0) {
        output.add(new RoomChatItem(
                item.getRoomChat().getConvertId(),
                item.getRoomChat().getRoomChatUsers()
                    .stream()
                    .filter(i -> !i.getAccount().getId().equals(userId))
                    .findFirst().get().getAccount().getEmail(),
                item.getRoomChat().getMessages().get(item.getRoomChat().getMessages().size() - 1).getContent(),
                item.getRoomChat().getMessages().get(item.getRoomChat().getMessages().size() - 1).getCreatedDate()
            )
        );
      }
    }

    output.sort((o1, o2) -> compare(o1.getCreatedDate(), o2.getCreatedDate()));

    return output;
  }

  public int compare(Timestamp t1, Timestamp t2) {
    return Long.compare(t2.getTime(), t1.getTime());
  }


  private Account findUserById(Integer id) {
    Optional<Account> user = accountRepository.findById(id);
    if (user.isEmpty()) {
      throw new NotFoundException("Can not find user by id = " + id);
    }
    return user.get();
  }
}

package com.example.messageservice.repository;

import com.example.messageservice.domain.entity.Account;
import com.example.messageservice.domain.entity.RoomChat;
import com.example.messageservice.domain.entity.RoomChatUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomChatUserRepository extends JpaRepository<RoomChatUser, Long> {

  RoomChatUser findByRoomChatAndAccount(RoomChat roomChat, Account account);

  List<RoomChatUser> findAllByAccount(Account account);

}

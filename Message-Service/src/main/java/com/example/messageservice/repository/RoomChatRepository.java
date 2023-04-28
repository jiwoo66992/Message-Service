package com.example.messageservice.repository;

import com.example.messageservice.domain.entity.RoomChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomChatRepository extends JpaRepository<RoomChat, Long> {

  RoomChat findByConvertId(String convertId);

}

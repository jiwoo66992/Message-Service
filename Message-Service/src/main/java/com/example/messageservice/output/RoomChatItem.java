package com.example.messageservice.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomChatItem {

  private String convertId;

  private String email;

  private String lastMessage;

  private Timestamp createdDate;

}

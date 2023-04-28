package com.example.messageservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {

  public String name;
  public String content;
  public boolean me;
  private Integer userId;
  private String roomId; // ConvertId

  public MessageDto(String content, boolean me, String name, String roomId, String userId) {
    this.name = name;
    this.content = content;
    this.me = me;
    this.roomId = roomId;
    this.userId = Integer.parseInt(userId);
  }

}

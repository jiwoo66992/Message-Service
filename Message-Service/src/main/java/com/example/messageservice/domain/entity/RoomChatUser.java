package com.example.messageservice.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "room_chat_user")
public class RoomChatUser {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  private String name;

  private String type;

  @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
  private Account account;

  @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
  private RoomChat roomChat;

  public RoomChatUser(String type, Account account, RoomChat roomChat) {
    this.type = type;
    this.account = account;
    this.roomChat = roomChat;
  }
}

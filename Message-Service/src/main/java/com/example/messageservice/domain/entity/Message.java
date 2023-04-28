package com.example.messageservice.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "messages")
public class Message {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  public String name;
  public String content;
  @CreationTimestamp
  private Timestamp createdDate;

  @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
  @JsonIgnore
  private RoomChat roomChat;

  @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
  private Account account;

  public Message(String content, String name) {
    this.name = name;
    this.content = content;
  }

  public Message(String content, String name, RoomChat roomChat, Account account) {
    this.name = name;
    this.content = content;
    this.roomChat = roomChat;
    this.account = account;
  }

}

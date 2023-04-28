package com.example.messageservice.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "room_chat")
public class RoomChat {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String convertId;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "roomChat")
  @JsonIgnore
  private List<Message> messages;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "roomChat")
  @JsonIgnore
  private List<RoomChatUser> roomChatUsers;

}

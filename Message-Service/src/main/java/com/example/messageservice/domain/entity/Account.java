package com.example.messageservice.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  private String email;

  private String password;

  private String state;

  private String role;

  private String createdBy;

  private Date createdDate;

  private String updatedDate;

  private String updatedBy;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "account")
  @JsonIgnore
  private List<RoomChatUser> roomChatUsers;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "account")
  @JsonIgnore
  private List<Message> messages;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  public Account(String email, String password, String state) {
    this.email = email;
    this.password = password;
    this.state = state;
  }
}

package com.example.messageservice.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Lob
  private String avatar;

  private String name;

  private String gender;

  private Date dob;

  private String phone;

  private String email;

  @Lob
  private String address;

  @Lob
  private String job;

  @Lob
  private String workplace;

  private boolean isTopStudent;

  @Lob
  private String feedback;

  private String state;

  private String createdBy;

  private Date createdDate;

  private String updatedDate;

  private String updatedBy;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "account_id", referencedColumnName = "id")
  @JsonIgnore
  private Account account;

  public User(String name, String gender, Date dob, String phone, String email, String address, String job, String workplace, boolean isTopStudent, String feedback, String state) {
    this.name = name;
    this.gender = gender;
    this.dob = dob;
    this.phone = phone;
    this.email = email;
    this.address = address;
    this.job = job;
    this.workplace = workplace;
    this.isTopStudent = isTopStudent;
    this.feedback = feedback;
    this.state = state;
  }
}

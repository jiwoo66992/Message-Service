package com.example.messageservice.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDetailInput {

  private String email;

  private String password;

  private String name;

  private String gender;

  private Date dob;

  private String phone;

  private String address;

  private String job;

  private String workplace;

  private boolean isTopStudent;

  private String feedback;

  private String state;

}

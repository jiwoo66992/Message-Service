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
public class GetMessageOutput {

  private Long id;

  private String content;

  private Integer userId;

  private String email;

  private Timestamp createdDate;

}

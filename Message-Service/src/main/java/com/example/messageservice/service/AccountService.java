package com.example.messageservice.service;

import com.example.messageservice.domain.entity.Account;
import com.example.messageservice.domain.entity.User;
import com.example.messageservice.input.RegisterInput;
import com.example.messageservice.input.ResetPasswordInput;
import com.example.messageservice.input.UpdateUserDetailInput;
import com.example.messageservice.output.FlagResponse;

import java.util.List;

public interface AccountService {

  Account getUserByEmail(String email);

  List<Account> getAllUserByKey(Integer id, String key);

  Account login(String email, String password);

  FlagResponse register(RegisterInput input);

  FlagResponse resetPassword(ResetPasswordInput input);

  User getUserDetail(String email);

  FlagResponse updateUserDetail(UpdateUserDetailInput input);

}

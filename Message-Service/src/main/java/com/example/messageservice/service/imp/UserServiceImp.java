package com.example.messageservice.service.imp;

import com.example.messageservice.repository.UserRepository;
import com.example.messageservice.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImp implements UserService {
  private final UserRepository userRepository;

  public UserServiceImp(UserRepository userRepository) {
    this.userRepository = userRepository;
  }



}

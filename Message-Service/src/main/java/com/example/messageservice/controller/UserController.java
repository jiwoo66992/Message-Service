package com.example.messageservice.controller;

import com.example.messageservice.service.MessageService;
import com.example.messageservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/email/{id}")
  public ResponseEntity<?> findUserByEmail(@PathVariable("id") Integer id, @RequestParam("key") String email) {
    //String content, boolean me, String name, String roomId
    return ResponseEntity.ok(userService.getAllUserByKey(id, email));
  }

}

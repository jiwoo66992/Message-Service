package com.example.messageservice.controller;

import com.example.messageservice.input.UpdateUserDetailInput;
import com.example.messageservice.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class AccountController {
  private final AccountService accountService;

  public AccountController(AccountService accountService) {
    this.accountService = accountService;
  }

  @GetMapping("/email/{id}")
  public ResponseEntity<?> findUserByEmail(@PathVariable("id") Integer id, @RequestParam("key") String email) {
    return ResponseEntity.ok(accountService.getAllUserByKey(id, email));
  }

  @GetMapping("/info/{email}")
  public ResponseEntity<?> getUserInfo(@PathVariable("email") String email) {
    return ResponseEntity.ok(accountService.getUserDetail(email));
  }

  @PatchMapping
  public ResponseEntity<?> changeUserInfo(@RequestBody UpdateUserDetailInput input) {
    return ResponseEntity.ok(accountService.updateUserDetail(input));
  }

}

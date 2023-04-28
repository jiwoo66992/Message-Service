package com.example.messageservice.controller;

import com.example.messageservice.input.RegisterInput;
import com.example.messageservice.input.ResetPasswordInput;
import com.example.messageservice.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
  private final AccountService accountService;

  public AuthController(AccountService accountService) {
    this.accountService = accountService;
  }

  @GetMapping("/login")
  public ResponseEntity<?> login(@RequestParam("email") String email, @RequestParam("password") String password) {
    return ResponseEntity.ok(accountService.login(email, password));
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody RegisterInput input) {
    return ResponseEntity.ok(accountService.register(input));
  }

  @PostMapping("/reset-password")
  public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordInput input) {
    return ResponseEntity.ok(accountService.resetPassword(input));
  }

}

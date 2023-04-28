package com.example.messageservice.controller;

import com.example.messageservice.input.CreateRoomChatInput;
import com.example.messageservice.service.RoomChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rooms")
public class RoomChatController {
  private final RoomChatService roomChatService;

  public RoomChatController(RoomChatService roomChatService) {
    this.roomChatService = roomChatService;
  }

  @PostMapping
  public ResponseEntity<?> createNewRoomChat(@RequestParam("userId") Integer userId, @RequestParam("friendId") Integer password) {
    return ResponseEntity.ok(roomChatService.createRoomChat(new CreateRoomChatInput(userId, password)));
  }

  @GetMapping("/{userId}")
  public ResponseEntity<?> getAllRoomChat(@PathVariable("userId") Integer userId) {
    return ResponseEntity.ok(roomChatService.getAllRoomChat(userId));
  }

}

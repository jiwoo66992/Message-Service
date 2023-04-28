package com.example.messageservice.socket;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.Transport;
import com.example.messageservice.domain.dto.MessageDto;
import com.example.messageservice.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SocketApplication {

  public static void startSocketApplication(MessageService messageService) {
    Configuration configuration = new Configuration();
    configuration.setHostname("192.168.31.246");
    configuration.setPort(8001);
    configuration.setTransports(Transport.POLLING);

    final SocketIOServer server = new SocketIOServer(configuration);

    server.addEventListener("client-leave-room", String.class, (client, data, ackRequest) -> {
      System.out.println("client-leave-room");
      client.leaveRoom(data);
    });

    //2 or nhiều client
    server.addEventListener("client-send-roomId", String.class, (client, data, ackRequest) -> {
      System.out.println("client-send-roomId: " + data);
      client.joinRoom(data);
    });

    server.addEventListener("client-send-message", String.class, (client, data, ackRequest) -> {
      System.out.println(data);

      ObjectMapper objectMapper = new ObjectMapper();
      MessageDto message = objectMapper.readValue(data, MessageDto.class);
      message.setMe(false);
      messageService.save(message);
      System.out.println("client-send-message");
      System.out.println("room: " + message.getRoomId());
      System.out.println("server-send-message");
      System.out.println(objectMapper.writeValueAsString(message));
      server.getRoomOperations(message.getRoomId()).sendEvent("server-send-message",
          objectMapper.writeValueAsString(message));
    });

    server.start();
  }

}
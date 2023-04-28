package com.example.messageservice;

import com.example.messageservice.service.MessageService;
import com.example.messageservice.socket.SocketApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class MessageServiceApplication implements CommandLineRunner {
  private final MessageService messageService;

  public MessageServiceApplication(MessageService messageService) {
    this.messageService = messageService;
    System.out.println(new BCryptPasswordEncoder().encode("Abc@1234"));
  }


  public static void main(String[] args) {
    SpringApplication.run(MessageServiceApplication.class, args);
  }

  @Override
  public void run(String... args) {
    SocketApplication.startSocketApplication(messageService);
  }

}

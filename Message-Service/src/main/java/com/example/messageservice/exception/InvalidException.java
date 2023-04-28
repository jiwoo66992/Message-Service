package com.example.messageservice.exception;

public class InvalidException extends RuntimeException {
  public InvalidException(String message) {
    super(message);
  }
}

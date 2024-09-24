package org.example.server.exception;

public class WrongIdException extends RuntimeException {
  public WrongIdException(String message) {
    super(message);
  }
}

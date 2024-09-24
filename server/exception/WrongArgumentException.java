package org.example.server.exception;

public class WrongArgumentException extends Exception{
    public WrongArgumentException() {
        super("Неверный аргумент для команды");
    }
}

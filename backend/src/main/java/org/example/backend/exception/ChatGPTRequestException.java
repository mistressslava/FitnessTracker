package org.example.backend.exception;

public class ChatGPTRequestException extends RuntimeException {
    public ChatGPTRequestException(String string) {
        super(string);
    }
}

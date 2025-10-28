package org.example.backend.planGenerator;

public class ChatGPTRequestException extends RuntimeException {
    public ChatGPTRequestException(String string) {
        super(string);
    }
}

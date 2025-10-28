package org.example.backend.planGenerator;

import java.util.List;

public record ChatGPTResponse(List<ChatGPTChoice> choices) {
    public String text() {
        return choices.getFirst().message().content();
    }
}

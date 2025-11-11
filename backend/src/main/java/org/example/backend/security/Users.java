package org.example.backend.security;

import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public record Users(@Id String id, @With @Indexed(unique = true) String username, @With String password){

    public Users {
        username = username == null ? null : username.trim();
    }

    public static Users oauth(String username) {
        return new Users(null, username, null);
    }
}
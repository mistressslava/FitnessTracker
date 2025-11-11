package org.example.backend.security;

import lombok.With;
import org.springframework.data.annotation.Id;

public record Users(@Id String id, @With String username, @With String password){

    public Users {
        username = username == null ? null : username.trim().toLowerCase();
    }

    public static Users oauth(String username) {
        return new Users(null, username, null);
    }
}

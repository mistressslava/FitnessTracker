package org.example.backend.security;

import lombok.With;
import org.springframework.data.annotation.Id;

public record Users(@Id String id, String username, @With String password){
}

package org.example.backend.security;

import org.springframework.data.annotation.Id;

public record User(@Id String id, String username, String password){
}

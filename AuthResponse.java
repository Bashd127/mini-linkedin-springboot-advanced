package com.example.minilinkedadvanced.dto;

public class AuthResponse {
    public String token;
    public Long userId;

    public AuthResponse(String token, Long userId) {
        this.token = token;
        this.userId = userId;
    }
}

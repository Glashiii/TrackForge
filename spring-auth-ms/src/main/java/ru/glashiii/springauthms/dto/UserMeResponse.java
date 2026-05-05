package ru.glashiii.springauthms.dto;

public record UserMeResponse(
        Long id,
        String email,
        String username,
        String roles
){}

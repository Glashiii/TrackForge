package ru.glashiii.springauthms.dto;

public record UserMeResponse(
        Integer id,
        String email,
        String username,
        String roles
){}

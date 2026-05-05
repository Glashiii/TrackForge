package ru.glashiii.springauthms.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.jackson.autoconfigure.JacksonProperties;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.glashiii.springauthms.dto.*;
import ru.glashiii.springauthms.entities.UserInfo;
import ru.glashiii.springauthms.services.AuthService;
import ru.glashiii.springauthms.services.UserInfoService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    private final UserInfoService userInfoService;
    private final AuthService authService;

    @PostMapping("/register")
    public String addNewUser(@Valid @RequestBody RegisterRequest request) {
        return userInfoService.addUser(request);
    }


    @PostMapping("/login")
    public AuthResponse authenticateAndGetToken(@Valid @RequestBody AuthRequest authRequest) {
        return authService.login(authRequest);
    }

    @PostMapping("/refresh")
    public AuthResponse refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refresh(refreshTokenRequest);
    }

    @GetMapping("/me")
    public UserMeResponse me(Authentication authentication) {
        UserInfo user = userInfoService.findByEmail(authentication.getName());

        return new UserMeResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRoles()
        );
    }

}

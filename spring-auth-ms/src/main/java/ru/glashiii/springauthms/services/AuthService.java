package ru.glashiii.springauthms.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.glashiii.springauthms.dto.AuthRequest;
import ru.glashiii.springauthms.dto.AuthResponse;
import ru.glashiii.springauthms.dto.RefreshTokenRequest;
import ru.glashiii.springauthms.dto.RotatedRefreshToken;
import ru.glashiii.springauthms.entities.UserInfo;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserInfoService userInfoService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                )
        );

        UserInfo user = userInfoService.findByEmail(authRequest.getEmail());
        String accessToken = jwtService.generateToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user);

        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse refresh(RefreshTokenRequest request) {
        RotatedRefreshToken rotated = refreshTokenService.rotateRefreshToken(request.getRefreshToken());

        String accessToken = jwtService.generateToken(rotated.user());

        return new AuthResponse(accessToken, rotated.refreshToken());
    }

}

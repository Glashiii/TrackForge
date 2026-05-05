package ru.glashiii.springauthms.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.glashiii.springauthms.dto.RotatedRefreshToken;
import ru.glashiii.springauthms.entities.RefreshToken;
import ru.glashiii.springauthms.entities.UserInfo;
import ru.glashiii.springauthms.repositories.RefreshTokenRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    public String createRefreshToken(UserInfo user) {
        String rawToken = generateSecureToken();

        RefreshToken refreshToken = RefreshToken.builder()
                .tokenHash(hashToken(rawToken))
                .user(user)
                .expiresAt(Instant.now().plusSeconds(60*60*24*7))
                .createdAt(Instant.now())
                .revokedAt(null)
                .revoked(false)
                .build();

        refreshTokenRepository.save(refreshToken);

        return rawToken;
    }

    @Transactional
    public RotatedRefreshToken rotateRefreshToken(String rawToken) {
        String tokenHash = hashToken(rawToken);

        RefreshToken oldRefreshToken = refreshTokenRepository.findByTokenHashForUpdate(tokenHash)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Invalid refresh token"
                ));

        if (oldRefreshToken.getExpiresAt().isBefore(Instant.now()) || oldRefreshToken.isRevoked()) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid refresh token"
            );
        }

        oldRefreshToken.setRevoked(true);
        oldRefreshToken.setRevokedAt(Instant.now());
        refreshTokenRepository.save(oldRefreshToken);

        String newRawRefreshToken = createRefreshToken(oldRefreshToken.getUser());

        return new RotatedRefreshToken(oldRefreshToken.getUser(), newRawRefreshToken);
    }

    private String generateSecureToken() {
        byte[] bytes = new byte[64];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}

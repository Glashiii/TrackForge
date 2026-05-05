package ru.glashiii.springauthms.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.glashiii.springauthms.config.JwtProperties;
import ru.glashiii.springauthms.entities.UserInfo;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@AllArgsConstructor
public class JwtService {
    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    private final JwtProperties jwtProperties;


    public String generateToken(UserInfo user) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("email", user.getEmail());
        claims.put("roles", user.getRoles());

        return createToken(claims, user);
    }

    private String createToken(Map<String, Object> claims, UserInfo user) {
        return Jwts
                .builder()
                .header()
                    .keyId(jwtProperties.getKeyId())
                    .and()
                .claims(claims)
                .subject(String.valueOf(user.getId()))
                .issuedAt(new Date())
                .issuer("spring-auth-ms")
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(privateKey, Jwts.SIG.RS256)
                .compact();
    }


    public String extractEmail(String token) {
        return extractClaim(token, claims -> claims.get("email", String.class));
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}

package ru.glashiii.springauthms.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.glashiii.springauthms.config.JwtProperties;

import java.math.BigInteger;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class JwksController {

    private final PublicKey publicKey;
    private final JwtProperties jwtProperties;

    @GetMapping("/.well-known/jwks.json")
    public Map<String, Object> jwks() {
        RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;

        Map<String, Object> key = Map.of(
                "kty", "RSA",
                "use", "sig",
                "kid", jwtProperties.getKeyId(),
                "alg", "RS256",
                "n", base64Url(rsaPublicKey.getModulus()),
                "e", base64Url(rsaPublicKey.getPublicExponent())
        );

        return Map.of("keys", List.of(key));
    }

    private String base64Url(BigInteger value) {
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(toUnsignedBytes(value));
    }

    private byte[] toUnsignedBytes(BigInteger value) {
        byte[] bytes = value.toByteArray();
        if (bytes.length > 1 && bytes[0] == 0) {
            return Arrays.copyOfRange(bytes, 1, bytes.length);
        }
        return bytes;
    }
}
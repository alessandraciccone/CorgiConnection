package alessandraciccone.CorgiConnection.security;

import alessandraciccone.CorgiConnection.entities.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTool {

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey getSigningKey() {
        // Assicurati che il secret sia lungo almeno 32 caratteri
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(User user) {
        long now = System.currentTimeMillis();
        long expirationTime = now + 1000L * 60 * 60 * 24 * 365; // 1 anno

        return Jwts.builder()
                .issuedAt(new Date(now))
                .expiration(new Date(expirationTime))
                .subject(user.getId().toString())
                .claim("username", user.getUsername())
                .claim("email", user.getEmail())
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

}

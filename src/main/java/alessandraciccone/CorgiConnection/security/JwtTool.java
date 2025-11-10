package alessandraciccone.CorgiConnection.security;

import alessandraciccone.CorgiConnection.entities.User;
import alessandraciccone.CorgiConnection.exceptions.UnauthorizedException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtTool {

    @Value("${jwt.secret}")
    private String secret;

    private static final long EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 365; // 1 anno

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("roles", user.getRoles().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public void verifyToken(String token) {
        if (token == null || token.split("\\.").length != 3) {
            throw new UnauthorizedException("Token non valido!");
        }
    }

    public UUID extractIdFromToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) throw new UnauthorizedException("Token non valido!");

            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);

            // Estrai manualmente "sub":"UUID"
            String search = "\"sub\":\"";
            int start = payloadJson.indexOf(search);
            if (start == -1) throw new UnauthorizedException("Token non valido!");

            start += search.length();
            int end = payloadJson.indexOf("\"", start);
            if (end == -1) throw new UnauthorizedException("Token non valido!");

            String uuidStr = payloadJson.substring(start, end);
            return UUID.fromString(uuidStr);

        } catch (Exception e) {
            throw new UnauthorizedException("Token non valido!");
        }
    }
}

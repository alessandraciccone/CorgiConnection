package alessandraciccone.CorgiConnection.security;
import alessandraciccone.CorgiConnection.entities.User;
import alessandraciccone.CorgiConnection.exceptions.UnauthorizedException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class JwtTool {
    @Value("${jwt.secret}")
    private String secret;

    public String createToken (User user) {
        return Jwts.builder().issuedAt(new Date(System.currentTimeMillis())).expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
                .subject(String.valueOf(user.getId())).signWith(Keys.hmacShaKeyFor(secret.getBytes())).compact();
    }

    public void verifyToken(String accessToken) {
        try {
            Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parse(accessToken);
        } catch (Exception ex) {
            throw new UnauthorizedException("Non autorizzato!");
        }
    }

    public UUID extractIdFromToken(String accessToken) {
        return UUID.fromString(Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build()
                .parseSignedClaims(accessToken)
                .getPayload()
                .getSubject());
    }
}

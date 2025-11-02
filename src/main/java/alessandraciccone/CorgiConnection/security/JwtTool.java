package alessandraciccone.CorgiConnection.security;





import alessandraciccone.CorgiConnection.entities.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.Keys;
@Component
public class JwtTool {
    @Value("${jwt.secret}")
    private String secret;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(User user){
        return Jwts.builder()
                //data emissione token
                .issuedAt((new Date(System.currentTimeMillis())))
                //data scadenza token(tra un anno)
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 365))
                //subject è id user
                .subject(String.valueOf(user.getId()))
                .claim("username", user.getUsername())
                .claim("email", user.getEmail())
                //firmo il token con la chiave segreta
                .signWith(getSigningKey()).compact();
    }
}

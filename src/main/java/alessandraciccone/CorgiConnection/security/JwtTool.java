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

    @Value("${jwt.secret}") // Recupera il secret dal file di configurazione
    private String secret; // Chiave segreta per firmare i token

    private static final long EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 365; // 1 anno

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }// Ottiene la chiave di firma dal secret

    public String generateToken(User user) { // Genera un token JWT per l'utente
        return Jwts.builder()
                .setSubject(user.getId().toString())// Imposta l'ID dell'utente come soggetto
                .claim("roles", user.getRoles().name())// Aggiunge i ruoli dell'utente come claim
                .setIssuedAt(new Date())// Imposta la data di emissione
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))// Imposta la data di scadenza
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)// Firma il token con la chiave segreta
                .compact();// Compatta il token in una stringa
    }


    public void verifyToken(String token) {// Verifica la validità del token
        if (token == null || token.split("\\.").length != 3) {// Controlla che il token non sia nullo e abbia tre parti
            throw new UnauthorizedException("Token non valido!");
        }
    }

    public UUID extractIdFromToken(String token) {// Estrae l'ID dell'utente dal token senza usare librerie esterne
        try {
            String[] parts = token.split("\\.");// Divide il token in tre parti
            if (parts.length != 3) throw new UnauthorizedException("Token non valido!");// Controlla che il token abbia tre parti

            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);// Decodifica la parte del payload

            // Estrai manualmente "sub":"UUID"
            String search = "\"sub\":\""; // Cerca la chiave "sub"
            int start = payloadJson.indexOf(search);// Trova l'inizio del valore
            if (start == -1) throw new UnauthorizedException("Token non valido!");// Se non trovato, lancia eccezione

            start += search.length();// Sposta l'indice all'inizio del valore
            int end = payloadJson.indexOf("\"", start);// Trova la fine del valore
            if (end == -1) throw new UnauthorizedException("Token non valido!");

            String uuidStr = payloadJson.substring(start, end);// Estrai la stringa UUID
            return UUID.fromString(uuidStr);

        } catch (Exception e) {
            throw new UnauthorizedException("Token non valido!");
        }
    }
}

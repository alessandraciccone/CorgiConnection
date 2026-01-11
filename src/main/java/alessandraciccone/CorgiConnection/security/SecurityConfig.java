package alessandraciccone.CorgiConnection.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration // indica che questa classe contiene configurazioni di Spring
@EnableWebSecurity// abilita le funzionalità di sicurezza web di Spring Security
@EnableMethodSecurity// abilita la sicurezza a livello di metodo
public class SecurityConfig {

    private final JwtFilter jwtFilter;// filtro personalizzato per l'autenticazione JWT

    public SecurityConfig(JwtFilter jwtFilter) {// costruttore per l'iniezione del filtro JWT
        this.jwtFilter = jwtFilter;
    }

    @Bean//
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .formLogin(form -> form.disable())// disabilita il login basato su form
                .csrf(csrf -> csrf.disable())// disabilita la protezione CSRF (utile per API REST)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))// imposta la gestione della sessione come stateless
                .authorizeHttpRequests(auth -> auth// configura le regole di autorizzazione delle richieste HTTP
                        .requestMatchers("/auth/**").permitAll()     // login, registrazione pubblici
                        .requestMatchers("/ws/**").permitAll()       //  WebSocket endpoint
                        .requestMatchers("/app/**").permitAll()      //  App destinazioni client WebSocket
                        .requestMatchers("/topic/**").permitAll()    // Topic destinazioni client WebSocket
                        .requestMatchers("/user/**").permitAll()     //  User-specific destinations
                        .anyRequest().authenticated()                // tutto il r esto protetto
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)// aggiunge il filtro JWT prima del filtro di autenticazione standard
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));// configura il CORS

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();// fornisce il gestore di autenticazione
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {// configura le regole CORS
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of(
                "http://localhost:5173",// per lo sviluppo locale
                "https://corgi-connection-5yycfjwqa-alessandraciccones-projects.vercel.app", // URL attuale
                "https://corgi-connection-ifr39rqjm-alessandraciccones-projects.vercel.app"  // URL vecchio
        ));

        // Aggiungi pattern per tutti i deploy Vercel
        config.setAllowedOriginPatterns(List.of(
                "https://corgi-connection-*.vercel.app",
                "https://*.vercel.app"
        ));

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));// consente tutti gli header
        config.setAllowCredentials(true);// consente l'invio di credenziali (cookie, header di autorizzazione)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);// applica la configurazione a tutte le rotte
        return source;
    }

} 

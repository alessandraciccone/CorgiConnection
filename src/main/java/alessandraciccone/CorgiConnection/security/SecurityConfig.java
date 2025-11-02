
/*
package alessandraciccone.CorgiConnection.security;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

public class SecurityConfig {

    //configuro la catena di filtri di sicurezza

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity JwtFilter jwtFilter) throws Exception {

        //disabilito il form predefinito di spring

        httpSecurity.formLogin(formLogin -> formLogin.disable());

        //disabilito CSFR

        httpSecurity.csrf(csfr -> csfr.disable());

        //non creo sessioni server-side

        httpSecurity.sessionManagement(sessions -> sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //configuro autorizzazione endpoint

        httpSecurity.authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/api/corgi-info/**").permitAll()
                .requestMatchers("/api/pet-friendly/**").permitAll()
                .requestMatchers("/api/quizzes/public/**").permitAll()
                .requestMatchers("/upload/**").permitAll()

                //tutti gli altri richiedono autorizzazione
                .anyRequest().authenticated()
        );
        //aggiungo filtro jwt
        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        //abilito il corso

        httpSecurity.cors(Customizer.withDefaults());


        @Bean
                public CorsConfigurationSource corsConfigurationSource(){
            CorsConfiguration configuration= new CorsConfiguration();


            configuration.setAllowedOrigins(List.of(
                    "http://localhost:3000",      // React in sviluppo locale
                    "http://localhost:5173",      // Vite in sviluppo locale
                    "https://corgi-connection.netlify.app"  // Frontend in produzione (esempio)
            ));

            configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));

            configuration.setAllowedHeaders(List.of("*"));

            configuration.setAllowCredentials(true);

            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);

            return source;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder(12);
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
            return configuration.getAuthenticationManager();
        }

    }

}

*/

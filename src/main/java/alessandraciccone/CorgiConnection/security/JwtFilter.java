package alessandraciccone.CorgiConnection.security;

import alessandraciccone.CorgiConnection.entities.User;
import alessandraciccone.CorgiConnection.exceptions.UnauthorizedException;
import alessandraciccone.CorgiConnection.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTool jwtTool;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // rimuove "Bearer "

            // Verifica il token
            jwtTool.verifyToken(token);

            // Estrai l'ID dell'utente dal token
            UUID userId = jwtTool.extractIdFromToken(token);

            // Recupera l'utente dal DB
            User user = userService.getUserById(userId);

            if (user != null) {
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                throw new UnauthorizedException("Utente non trovato");
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // Non applicare il filtro alle rotte /auth/** (login, registrazione)
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}

package alessandraciccone.CorgiConnection.security;

import alessandraciccone.CorgiConnection.entities.User;
import alessandraciccone.CorgiConnection.exceptions.UnauthorizedException;
import alessandraciccone.CorgiConnection.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class JwtFilter extends OncePerRequestFilter { // jwt filter intercetta le richieste HTTP e verifica la presenza e la validità del token JWT se c'è un token valido estrae l'id dell'utente dal token e recupera i dettagli dell'utente dal database quindi crea un oggetto di autenticazione e lo imposta nel contesto di sicurezza di Spring Security
    //oncePerRequestFilter garantisce che il filtro venga eseguito una sola volta per ogni richiesta

    @Autowired
    private JwtTool jwtTool; //dipendenza per gestire le operazioni sui token JWT

    @Autowired
    @Lazy // per evitare dipendenza circolare
    private UserService userService;

    @Override 
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException { // metodo principale del filtro che viene eseguito per ogni richiesta HTTP

        String authHeader = request.getHeader("Authorization"); // recupera l'intestazione di autorizzazione dalla richiesta HTTP

        if (authHeader != null && authHeader.startsWith("Bearer ")) {// verifica se l'intestazione è presente e inizia con "Bearer
            String token = authHeader.substring(7); // rimuove "Bearer "

            // Verifica il token
            jwtTool.verifyToken(token);

            // Estrai l'ID dell'utente dal token
            UUID userId = jwtTool.extractIdFromToken(token);

            // Recupera l'utente dal DB
            User user = userService.fetUserById(userId);

            if (user != null) {// se l'utente viene trovato nel database
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());// crea un oggetto di autenticazione utilizzando i dettagli dell'utente recuperati dal database
                SecurityContextHolder.getContext().setAuthentication(authentication);// imposta l'oggetto di autenticazione nel contesto di sicurezza di Spring Security
            } else {
                throw new UnauthorizedException("Utente non trovato");
            }// se l'utente non viene trovato viene lanciata un'eccezione di tipo UnauthorizedException
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
} //protected perchè metodo della superclasse OncePerRequestFilter il protected serve per permettere l'accesso al metodo dalle sottoclassi

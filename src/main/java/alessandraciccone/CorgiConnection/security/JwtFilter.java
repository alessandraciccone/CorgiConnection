
/*
package alessandraciccone.CorgiConnection.security;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTool jwtTool;

    @Autowired
    private UserService userService;

    // metodo che filtra ogni richiesta http

    @Override
    protected  void doFilterIntenal(httpServletRequest request,
                                    httpServletResponse response,
                                    FilterChain filterChain) throw ServletException{
        //uso AntPathMatcher x verificate se il path corrispone ai pattern
        AntPathMatcher pathMatcher= new AntPathMatcher();
        String path= request.getServletPath();

    }  // ENDPOINT PUBBLICI (non richiedono autenticazione)
        return pathMatcher.match("/auth/**", path) ||           // Login, Register
            pathMatcher.match("/api/corgi-info/**", path) || // Curiosità sui Corgi (pubbliche)
            pathMatcher.match("/api/pet-friendly/**", path) || // Luoghi pet-friendly (pubblici)
            pathMatcher.match("/api/quizzes/public/**", path); // Quiz pubblici

}*/

package alessandraciccone.CorgiConnection.exceptions;


import alessandraciccone.CorgiConnection.payloads.ErrorsDTO;
import alessandraciccone.CorgiConnection.payloads.ErrorsListDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice //gestisce le eccezioni a livello globale ogni volta che un controller lancia un eccezione passa prima da li




public class ExceptionsHandler {

  @ExceptionHandler({BadRequestException.class, DataIntegrityViolationException.class, BadCredentialsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)//gestisce le eccezioni come errori neldb o credenziali sbagliatte
    public ErrorsDTO handleBadRequest(Exception ex) {
        return new ErrorsDTO(ex.getMessage(), LocalDateTime.now());
    } //

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorsDTO handleNotFound(NotFoundException ex) {
        return new ErrorsDTO("Elemento non trovato in db o id incorretto", LocalDateTime.now());//gestisce le eccezioni di tipo not found
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsListDTO handleValidationErrors(ValidationException ex) {
        return new ErrorsListDTO(ex.getMessage(), LocalDateTime.now(), ex.getErrors()); //rimanda un oggetto con la lista degli errori di validazione
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorsDTO handleValidationErrors(AuthorizationDeniedException ex) {
        return new ErrorsDTO(ex.getMessage(), LocalDateTime.now());//gestisce le eccezioni di autorizzazione negata
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorsDTO handleUnahautorizedError(UnauthorizedException ex) {
        return new ErrorsDTO(ex.getMessage(), LocalDateTime.now());//gestisce le eccezioni di tipo non autorizzato
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorsDTO handleServerError(Exception ex) {
        ex.printStackTrace();
        return new ErrorsDTO("Errore nel server", LocalDateTime.now());//gestisce tutte le eccezioni generiche
    }
}



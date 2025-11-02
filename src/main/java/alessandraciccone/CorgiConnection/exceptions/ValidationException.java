package alessandraciccone.CorgiConnection.exceptions;

import java.util.List;

public class ValidationException extends RuntimeException {
    private List<String> errors;

    public ValidationException(List<String> errors) {
        super("Errori nella validazoione del payload");
        this.errors = errors;
    }
}

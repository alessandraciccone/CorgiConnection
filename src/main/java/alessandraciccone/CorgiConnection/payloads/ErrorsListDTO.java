package alessandraciccone.CorgiConnection.payloads;


import java.time.LocalDateTime;
import java.util.List;

public record ErrorsListDTO (
        String message,
        LocalDateTime timestamp,
        List<String> errors) {
}
// questo record serve a rappresentare una lista di errori con un messaggio generale, una data/ora e una lista di messaggi di errore specifici.
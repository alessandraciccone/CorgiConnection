package alessandraciccone.CorgiConnection.payloads;

import java.time.LocalDateTime;

public record ErrorsDTO(
        String message,
        LocalDateTime date) {}

// questo record serve a rappresentare un singolo errore con un messaggio e una data/ora associata.
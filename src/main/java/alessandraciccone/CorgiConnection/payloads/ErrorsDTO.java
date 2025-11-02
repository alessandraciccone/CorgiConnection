package alessandraciccone.CorgiConnection.payloads;

import java.time.LocalDateTime;

public record ErrorsDTO(
        String message,
        LocalDateTime date) {}

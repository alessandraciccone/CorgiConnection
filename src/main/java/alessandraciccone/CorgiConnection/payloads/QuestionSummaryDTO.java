package alessandraciccone.CorgiConnection.payloads;

import java.util.UUID;

public record QuestionSummaryDTO(
        UUID id,
        String questionText
) {
} // questo record serve a ritornare le informazioni di una domanda senza esporre dati sensibili come le risposte corrette

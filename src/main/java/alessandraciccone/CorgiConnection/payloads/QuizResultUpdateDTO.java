package alessandraciccone.CorgiConnection.payloads;

import jakarta.validation.constraints.NotNull;

public record QuizResultUpdateDTO(
        @NotNull(message="Il punteggio è obbligatorio")
        Integer score,
        @NotNull(message="Il numero totale delle domande è obbligatorio")
        Integer totalQuestions
) {
}

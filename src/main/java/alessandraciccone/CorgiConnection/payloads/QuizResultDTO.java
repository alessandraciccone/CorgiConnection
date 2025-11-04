package alessandraciccone.CorgiConnection.payloads;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record QuizResultDTO(
        @NotNull(message="l'Id dell'utente è obbligatorio!")
        UUID userId,

        @NotNull(message="L'id del quiz è obbligatorio")
        UUID quizId,

        @NotNull(message="Il punteggio è obbligatorio")
        Integer score,

        @NotNull(message="Il numero totale delle domande è obbligatorio")
        Integer totalQuestions
){}

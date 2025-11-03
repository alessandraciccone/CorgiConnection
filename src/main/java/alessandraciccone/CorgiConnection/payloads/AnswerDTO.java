package alessandraciccone.CorgiConnection.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record AnswerDTO(
        @NotBlank(message = "Il testo della risposta è obbligatorio")
        @Size(min = 1, max = 500, message = "La risposta deve essere tra 1 e 500 caratteri")
                String answerText,

        @NotNull(message = "Specificare se la risposta è corretta")
        Boolean isCorrect,

        @NotNull(message = "L'ID della domanda è obbligatorio")
        UUID questionId
) {}


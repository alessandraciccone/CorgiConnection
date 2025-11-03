package alessandraciccone.CorgiConnection.payloads;

import jakarta.validation.constraints.Size;

public record AnswerUpdateDTO(
        @Size(min = 1, max = 500, message = "La risposta deve essere tra 1 e 500 caratteri")
        String answerText,

        Boolean isCorrect
) {
}

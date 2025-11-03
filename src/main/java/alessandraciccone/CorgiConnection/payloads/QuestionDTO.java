package alessandraciccone.CorgiConnection.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record QuestionDTO(

        @NotBlank(message="Il testo della domanda è obbligatorio")
        String questionText,

        @NotNull(message="Id quiz è obbligatorio!")
        UUID quizId,

        List<UUID> answerIds,

        Integer orderNumber


) {}

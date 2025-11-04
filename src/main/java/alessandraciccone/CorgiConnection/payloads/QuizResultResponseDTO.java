package alessandraciccone.CorgiConnection.payloads;

import java.util.UUID;

public record QuizResultResponseDTO(

        UUID id,
        UUID userId,
        UUID quizId,
        Integer score,
        Integer totalQuestions,
        double percentage
) {
}

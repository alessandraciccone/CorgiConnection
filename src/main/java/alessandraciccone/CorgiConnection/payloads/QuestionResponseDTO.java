package alessandraciccone.CorgiConnection.payloads;

import java.util.List;
import java.util.UUID;

public record QuestionResponseDTO(
        UUID id,
        String questionText,
        UUID quizId,
        List<UUID> answerIds,
        Integer orderNumber
) {
}

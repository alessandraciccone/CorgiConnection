package alessandraciccone.CorgiConnection.payloads;

import java.util.UUID;
import java.util.List;
public record QuestionUpdateDTO(
        String questionText,
        UUID quizId,
        List<UUID> answerIds,
Integer orderNumber
) {
}

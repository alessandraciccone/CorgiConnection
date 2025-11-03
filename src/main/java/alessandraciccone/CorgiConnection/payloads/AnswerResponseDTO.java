package alessandraciccone.CorgiConnection.payloads;

import java.util.UUID;

public record AnswerResponseDTO(
        UUID id,
        String answerText,
        Boolean isCorrect,
        QuestionSummaryDTO question
) {
}

// QuizSubmissionDTO.java
package alessandraciccone.CorgiConnection.payloads;

import java.util.List;
import java.util.UUID;

public record QuizSubmissionDTO(
        List<AnswerSubmission> answers
) {
    public record AnswerSubmission(
            UUID questionId,
            UUID answerId
    ) {}
}
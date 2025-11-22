package alessandraciccone.CorgiConnection.payloads;

import java.util.UUID;

public record AnswerSubmissionDTO(
        UUID questionId,
        UUID answerId
) {}
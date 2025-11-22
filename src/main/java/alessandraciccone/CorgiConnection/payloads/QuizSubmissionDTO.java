package alessandraciccone.CorgiConnection.payloads;

import java.util.List;

public record QuizSubmissionDTO(
        List<AnswerSubmissionDTO> answers
) {}
package alessandraciccone.CorgiConnection.payloads;

import java.util.List;

public record QuizSubmissionDTO(
        List<AnswerSubmissionDTO> answers
) {}
// questo record serve a inviare le risposte date dall'utente per un quiz completo //